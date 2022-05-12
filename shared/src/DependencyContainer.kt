import framework.databaseDependencyModule
import org.koin.core.context.startKoin
import org.ktorm.database.Database
import parameters.parameterDependencyModule

object DependencyContainer {

    fun bootstrap(aDatabase: Database) {
        startKoin {
            modules(
                databaseDependencyModule(aDatabase),
                parameterDependencyModule()
            )
        }
    }

}