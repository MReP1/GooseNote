package little.goose.note

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.Flow
import little.goose.data.note.domain.DeleteBlockUseCase
import little.goose.data.note.domain.DeleteNoteAndItsBlocksUseCase
import little.goose.data.note.domain.GetNoteWithContentFlowWithNoteIdUseCase
import little.goose.data.note.domain.InsertOrReplaceNoteContentBlockUseCase
import little.goose.data.note.domain.InsertOrReplaceNoteContentBlocksUseCase
import little.goose.data.note.domain.InsertOrReplaceNoteUseCase
import little.goose.note.event.NoteScreenEvent
import little.goose.note.ui.note.NoteScreen
import little.goose.note.ui.note.NoteScreenIntent
import little.goose.note.ui.note.NoteScreenState
import org.koin.compose.koinInject

const val ROUTE_NOTE = "note"

const val KEY_NOTE_ID = "note_id"

const val FULL_ROUTE_NOTE = "$ROUTE_NOTE?$KEY_NOTE_ID={$KEY_NOTE_ID}"

fun NavController.navToNote(noteId: Long) {
    navigate("$ROUTE_NOTE?$KEY_NOTE_ID=${noteId}")
}

fun NavGraphBuilder.navNoteRoute(onBack: () -> Unit) {
    composable(
        route = FULL_ROUTE_NOTE,
        arguments = listOf(
            navArgument(KEY_NOTE_ID) {
                type = NavType.LongType
                defaultValue = 0
            }
        )
    ) {
        val insertOrReplaceNoteContentBlocks: InsertOrReplaceNoteContentBlocksUseCase = koinInject()
        val getNoteWithContentFlowWithNoteId: GetNoteWithContentFlowWithNoteIdUseCase = koinInject()
        val insertOrReplaceNoteContentBlock: InsertOrReplaceNoteContentBlockUseCase = koinInject()
        val insertOrReplaceNote: InsertOrReplaceNoteUseCase = koinInject()
        val deleteNoteAndItsBlocks: DeleteNoteAndItsBlocksUseCase = koinInject()
        val deleteNoteContentBlockUseCase: DeleteBlockUseCase = koinInject()
        val viewModel = viewModel(
            modelClass = NoteViewModel::class,
            factory = viewModelFactory {
                addInitializer(NoteViewModel::class) {
                    NoteViewModel(
                        createSavedStateHandle(),
                        insertOrReplaceNoteContentBlocks,
                        getNoteWithContentFlowWithNoteId,
                        insertOrReplaceNoteContentBlock,
                        insertOrReplaceNote,
                        deleteNoteAndItsBlocks,
                        deleteNoteContentBlockUseCase
                    )
                }
            }
        )
        val screenState by viewModel.noteScreenStateHolder.noteScreenState.collectAsState()
        NoteScreenRoute(
            modifier = Modifier.fillMaxSize(),
            onBack = onBack,
            event = viewModel.noteScreenStateHolder.event,
            screenState = screenState,
            action = viewModel.noteScreenStateHolder.action
        )
    }
}

@Composable
fun NoteScreenRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    event: Flow<NoteScreenEvent>,
    screenState: NoteScreenState,
    action: (NoteScreenIntent) -> Unit
) {
    val blockColumnState = rememberLazyListState()

    NoteScreen(
        modifier = modifier,
        onBack = onBack,
        noteScreenState = screenState,
        blockColumnState = blockColumnState,
        action = action
    )

    LaunchedEffect(event) {
        event.collect { event ->
            when (event) {
                is NoteScreenEvent.AddNoteBlock -> {
                    if (event.blockIndex != -1) {
                        // 标题占了一个位置，所以要 +1
                        blockColumnState.animateScrollToItem(event.blockIndex + 1, 0)
                    }
                    // 申请焦点
                    runCatching { event.focusRequester.requestFocus() }
                }
            }
        }
    }

}