package framework

import org.koin.dsl.module
import org.ktorm.database.Database

fun databaseDependencyModule(aDatabase : Database) : org.koin.core.module.Module {
    return module {
        single { aDatabase }
    }
}