import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import configuration.ServerConfigurationDTO
import io.javalin.Javalin
import org.ktorm.database.Database

class Server {

    fun start() {
        // 1. Read config
        val lConfig = ConfigLoaderBuilder.default()
            .addResourceSource("/applicationConfig.json")
            .build()
            .loadConfigOrThrow<ServerConfigurationDTO>()
        // 2. Start DI
        val lDatabase: Database = DatabaseProvider.getDatabaseConnectionAndMigrate(
            aDatabaseConfigurationDTO = lConfig.database
        )

        DependencyContainer.bootstrap(
            aDatabase = lDatabase
        )
        // 3. Migrate DB
        Javalin.create {

        }.start(7070)
    }

}