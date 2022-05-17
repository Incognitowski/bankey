package framework

import org.koin.core.module.Module
import org.koin.dsl.module
import org.ktorm.database.Database

object DatabaseDependencyModule {
    operator fun invoke(aDatabase: Database): Module {
        return module {
            single { aDatabase }
        }
    }
}