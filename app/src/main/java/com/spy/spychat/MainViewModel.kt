/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spy.spychat

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spy.spychat.BuildConfig
import com.spy.spychat.data.JsonParser
import com.spy.spychat.data.TextBean
import com.iflytek.aikit.core.AIChatHandle
import com.iflytek.aikit.core.AiHelper
import com.iflytek.aikit.core.BaseLibrary
import com.iflytek.aikit.core.ChatListener
import com.iflytek.aikit.core.ChatParam
import com.iflytek.aikit.core.CoreListener
import com.iflytek.aikit.core.ErrType
import com.iflytek.cloud.InitListener
import com.iflytek.cloud.RecognizerListener
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechRecognizer
import com.iflytek.cloud.SpeechUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

/**
 * Used to communicate between screens.
 */
class MainViewModel : ViewModel() {
    private val TAG = this.javaClass.name
    private val _drawerShouldBeOpened = MutableStateFlow(false)
    val drawerShouldBeOpened = _drawerShouldBeOpened.asStateFlow()

    private val _chatHandle = MutableStateFlow("Spy")
    val chatHandle = _chatHandle.asStateFlow()

    private val initTime = SystemClock.uptimeMillis()
    fun isDataReady() = SystemClock.uptimeMillis() - initTime > 1000L

    private lateinit var onTextFromSpeech: (String) -> Unit

    // 讯飞开发平台申请appid、apiSecret、apiKey，将三个值填入local.properties中
    // ex:
    // appId="123456"
    // apiSecret="abcdefghijkopqrstuvwxyz"
    // apiKey="abcdefghijkopqrstuvwxyz"
    val appId = BuildConfig.appId
    private val apiSecret = BuildConfig.apiSecret
    private val apiKey = BuildConfig.apiKey


    val chatParam = ChatParam.builder()
        .domain("general")
        .auditing("default")
        .uid("uid")

    val textList = mutableListOf<TextBean>()
    val rspContent = StringBuilder()

    private lateinit var iat: SpeechRecognizer

    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }

    fun initAiSdk(
        context: Context,
        onInitSuccess: () -> Unit,
        onInitFail: () -> Unit,
        onMessageRespond: (String) -> Unit
    ) {
        AiHelper.getInst().registerListener(CoreListener { type: ErrType?, code: Int ->
            when (type) {
                ErrType.AUTH -> {
                    onInitSuccess()
                }

                else -> {
                    onInitFail()
                }
            }
        })
        AiHelper.getInst().registerChatListener(object : ChatListener {
            override fun onChatOutput(p0: AIChatHandle?, p1: String?, p2: String?, p3: Int) {
                _chatHandle.value = p0?.usrContext as String? ?: "Spy"
                p2?.let {
                    rspContent.append(it)
                }
            }

            override fun onChatError(p0: AIChatHandle?, p1: Int, p2: String?) {
                _chatHandle.value = p0?.usrContext as String? ?: "Spy"
                onMessageRespond("错误: err:$p1 errDesc:$p2\n")
            }

            override fun onChatToken(p0: AIChatHandle?, p1: Int, p2: Int, p3: Int) {
                _chatHandle.value = p0?.usrContext as String? ?: "Spy"
                onMessageRespond(rspContent.toString())
                textList.add(TextBean(role = "assistant", content = rspContent.toString()))
                rspContent.clear()
            }
        })

        viewModelScope.launch(Dispatchers.Default) {
            val params = BaseLibrary.Params.builder()
                .appId(appId)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .workDir("/sdcard/workDir").build()
            AiHelper.getInst().init(context, params)
        }
    }

    fun unInitSdk() {
        AiHelper.getInst().unInit()
    }

    fun startListener(context: Context): Int {
        setParam(context)
        return iat.startListening(recognizerListener)
    }

    fun stopListener() {
        iat.stopListening()
    }

    fun cancelListener() {
        iat.cancel()
    }

    fun initIat(context: Context, onTextSpeech: (String) -> Unit, initListener: InitListener) {
        SpeechUtility.createUtility(
            context.applicationContext,
            SpeechConstant.APPID + "=${appId}"
        )
        val r = SpeechRecognizer.createRecognizer(context, initListener)
        if (r == null) {
            initListener.onInit(-1)
        } else {
            iat = r
            onTextFromSpeech = onTextSpeech
        }
    }

    private val recognizerListener: RecognizerListener = object : RecognizerListener {
        override fun onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
        }

        override fun onError(error: SpeechError) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            Log.d(TAG, "onError " + error.getPlainDescription(true))
        }

        override fun onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        }

        override fun onResult(results: RecognizerResult, isLast: Boolean) {
            Log.d(TAG, results.resultString)
            if (isLast) {
                Log.d(TAG, "onResult 结束")
                val r = printResult(results)
                if (r != "") {
                    onTextFromSpeech(r)
                }
            }
        }

        override fun onVolumeChanged(volume: Int, data: ByteArray) {
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    }

    private fun printResult(results: RecognizerResult): String {
        val text: String = JsonParser.parseIatResult(results.resultString)
        val iatResult: HashMap<String, String> = LinkedHashMap()
        val resultBuffer = StringBuffer()
        var sn: String? = null
        // 读取json结果中的sn字段
        try {
            val resultJson = JSONObject(results.resultString)
            sn = resultJson.optString("sn")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        sn?.let {
            iatResult[sn] = text
            iatResult.onEach {
                resultBuffer.append(it.value)
            }
        }
        return resultBuffer.toString()
    }

    private fun setParam(context: Context) {
        // 清空参数
        iat.setParameter(SpeechConstant.PARAMS, null)
        // 设置听写引擎
        iat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
        // 设置返回结果格式
        iat.setParameter(SpeechConstant.RESULT_TYPE, "json")

        iat.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
        // 设置语言区域
        iat.setParameter(SpeechConstant.ACCENT, "mandarin")

        Log.e(
            TAG,
            "last language:" + iat.getParameter(SpeechConstant.LANGUAGE)
        )

        //此处用于设置dialog中不显示错误码信息
        //iat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        iat.setParameter(
            SpeechConstant.VAD_BOS,
            "60000"
        )

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        iat.setParameter(
            SpeechConstant.VAD_EOS,
            "60000"
        )

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        iat.setParameter(
            SpeechConstant.ASR_PTT,
            "1"
        )

        // 设置音频保存路径，保存音频格式支持pcm、wav.
        iat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        iat.setParameter(
            SpeechConstant.ASR_AUDIO_PATH,
            context.getExternalFilesDir("msc")?.absolutePath + "/iat.wav"
        )
    }
}
