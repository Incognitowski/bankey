import framework.DatabaseDependencyModule
import operation.OperationDependencyModule
import operationEvent.OperationEventDependencyModule
import org.koin.core.context.startKoin
import org.ktorm.database.Database
import parameter.ParameterDependencyModule

object DependencyContainer {

    fun bootstrap(aDatabase: Database) {
        startKoin {
            modules(
                DatabaseDependencyModule(aDatabase),
                ParameterDependencyModule(),
                OperationEventDependencyModule(),
                OperationDependencyModule(),
            )
        }
    }

}