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

package com.cavin.material3expressivecatalog.ui.notes

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cavin.material3expressivecatalog.data.model.Note
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(navController: NavController, viewModel: NotesViewModel = hiltViewModel()) {
    val notes by viewModel.notes.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // State for FAB menu
    var isFabExpanded by remember { mutableStateOf(false) }
    
    // Camera launcher for taking photos
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            // Save bitmap to file and get URI
            val file = File(context.cacheDir, "note_image_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                it.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            val uri = Uri.fromFile(file)
            navController.navigate("note_editor?imageUri=${uri}")
        }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { 
                    Text("Notes", style = MaterialTheme.typography.titleLarge) 
                },
                actions = {
                    // Add any action buttons here if needed
                }
            ) 
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // FAB Menu Items
                if (isFabExpanded) {
                    // Text Note FAB
                    ExtendedFloatingActionButton(
                        onClick = {
                            isFabExpanded = false
                            navController.navigate("note_editor")
                        },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Icon(Icons.Filled.TextFields, "Text Note", modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Text Note", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    
                    // Photo Note FAB
                    ExtendedFloatingActionButton(
                        onClick = {
                            isFabExpanded = false
                            cameraLauncher.launch(null)
                        },
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ) {
                        Icon(Icons.Filled.PhotoCamera, "Photo Note", modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Photo Note", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                
                // Main FAB - Large and expressive
                LargeFloatingActionButton(
                    onClick = { isFabExpanded = !isFabExpanded },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        Icons.Filled.Add,
                        "Create Note",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No notes yet. Tap + to create one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteNote(note)
                                true
                            } else {
                                false
                            }
                        }
                    )
                    
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            val color = when (dismissState.targetValue) {
                                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.surface
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color, RoundedCornerShape(12.dp))
                                    .padding(16.dp),
                                contentAlignment = androidx.compose.ui.Alignment.CenterEnd
                            ) {
                                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        "Delete",
                                        tint = MaterialTheme.colorScheme.onError,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        },
                        content = {
                            Card(
                                shape = MaterialTheme.shapes.large,
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .clickable { navController.navigate("note_preview/${note.id}") }
                            ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .animateContentSize(animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = note.title.ifEmpty { "Untitled" },
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (note.content.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = note.content,
                                            style = MaterialTheme.typography.bodyLarge,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                // Show image thumbnails if available
                                val imagesToShow = if (note.imageUris.isNotEmpty()) {
                                    note.imageUris
                                } else if (note.imageUri != null) {
                                    listOf(note.imageUri)
                                } else {
                                    emptyList()
                                }
                                
                                if (imagesToShow.isNotEmpty()) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Box {
                                        // Show first image as main thumbnail
                                        AsyncImage(
                                            model = imagesToShow.first(),
                                            contentDescription = "Note image",
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(RoundedCornerShape(16.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        
                                        // Show count badge if multiple images
                                        if (imagesToShow.size > 1) {
                                            Card(
                                                modifier = Modifier
                                                    .align(androidx.compose.ui.Alignment.BottomEnd)
                                                    .padding(4.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.primary
                                                )
                                            ) {
                                                Text(
                                                    text = "+${imagesToShow.size - 1}",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                            }
                        }
                    )
                }
            }
        }
    }
}