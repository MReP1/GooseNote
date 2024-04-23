package little.goose.note

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import little.goose.note.ui.search.navToSearchNote
import little.goose.note.ui.search.searchNoteRoute

@Composable
fun NoteApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ROUTE_NOTEBOOK_HOME
    ) {
        navNotebookHome(
            onNavigateToNote = navController::navToNote,
            onNavigateToSearch = navController::navToSearchNote
        )
        noteRoute(
            onBack = navController::popBackStack
        )
        searchNoteRoute(
            onBack = navController::popBackStack,
            navigateToNote = navController::navToNote
        )
    }
}