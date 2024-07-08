package little.goose.data.note.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import little.goose.shared.common.getCurrentTimeMillis

internal const val TABLE_NOTE = "note"

@Entity(tableName = TABLE_NOTE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "time")
    val time: Long = getCurrentTimeMillis(),
    @ColumnInfo(name = "content")
    val content: String = ""
)