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

package com.spy.spychat.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val SpyChatDarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Blue20,
    primaryContainer = Blue30,
    onPrimaryContainer = Color.White,
    inversePrimary = Color.White,
    secondary = Color.White,
    onSecondary = DarkBlue20,
    secondaryContainer = DarkBlue30,
    onSecondaryContainer = Color.White,
    tertiary = Yellow80,
    onTertiary = Yellow20,
    tertiaryContainer = Yellow30,
    onTertiaryContainer = Yellow90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Gray10,
    onBackground = Gray90,
    surface = Gray10,
    onSurface = Gray80,
    inverseSurface = Gray90,
    inverseOnSurface = Gray20,
    surfaceVariant = BlueGray30,
    onSurfaceVariant = BlueGray80,
    outline = BlueGray60
)

private val SpyChatLightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = Blue20,
    primaryContainer = Blue90,
    onPrimaryContainer = Color.White,
    inversePrimary = Color.White,
    secondary = DarkBlue40,
    onSecondary = Color.White,
    secondaryContainer =  Color.White,
    onSecondaryContainer = DarkBlue10,
    tertiary = Yellow40,
    onTertiary = Color.White,
    tertiaryContainer = Yellow90,
    onTertiaryContainer = Yellow10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Color.White,
    onBackground = Color.White,
    surface = Color.White,
    onSurface = Gray10,
    inverseSurface = Color.White,
    inverseOnSurface = Color.White,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.White,
    outline = BlueGray50
)

@SuppressLint("NewApi")
@Composable
fun SpyChatTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val myColorScheme = SpyChatLightColorScheme
//    val myColorScheme = when {
//        isDynamicColor && isDarkTheme -> {
//            dynamicDarkColorScheme(LocalContext.current)
//        }
//        isDynamicColor && !isDarkTheme -> {
//            dynamicLightColorScheme(LocalContext.current)
//        }
//        isDarkTheme -> SpyChatDarkColorScheme
//        else -> SpyChatLightColorScheme
//    }

    MaterialTheme(
        colorScheme = myColorScheme,
        typography = SpyChatTypography,
        content = content
    )
}
