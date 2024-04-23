package little.goose.note.di

import little.goose.data.note.di.noteDatabaseModule
import org.koin.dsl.module

val sharedNoteFeatureModule = module {

    includes(noteDatabaseModule)

}