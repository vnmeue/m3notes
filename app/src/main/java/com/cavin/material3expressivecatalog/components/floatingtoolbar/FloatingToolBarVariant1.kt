/*
 * Designed and developed by MetichaHQ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cavin.material3expressivecatalog.components.floatingtoolbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingToolBarVariant1() {
    Scaffold(
        content = { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                LazyColumn(
                    state = rememberLazyListState(),
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val list = (0..75).map { it.toString() }
                    items(count = list.size) {
                        Text(
                            text = list[it],
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
                HorizontalFloatingToolbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = -ScreenOffset),
                    expanded = true,
                    leadingContent = { },
                    trailingContent = {
                        AppBarRow(
                            overflowIndicator = { menuState ->
                                IconButton(
                                    onClick = {
                                        if (menuState.isExpanded) {
                                            menuState.dismiss()
                                        } else {
                                            menuState.show()
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "Localized description",
                                    )
                                }
                            },
                        ) {
                            clickableItem(
                                onClick = { /* doSomething() */ },
                                icon = {
                                    Icon(
                                        Icons.Filled.Download,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "Download",
                            )
                            clickableItem(
                                onClick = { /* doSomething() */ },
                                icon = {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "Favorite",
                            )
                            clickableItem(
                                onClick = { /* doSomething() */ },
                                icon = {
                                    Icon(
                                        Icons.Filled.Add,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "Add",
                            )
                            clickableItem(
                                onClick = { /* doSomething() */ },
                                icon = {
                                    Icon(
                                        Icons.Filled.Person,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "Person",
                            )
                            clickableItem(
                                onClick = { /* doSomething() */ },
                                icon = {
                                    Icon(
                                        Icons.Filled.ArrowUpward,
                                        contentDescription = "Localized description",
                                    )
                                },
                                label = "ArrowUpward",
                            )
                        }
                    },
                    content = {
                        FilledIconButton(
                            modifier = Modifier.width(64.dp),
                            onClick = { /* doSomething() */ },
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Localized description")
                        }
                    },
                )
            }
        },
    )
}
