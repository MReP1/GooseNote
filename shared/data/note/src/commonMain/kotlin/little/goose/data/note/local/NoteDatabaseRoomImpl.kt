package little.goose.data.note.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import little.goose.data.note.bean.Note
import little.goose.data.note.bean.NoteContentBlock
import little.goose.data.note.bean.NoteWithContent

class NoteDatabaseRoomImpl(
    private val noteDao: NoteDao
) : NoteDatabase {

    private val _deleteNoteIdListSharedFlow = MutableSharedFlow<List<Long>>()
    override val deleteNoteIdListFlow = _deleteNoteIdListSharedFlow.asSharedFlow()

    override fun getNote(noteId: Long): Flow<Note> {
        return noteDao.getNoteFlow(noteId)
    }

    override suspend fun upsertNote(note: Note): Long {
        return noteDao.upsertNote(note)
    }

    override fun getNoteWithContentFlow(noteId: Long): Flow<NoteWithContent> {
        return noteDao.getNoteWithContentMapFlow(noteId).filter(Map<*, *>::isNotEmpty).map {
            NoteWithContent(it.keys.first(), it.values.flatten())
        }
    }

    override suspend fun deleteNoteAndItsBlocks(noteId: Long) {
        return noteDao.deleteNoteAndItsBlocks(noteId)
    }

    override suspend fun deleteNoteAndItsBlocksList(noteIds: List<Long>) {
        return noteDao.deleteNotesAndItsBlocks(noteIds)
    }

    override suspend fun deleteBlock(id: Long) {
        return noteDao.deleteNoteContentBlock(id)
    }

    override suspend fun deleteBlockWithNoteId(noteId: Long) {
        return noteDao.deleteNoteContentBlocks(noteId)
    }

    override suspend fun upsertNoteContentBlock(noteContentBlock: NoteContentBlock): Long {
        return noteDao.upsertNoteContentBlock(noteContentBlock)
    }

    override suspend fun upsertNoteContentBlocks(noteContentBlocks: List<NoteContentBlock>) {
        return noteDao.upsertNoteContentBlock(noteContentBlocks)
    }

    override fun getNoteWithContentFlowByKeyword(keyword: String): Flow<List<NoteWithContent>> {
        return noteDao.getNoteWithContentMapFlow(keyword).mapToNoteWithContentList()
    }

    override fun getNoteWithContentFlow(): Flow<List<NoteWithContent>> {
        return noteDao.getNoteWithContentMapFlow().mapToNoteWithContentList()
    }

    private fun Flow<Map<Note, List<NoteContentBlock>>>.mapToNoteWithContentList(): Flow<List<NoteWithContent>> {
        return map { map -> map.toNoteWithContentList() }.flowOn(Dispatchers.Default)
    }

    private fun Map<Note, List<NoteContentBlock>>.toNoteWithContentList(): List<NoteWithContent> {
        return map { entry ->
            NoteWithContent(entry.key, entry.value)
        }.sortedByDescending { nwc ->
            nwc.note.time
        }
    }

}