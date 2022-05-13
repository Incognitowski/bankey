import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import configuration.ServerConfigurationDTO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.ktorm.database.Database
import parameter.ParameterController

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
        Javalin.create {
            it.defaultContentType = "application/json"
            it.enableCorsForAllOrigins()
        }.routes {
            path("parameter") {
                get(ParameterController::getOrCreate)
                sse("aaa", ParameterController::listenToParameterChanges)
            }
        }.start(lConfig.port)
    }

}