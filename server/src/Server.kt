import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import configuration.ServerConfigurationDTO
import framework.injectFromKoin
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import operation.OperationController
import org.ktorm.database.Database
import parameter.ParameterAPI
import parameter.ParameterController
import parameter.ParameterEntity
import parameter.ParameterListeners

class Server {

    fun start() {
        val lConfig = ConfigLoaderBuilder.default()
            .addResourceSource("/applicationConfig.json")
            .build()
            .loadConfigOrThrow<ServerConfigurationDTO>()
        val lDatabase: Database = DatabaseProvider.getDatabaseConnectionAndMigrate(
            aDatabaseConfigurationDTO = lConfig.database
        )
        DependencyContainer.bootstrap(
            aDatabase = lDatabase,
            aKafkaConfigurationDTO = lConfig.kafka,
        )
        val lApp = Javalin.create {
            it.defaultContentType = "application/json"
            it.enableCorsForAllOrigins()
        }.routes {
            path("parameter") {
                get(ParameterController::getOrCreate)
                put("{parameterId}", ParameterController::update)
                sse("listen", ParameterController::listenToParameterChanges)
            }
            path("operation") {
                post(OperationController::create)
                sse("listen/{protocol}", OperationController::listenToOperationChanges)
            }
        }.events {
            it.serverStopping {
                ParameterListeners.clearListeners()
            }
        }.start(lConfig.port)
        createInitialParameter()
        Runtime.getRuntime().addShutdownHook(Thread { lApp.stop() })
    }

    private fun createInitialParameter() {
        injectFromKoin<ParameterAPI>().create(ParameterEntity())
    }

}