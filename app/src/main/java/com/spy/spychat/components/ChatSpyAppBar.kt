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

@file:OptIn(ExperimentalMaterial3Api::class)

package com.spy.spychat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spy.spychat.R
import com.spy.spychat.theme.SpyChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpyChatAppBar(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(id = R.drawable.ic_more),
    tint : Color = Color(0xFF0A84FF),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { },
    title: @Composable () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = title,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            SpyChatIcon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable(onClick = onNavIconPressed)
                    .padding(start = 4.dp),
                painter = painter,
                tint = tint,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SpyChatAppBarPreview() {
    SpyChatTheme {
        SpyChatAppBar(title = { Text("Preview!") })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SpyChatAppBarPreviewDark() {
    SpyChatTheme(isDarkTheme = true) {
        SpyChatAppBar(title = { Text("Preview!") })
    }
}
