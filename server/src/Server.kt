import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import configuration.ServerConfigurationDTO
import io.javalin.Javalin
import org.ktorm.database.Database

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

        }.start(lConfig.port)
    }

}