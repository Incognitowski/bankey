import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import configuration.ServerConfigurationDTO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
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
        }.routes {
            path("parameter") {
                get(ParameterController::getOrCreate)
            }
        }.start(lConfig.port)
    }

}