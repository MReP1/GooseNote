package little.goose.data.note.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import little.goose.data.note.bean.Note
import little.goose.data.note.bean.NoteContentBlock

@Database(
    entities = [Note::class, NoteContentBlock::class],
    version = 4,
    exportSchema = false
)
@ConstructedBy(RoomNoteDatabaseConstructor::class)
abstract class RoomNoteDatabase : RoomDatabase() {
    abstract fun userDao(): NoteDao
}

// generate by ksp, ignore
expect object RoomNoteDatabaseConstructor : RoomDatabaseConstructor<RoomNoteDatabase>