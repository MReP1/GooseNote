package little.goose.data.note.local

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import little.goose.data.note.bean.TABLE_NOTE
import org.koin.core.scope.Scope
import platform.Foundation.NSHomeDirectory

actual fun Scope.noteDatabaseFactory(): RoomNoteDatabase {
    val dbFile = NSHomeDirectory() + "/$TABLE_NOTE"
    return Room.databaseBuilder<RoomNoteDatabase>(
        name = dbFile,
    ).fallbackToDestructiveMigrationOnDowngrade(true)
        .addMigrations(*noteMigrations)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}