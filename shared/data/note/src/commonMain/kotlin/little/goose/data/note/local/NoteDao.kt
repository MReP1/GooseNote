package little.goose.data.note.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import little.goose.data.note.bean.Note
import little.goose.data.note.bean.NoteContentBlock
import little.goose.data.note.bean.TABLE_NOTE
import little.goose.data.note.bean.TABLE_NOTE_CONTENT_BLOCK

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note): Long

    @Query("SELECT * FROM $TABLE_NOTE WHERE id = :noteId")
    fun getNoteFlow(noteId: Long): Flow<Note>

    @Query("DELETE FROM $TABLE_NOTE WHERE id = :noteId")
    suspend fun deleteNote(noteId: Long)

    @Query("DELETE FROM $TABLE_NOTE WHERE id IN (:noteIdList)")
    suspend fun deleteNote(noteIdList: List<Long>)

    @Upsert
    suspend fun upsertNoteContentBlock(noteContentBlock: NoteContentBlock): Long

    @Upsert
    suspend fun upsertNoteContentBlock(noteContentBlocks: List<NoteContentBlock>)

    @Transaction
    @Query(
        "SELECT * FROM $TABLE_NOTE " +
                "JOIN $TABLE_NOTE_CONTENT_BLOCK " +
                "ON $TABLE_NOTE.id = $TABLE_NOTE_CONTENT_BLOCK.note_id " +
                "WHERE $TABLE_NOTE.id = :noteId " +
                "ORDER BY $TABLE_NOTE_CONTENT_BLOCK.`index` ASC"
    )
    fun getNoteWithContentMapFlow(noteId: Long): Flow<Map<Note, List<NoteContentBlock>>>

    @Transaction
    @Query(
        "SELECT * FROM $TABLE_NOTE " +
                "JOIN $TABLE_NOTE_CONTENT_BLOCK " +
                "ON $TABLE_NOTE.id = $TABLE_NOTE_CONTENT_BLOCK.note_id " +
                "WHERE $TABLE_NOTE.title LIKE '%'|| :keyword ||'%' OR $TABLE_NOTE_CONTENT_BLOCK.content LIKE '%'|| :keyword ||'%' " +
                "ORDER BY $TABLE_NOTE_CONTENT_BLOCK.`index` ASC"
    )
    fun getNoteWithContentMapFlow(keyword: String): Flow<Map<Note, List<NoteContentBlock>>>

    @Transaction
    @Query(
        "SELECT * FROM $TABLE_NOTE " +
                "JOIN $TABLE_NOTE_CONTENT_BLOCK " +
                "ON $TABLE_NOTE.id = $TABLE_NOTE_CONTENT_BLOCK.note_id " +
                "ORDER BY $TABLE_NOTE_CONTENT_BLOCK.`index` ASC"
    )
    fun getNoteWithContentMapFlow(): Flow<Map<Note, List<NoteContentBlock>>>

    @Query("DELETE FROM $TABLE_NOTE_CONTENT_BLOCK WHERE id = :id")
    suspend fun deleteNoteContentBlock(id: Long)

    @Query("DELETE FROM $TABLE_NOTE_CONTENT_BLOCK WHERE note_id = :noteId")
    suspend fun deleteNoteContentBlocks(noteId: Long)

    @Query("DELETE FROM $TABLE_NOTE_CONTENT_BLOCK WHERE note_id IN (:noteIdList)")
    suspend fun deleteNoteContentBlocks(noteIdList: List<Long>)

    @Transaction
    suspend fun deleteNoteAndItsBlocks(id: Long) {
        deleteNoteContentBlocks(id)
        deleteNote(id)
    }

    @Transaction
    suspend fun deleteNotesAndItsBlocks(ids: List<Long>) {
        deleteNoteContentBlocks(ids)
        deleteNote(ids)
    }

}