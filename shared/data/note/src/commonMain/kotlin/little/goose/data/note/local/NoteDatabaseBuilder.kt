package little.goose.data.note.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import org.koin.core.scope.Scope

expect fun Scope.noteDatabaseFactory(): RoomNoteDatabase

internal val noteMigrations = arrayOf(
    object : Migration(startVersion = 1, endVersion = 2) {
        override fun migrate(connection: SQLiteConnection) {
            // do nothing..
        }
    },
    object : Migration(startVersion = 2, endVersion = 3) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                sql = "CREATE TABLE IF NOT EXISTS `note_content_block` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `note_id` INTEGER, `content` TEXT NOT NULL);"
            )
        }
    },
    object : Migration(startVersion = 3, endVersion = 4) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                sql = "ALTER TABLE `note_content_block` ADD COLUMN `index` INTEGER NOT NULL DEFAULT 0;"
            )
        }
    }
)