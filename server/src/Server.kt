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

    fun getJavalinApp(aServerConfigurationDTO : ServerConfigurationDTO) : Javalin {
        val lDatabase: Database = DatabaseProvider.getDatabaseConnectionAndMigrate(
            aDatabaseConfigurationDTO = aServerConfigurationDTO.database
        )
        DependencyContainer.bootstrap(
            aDatabase = lDatabase,
            aKafkaConfigurationDTO = aServerConfigurationDTO.kafka,
        )
        val lJavalin = Javalin.create {
            it.defaultContentType = "application/json"
            it.enableCorsForAllOrigins()
        }.routes {
            path("parameter") {
                get(ParameterController::getLatest)
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
        }
        createInitialParameter()
        Runtime.getRuntime().addShutdownHook(Thread { lJavalin.stop() })
        return lJavalin
    }

    fun start(): Javalin {
        val lServerConfigurationDTO = ConfigLoaderBuilder.default()
            .addResourceSource("/applicationConfig.json")
            .build()
            .loadConfigOrThrow<ServerConfigurationDTO>()
        return getJavalinApp(lServerConfigurationDTO).start(lServerConfigurationDTO.port)
    }

    private fun createInitialParameter() {
        injectFromKoin<ParameterAPI>().create(ParameterEntity())
    }

}