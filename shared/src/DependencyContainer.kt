import configuration.KafkaConfigurationDTO
import framework.DatabaseDependencyModule
import framework.KafkaConfigurationDependencyModule
import operation.OperationDependencyModule
import operationEvent.OperationEventDependencyModule
import org.koin.core.context.startKoin
import org.ktorm.database.Database
import parameter.ParameterDependencyModule

object DependencyContainer {

    fun bootstrap(aDatabase: Database, aKafkaConfigurationDTO: KafkaConfigurationDTO) {
        startKoin {
            modules(
                KafkaConfigurationDependencyModule(aKafkaConfigurationDTO),
                DatabaseDependencyModule(aDatabase),
                ParameterDependencyModule(),
                OperationEventDependencyModule(),
                OperationDependencyModule(),
            )
        }
    }

}