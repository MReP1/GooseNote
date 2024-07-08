package little.goose.data.note.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

internal const val TABLE_NOTE_CONTENT_BLOCK = "note_content_block"

@Entity(tableName = TABLE_NOTE_CONTENT_BLOCK)
data class NoteContentBlock(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo("note_id")
    val noteId: Long? = null,
    @ColumnInfo("content")
    val content: String = "",
    @ColumnInfo("index")
    val sectionIndex: Long = 0,
)