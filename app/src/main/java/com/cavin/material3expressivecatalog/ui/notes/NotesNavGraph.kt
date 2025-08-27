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

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument

const val NOTES_GRAPH_ROUTE = "notes_graph"

fun NavGraphBuilder.notesGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "notes_list",
        route = NOTES_GRAPH_ROUTE
    ) {
        composable("notes_list") {
            NotesListScreen(navController = navController)
        }
        composable(
            route = "note_preview/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            NotePreviewScreen(
                noteId = id,
                navController = navController
            )
        }
        composable(
            route = "note_editor?id={id}&imageUri={imageUri}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType; defaultValue = -1 },
                navArgument("imageUri") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val imageUri = backStackEntry.arguments?.getString("imageUri")?.takeIf { it.isNotEmpty() }
            NoteEditorScreen(
                navController = navController,
                noteId = if (id == -1) null else id,
                imageUri = imageUri
            )
        }
    }
}
