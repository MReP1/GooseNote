package little.goose.note.ui.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import little.goose.shared.ui.screen.LittleGooseEmptyScreen
import little.goose.shared.ui.screen.LittleGooseLoadingScreen
import little.goose.shared.ui.search.SearchScreen

const val ROUTE_SEARCH_NOTE = "search_note"

fun NavController.navToSearchNote() {
    navigate(ROUTE_SEARCH_NOTE)
}

fun NavGraphBuilder.navSearchNoteRoute(
    navigateToNote: (Long) -> Unit,
    onBack: () -> Unit
) = composable(
    route = ROUTE_SEARCH_NOTE,
    enterTransition = {
        fadeIn(
            animationSpec = tween(200, easing = LinearOutSlowInEasing)
        ) + slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(200, easing = LinearOutSlowInEasing),
            initialOffset = { it / 6 }
        )
    },
    exitTransition = null,
    popExitTransition = {
        fadeOut(
            animationSpec = tween(200, easing = FastOutLinearInEasing)
        ) + slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(200, easing = FastOutLinearInEasing),
            targetOffset = { it / 6 }
        )
    },
) {
    SearchNoteRoute(
        modifier = Modifier.fillMaxSize(),
        onNavigateToNote = navigateToNote,
        onBack = onBack
    )
}

@Composable
fun SearchNoteRoute(
    modifier: Modifier = Modifier,
    onNavigateToNote: (Long) -> Unit,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val (state, action) = rememberSearchNoteStateHolder(snackbarHostState)
    SearchNoteScreen(
        modifier = modifier,
        screenState = state,
        snackbarHostState = snackbarHostState,
        onNavigateToNote = onNavigateToNote,
        action = action,
        onBack = onBack
    )
}

@Composable
private fun SearchNoteScreen(
    modifier: Modifier = Modifier,
    screenState: SearchNoteScreenState,
    snackbarHostState: SnackbarHostState,
    action: (SearchNoteIntent) -> Unit,
    onNavigateToNote: (Long) -> Unit,
    onBack: () -> Unit
) {

    SearchScreen(
        modifier = modifier,
        keyword = screenState.keyword,
        onKeywordChange = { action(SearchNoteIntent.Search(it)) },
        snackbarHostState = snackbarHostState,
        onBack = onBack
    ) {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                val durationMillis = 320
                if (this.initialState is SearchNoteState.Success &&
                    this.targetState is SearchNoteState.Success
                ) {
                    fadeIn(
                        initialAlpha = 0.8F, animationSpec = tween(durationMillis = 100)
                    ) togetherWith fadeOut(
                        animationSpec = tween(durationMillis = 100)
                    )
                } else {
                    fadeIn(
                        animationSpec = tween(durationMillis)
                    ) + slideIntoContainer(
                        towards = if (targetState is SearchNoteState.Success)
                            AnimatedContentTransitionScope.SlideDirection.Down
                        else AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        initialOffset = { offset -> offset / 2 }
                    ) togetherWith fadeOut(
                        animationSpec = tween(durationMillis)
                    ) + slideOutOfContainer(
                        towards = if (targetState is SearchNoteState.Success)
                            AnimatedContentTransitionScope.SlideDirection.Down
                        else AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(durationMillis),
                        targetOffset = { offset -> offset / 2 }
                    )
                }
            },
            targetState = screenState.state,
            label = "search note content"
        ) { state ->
            when (state) {
                is SearchNoteState.Empty -> {
                    LittleGooseEmptyScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is SearchNoteState.Loading -> {
                    LittleGooseLoadingScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is SearchNoteState.Success -> {
                    SearchNoteContent(
                        modifier = Modifier.fillMaxSize(),
                        noteColumnState = state.data,
                        onNavigateToNote = onNavigateToNote,
                        action = { action(SearchNoteIntent.NotebookIntent(it)) }
                    )
                }
            }
        }
    }
}