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

package com.spy.spychat.conversation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.spy.spychat.MainViewModel
import com.spy.spychat.R
import com.spy.spychat.data.TextBean
import com.spy.spychat.data.xunFeiUiState
import com.spy.spychat.theme.SpyChatTheme
import com.iflytek.aikit.core.AiHelper
import com.iflytek.cloud.ErrorCode
import java.util.Calendar

class ConversationFragment : Fragment() {

    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)

        activityViewModel.initAiSdk(
            context = requireContext(),
            onInitSuccess = {},
            onInitFail = {}) { content ->
            val author = getString(R.string.author)
            xunFeiUiState.addMessage(Message(author, content, Calendar.getInstance().timeNow()))
        }

        val onUserInput: (String) -> Unit = {
            activityViewModel.textList.add(TextBean(role = "user", it))
            val contents = activityViewModel.textList.toString()
            Log.d("ChatSpy", contents)
            val ret: Int = AiHelper.getInst().asyncChat(
                activityViewModel.chatParam,
                contents,
                activityViewModel.chatHandle.value
            )
            if (ret != 0) {
                Toast.makeText(requireContext(), "错误：$ret.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        activityViewModel.initIat(context = requireContext(), {
            Log.d("ChatSpy", "textFromSpeech changed:$it")
            xunFeiUiState.addMessage(
                Message(
                    getString(R.string.user),
                    it,
                    Calendar.getInstance().timeNow()
                )
            )
            onUserInput(it)
        }) {
            if (it != ErrorCode.SUCCESS) {
                Toast.makeText(requireContext(), "初始化失败，错误码：$it.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        setContent {
            SpyChatTheme {
                ConversationContent(
                    uiState = xunFeiUiState,
                    navigateToProfile = { user ->
                        if (user == getString(R.string.author)) {
                            // Click callback
                            val bundle = bundleOf("userId" to user)
                            findNavController().navigate(
                                R.id.nav_profile,
                                bundle
                            )
                        }
                    },
                    onUserInput = onUserInput,
                    onNavIconPressed = {
                        activityViewModel.openDrawer()
                    },
                    onSpeech = {
                        if (it == MotionEvent.ACTION_DOWN) {
                            activityViewModel.startListener(context)
                        } else {
                            activityViewModel.stopListener()
                        }
                    }
                )
            }
        }
    }
}

