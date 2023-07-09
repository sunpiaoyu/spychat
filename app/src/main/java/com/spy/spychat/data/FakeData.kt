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

package com.spy.spychat.data

import androidx.compose.ui.graphics.Color
import com.spy.spychat.R
import com.spy.spychat.conversation.ConversationUiState
import com.spy.spychat.conversation.Message
import com.spy.spychat.conversation.timeNow
import com.spy.spychat.data.EMOJIS.EMOJI_SMILE
import com.spy.spychat.profile.ProfileScreenState
import java.util.Calendar

private val initialMessages = listOf(
    Message(
        "小宇",
        "你可以问我任何问题，我将尽力为你解答。$EMOJI_SMILE",
        Calendar.getInstance().timeNow()
    )
)

val xunFeiUiState = ConversationUiState(
    initialMessages = initialMessages,
    channelName = "星火大模型",
    iconId = R.drawable.ic_xunfei,
    color = Color(0xFF2A82E4)
)

val openAIUiState = ConversationUiState(
    initialMessages = initialMessages,
    channelName = "ChatGpt",
    iconId = R.drawable.ic_openai,
    color = Color(0xFF43CF7C)
)

/**
 * Example "me" profile.
 */
val meProfile = ProfileScreenState(
    userId = "author",
    photo = R.drawable.spy,
    name = "小宇",
    displayName = "spy",
    position = "Senior Android Dev at Yearin\nGoogle Developer Expert",
    email = "twitter.com/aliconors"
)

object EMOJIS {
    // EMOJI 15
    const val EMOJI_PINK_HEART = "\uD83E\uDE77"

    const val EMOJI_SMILE =  "\ud83d\ude04" // Smiling Face With Open Mouth and Smiling Eyes
    // EMOJI 14 🫠
    const val EMOJI_MELTING = "\uD83E\uDEE0"

    // ANDROID 13.1 😶‍🌫️
    const val EMOJI_CLOUDS = "\uD83D\uDE36\u200D\uD83C\uDF2B️"

    // ANDROID 12.0 🦩
    const val EMOJI_FLAMINGO = "\uD83E\uDDA9"

    // ANDROID 12.0  👉
    const val EMOJI_POINTS = " \uD83D\uDC49"
}
