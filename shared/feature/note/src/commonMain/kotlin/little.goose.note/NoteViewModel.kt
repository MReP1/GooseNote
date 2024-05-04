package little.goose.note

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import little.goose.data.note.domain.DeleteBlockUseCase
import little.goose.data.note.domain.DeleteNoteAndItsBlocksUseCase
import little.goose.data.note.domain.GetNoteWithContentFlowWithNoteIdUseCase
import little.goose.data.note.domain.InsertOrReplaceNoteContentBlockUseCase
import little.goose.data.note.domain.InsertOrReplaceNoteContentBlocksUseCase
import little.goose.data.note.domain.InsertOrReplaceNoteUseCase
import little.goose.note.logic.note.NoteScreenStateHolder
import org.koin.compose.koinInject

@Composable
fun noteViewModel(): NoteViewModel {
    val insertOrReplaceNoteContentBlocks: InsertOrReplaceNoteContentBlocksUseCase = koinInject()
    val getNoteWithContentFlowWithNoteId: GetNoteWithContentFlowWithNoteIdUseCase = koinInject()
    val insertOrReplaceNoteContentBlock: InsertOrReplaceNoteContentBlockUseCase = koinInject()
    val insertOrReplaceNote: InsertOrReplaceNoteUseCase = koinInject()
    val deleteNoteAndItsBlocks: DeleteNoteAndItsBlocksUseCase = koinInject()
    val deleteNoteContentBlockUseCase: DeleteBlockUseCase = koinInject()
    return viewModel(
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
}

class NoteViewModel(
    savedStateHandle: SavedStateHandle,
    insertOrReplaceNoteContentBlocks: InsertOrReplaceNoteContentBlocksUseCase,
    getNoteWithContentFlowWithNoteId: GetNoteWithContentFlowWithNoteIdUseCase,
    insertOrReplaceNoteContentBlock: InsertOrReplaceNoteContentBlockUseCase,
    insertOrReplaceNote: InsertOrReplaceNoteUseCase,
    deleteNoteAndItsBlocks: DeleteNoteAndItsBlocksUseCase,
    deleteNoteContentBlockUseCase: DeleteBlockUseCase
) : ViewModel() {

    private val noteId = savedStateHandle.get<Long>(KEY_NOTE_ID)!!

    val noteScreenStateHolder = NoteScreenStateHolder(
        noteId,
        viewModelScope,
        insertOrReplaceNoteContentBlocks,
        getNoteWithContentFlowWithNoteId,
        insertOrReplaceNoteContentBlock,
        insertOrReplaceNote,
        deleteNoteAndItsBlocks,
        deleteNoteContentBlockUseCase
    )

    override fun onCleared() {
        super.onCleared()
        noteScreenStateHolder.clear()
    }

}