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

package com.spy.spychat.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spy.spychat.R
import com.spy.spychat.data.meProfile
import com.spy.spychat.theme.Blue30
import com.spy.spychat.theme.Blue40
import com.spy.spychat.theme.SpyChatTheme

@Composable
fun SpyChatDrawerContent(
    onProfileClicked: (String) -> Unit,
    onChatClicked: (String) -> Unit
) {
    // Use windowInsetsTopHeight() to add a spacer which pushes the drawer content
    // below the status bar (y-axis)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        DrawerHeader()
        DividerItem()
        var iconOpen1 by remember { mutableStateOf(false) }
        DrawerItemHeader("切换功能", iconOpen1) {
            iconOpen1 = !iconOpen1
        }
        if (iconOpen1) {
            ChatItem(
                "星火大模型",
                painterResource(id = R.drawable.ic_xunfei),
                tint = Color(0xFF2A82E4),
                true
            ) { onChatClicked("xunFei") }
            ChatItem(
                "openAI",
                painterResource(id = R.drawable.ic_openai),
                tint = Color(0xFF43CF7C),
                false
            ) { onChatClicked("openAI") }
            ChatItem(
                "AI虚拟人",
                painterResource(id = R.drawable.ai_man),
                tint = Color(0x8800BAAD),
                false
            ) { onChatClicked("openAI") }
        }
        DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
        var iconOpen2 by remember { mutableStateOf(false) }
        DrawerItemHeader("作者", iconOpen2) {
            iconOpen2 = !iconOpen2
        }

        if (iconOpen2) {
            ProfileItem(stringResource(id = R.string.author), meProfile.photo) {
                onProfileClicked(
                    meProfile.userId
                )
            }
        }
        DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
        DrawerItemHeader("开源声明", false) {

        }
    }
}

@Composable
private fun DrawerHeader() {
    Row(
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = CenterVertically
    ) {
        SpyChatIcon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = R.drawable.ic_spy_lc),
            tint = Color(0xff000000)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_spychat_logo),
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun DrawerItemHeader(text: String, iconOpen: Boolean = false, onItemClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .heightIn(min = 56.dp)
            .padding(horizontal = 28.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onItemClicked,
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ),
        contentAlignment = CenterStart
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
            modifier = Modifier.align(CenterStart)
        )
        Icon(
            painter = if (iconOpen) painterResource(id = R.drawable.to_down) else painterResource(id = R.drawable.to_r),
            modifier = Modifier
                .size(16.dp)
                .align(CenterEnd),
            contentDescription = null
        )
    }
}

@Composable
private fun ChatItem(
    text: String,
    icon: Painter,
    tint: Color,
    selected: Boolean,
    onChatClicked: () -> Unit
) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .then(background)
            .clickable(onClick = onChatClicked),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            painter = icon,
            tint = tint,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            contentDescription = null
        )
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) {
                Blue40
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
private fun ProfileItem(text: String, @DrawableRes profilePic: Int?, onProfileClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .clickable(onClick = onProfileClicked),
        verticalAlignment = CenterVertically
    ) {
        val paddingSizeModifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            .size(24.dp)
        if (profilePic != null) {
            Image(
                painter = painterResource(id = profilePic),
                modifier = paddingSizeModifier.then(Modifier.clip(CircleShape)),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        } else {
            Spacer(modifier = paddingSizeModifier)
        }
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
@Preview
fun DrawerPreview() {
    SpyChatTheme {
        Surface {
            Column {
                SpyChatDrawerContent({}, {})
            }
        }
    }
}

@Composable
@Preview
fun DrawerPreviewDark() {
    SpyChatTheme(isDarkTheme = true) {
        Surface {
            Column {
                SpyChatDrawerContent({}, {})
            }
        }
    }
}
