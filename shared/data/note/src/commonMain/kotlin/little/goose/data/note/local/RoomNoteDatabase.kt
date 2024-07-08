package little.goose.data.note.local

import androidx.room.Database
import androidx.room.RoomDatabase
import little.goose.data.note.bean.Note
import little.goose.data.note.bean.NoteContentBlock

@Database(
    entities = [Note::class, NoteContentBlock::class],
    version = 4,
    exportSchema = false
)
abstract class RoomNoteDatabase : RoomDatabase() {
    abstract fun userDao(): NoteDao
}