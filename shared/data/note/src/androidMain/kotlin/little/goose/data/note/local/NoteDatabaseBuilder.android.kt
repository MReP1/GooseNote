package little.goose.data.note.local

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import little.goose.data.note.bean.TABLE_NOTE
import org.koin.core.scope.Scope

actual fun Scope.noteDatabaseFactory(): RoomNoteDatabase {
    val context: Context = get()
    val dbFile = context.getDatabasePath(TABLE_NOTE)
    return Room.databaseBuilder<RoomNoteDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    ).fallbackToDestructiveMigrationOnDowngrade(true)
        .addMigrations(*noteMigrations)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}