package little.goose.note

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import little.goose.note.ui.search.navToSearchNote
import little.goose.note.ui.search.navSearchNoteRoute

@Composable
fun NoteApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ROUTE_NOTEBOOK_HOME,
        enterTransition = { activityEnterTransition() },
        exitTransition = { activityExitTransition() },
        popEnterTransition = { activityPopEnterTransition() },
        popExitTransition = { activityPopExitTransition() }
    ) {
        navNotebookHome(
            onNavigateToNote = navController::navToNote,
            onNavigateToSearch = navController::navToSearchNote
        )
        navNoteRoute(
            onBack = navController::popBackStack
        )
        navSearchNoteRoute(
            onBack = navController::popBackStack,
            navigateToNote = navController::navToNote
        )
    }
}

private const val DEFAULT_ENTER_DURATION = 300
private const val DEFAULT_EXIT_DURATION = 220

private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = tween(DEFAULT_ENTER_DURATION, easing = LinearOutSlowInEasing),
        initialOffset = { it }
    )
}

@Suppress("UnusedReceiverParameter")
private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityExitTransition(): ExitTransition {
    return scaleOut(
        animationSpec = tween(DEFAULT_ENTER_DURATION),
        targetScale = 0.96F
    )
}

@Suppress("UnusedReceiverParameter")
private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityPopEnterTransition(): EnterTransition {
    return scaleIn(
        animationSpec = tween(DEFAULT_EXIT_DURATION),
        initialScale = 0.96F
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityPopExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = tween(DEFAULT_EXIT_DURATION, easing = FastOutLinearInEasing),
        targetOffset = { it }
    )
}