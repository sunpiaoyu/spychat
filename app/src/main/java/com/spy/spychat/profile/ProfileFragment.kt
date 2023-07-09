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

package com.spy.spychat.profile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.spy.spychat.FunctionalityNotAvailablePopup
import com.spy.spychat.MainViewModel
import com.spy.spychat.R
import com.spy.spychat.components.SpyChatAppBar
import com.spy.spychat.theme.SpyChatTheme

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Consider using safe args plugin
        val userId = arguments?.getString("userId")
        viewModel.setUserId(userId)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_profile, container, false)

        rootView.findViewById<ComposeView>(R.id.toolbar_compose_view).apply {
            setContent {
                var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
                if (functionalityNotAvailablePopupShown) {
                    FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
                }

                SpyChatTheme {
                    SpyChatAppBar(
                        // Reset the minimum bounds that are passed to the root of a compose tree
                        modifier = Modifier.wrapContentSize(),
                        painter = painterResource(id = R.drawable.ic_back),
                        tint = Color(0xff007AFF),
                        onNavIconPressed = {
                            Log.d("ChatSpy", "activity = ${this@ProfileFragment.activity}")
                            this@ProfileFragment.findNavController().navigateUp()
                        },
                        title = { }
                    )
                }
            }
        }

        rootView.findViewById<ComposeView>(R.id.profile_compose_view).apply {
            setContent {
                val userData by viewModel.userData.observeAsState()

                SpyChatTheme {
                    if (userData == null) {
                        ProfileError()
                    } else {
                        ProfileScreen(
                            userData = userData!!,
                        )
                    }
                }
            }
        }
        return rootView
    }
}
