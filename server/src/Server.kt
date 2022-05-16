import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import configuration.ServerConfigurationDTO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.ktorm.database.Database
import parameter.ParameterController
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
            aDatabase = lDatabase
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
        }.events {
            it.serverStopping {
                ParameterListeners.clearListeners()
            }
        }.start(lConfig.port)
        Runtime.getRuntime().addShutdownHook(Thread { lApp.stop() })
    }

}