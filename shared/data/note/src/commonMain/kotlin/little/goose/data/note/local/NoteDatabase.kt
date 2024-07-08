package little.goose.data.note.local

import kotlinx.coroutines.flow.Flow
import little.goose.data.note.bean.Note
import little.goose.data.note.bean.NoteContentBlock
import little.goose.data.note.bean.NoteWithContent

interface NoteDatabase {

    val deleteNoteIdListFlow: Flow<List<Long>>

    fun getNote(noteId: Long): Flow<Note>

    suspend fun upsertNote(note: Note): Long

    fun getNoteWithContentFlow(noteId: Long): Flow<NoteWithContent>

    suspend fun deleteNoteAndItsBlocks(noteId: Long)

    suspend fun deleteNoteAndItsBlocksList(noteIds: List<Long>)

    suspend fun deleteBlock(id: Long)

    suspend fun deleteBlockWithNoteId(noteId: Long)

    suspend fun upsertNoteContentBlock(noteContentBlock: NoteContentBlock): Long

    suspend fun upsertNoteContentBlocks(noteContentBlocks: List<NoteContentBlock>)

    fun getNoteWithContentFlow(): Flow<List<NoteWithContent>>

    fun getNoteWithContentFlowByKeyword(keyword: String): Flow<List<NoteWithContent>>
}