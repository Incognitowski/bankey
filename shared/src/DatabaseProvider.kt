import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import configuration.DatabaseConfigurationDTO
import framework.exception.DatabaseConfigurationException
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import org.ktorm.support.postgresql.PostgreSqlDialect

object DatabaseProvider {

    private enum class DatabaseProviders {
        PGSQL,
        H2
    }

    fun getDatabaseConnectionAndMigrate(aDatabaseConfigurationDTO: DatabaseConfigurationDTO): Database {
        val lDatabaseProvider = try {
            DatabaseProviders.valueOf(aDatabaseConfigurationDTO.provider)
        } catch (e: Exception) {
            throw DatabaseConfigurationException("Invalid database provider: '${aDatabaseConfigurationDTO.provider}'. Use one of: ${DatabaseProviders.values()}")
        }

        val lDataSource = when (lDatabaseProvider) {
            DatabaseProviders.PGSQL -> {
                HikariDataSource(
                    HikariConfig().apply {
                        driverClassName = "org.postgresql.Driver"
                        jdbcUrl = "jdbc:postgresql://${aDatabaseConfigurationDTO.host}:${aDatabaseConfigurationDTO.port}/${aDatabaseConfigurationDTO.name}"
                        username = aDatabaseConfigurationDTO.user
                        password = aDatabaseConfigurationDTO.password
                        maximumPoolSize = 20
                        minimumIdle = 5
                        isAutoCommit = true
                        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                        validate()
                    }
                )
            }
            DatabaseProviders.H2 -> {
                HikariDataSource(
                    HikariConfig().apply {
                        driverClassName = "org.h2.Driver"
                        jdbcUrl = "jdbc:h2:mem:~/test;USER=h2user;PASSWORD=password;MODE=PostgreSQL;DATABASE_TO_UPPER=false"
                        maximumPoolSize = 20
                        minimumIdle = 5
                        isAutoCommit = true
                        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                        validate()
                    }
                )
            }
        }

        Flyway.configure()
            .dataSource(lDataSource)
            .locations("migrations")
            .load()
            .migrate()

        return when (lDatabaseProvider) {
            DatabaseProviders.PGSQL -> Database.connect(dataSource = HikariDataSource(lDataSource), dialect = PostgreSqlDialect())
            DatabaseProviders.H2 -> Database.connect(dataSource = HikariDataSource(lDataSource))
        }
    }

}