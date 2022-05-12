import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import configuration.DatabaseConfigurationDTO
import framework.exception.DatabaseConfigurationException
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import org.ktorm.support.postgresql.PostgreSqlDialect
import javax.sql.DataSource

object DatabaseProvider {

    private enum class DatabaseProvider {
        PGSQL,
        H2
    }

    private fun migrate(aDatabaseProvider: DatabaseProvider, aDataSource: DataSource) {
        when (aDatabaseProvider) {
            DatabaseProvider.PGSQL -> {
                Flyway.configure()
                    .dataSource(aDataSource)
                    .locations("migrations/pgsql")
                    .load()
                    .migrate()
            }
            DatabaseProvider.H2 -> {
                Flyway.configure()
                    .dataSource(aDataSource)
                    .locations("migrations/h2")
                    .load()
                    .migrate()
            }
        }
    }

    private fun getDataSource(aDatabaseProvider: DatabaseProvider, aDatabaseConfigurationDTO: DatabaseConfigurationDTO) : DataSource {
        return when (aDatabaseProvider) {
            DatabaseProvider.PGSQL -> {
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
            DatabaseProvider.H2 -> {
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
    }

    fun getDatabaseConnectionAndMigrate(aDatabaseConfigurationDTO: DatabaseConfigurationDTO): Database {
        val lDatabaseProvider = try {
            DatabaseProvider.valueOf(aDatabaseConfigurationDTO.provider)
        } catch (e: Exception) {
            throw DatabaseConfigurationException("Invalid database provider: '${aDatabaseConfigurationDTO.provider}'. Use one of: ${DatabaseProvider.values()}")
        }
        val lDataSource = getDataSource(lDatabaseProvider, aDatabaseConfigurationDTO)
        migrate(lDatabaseProvider, lDataSource)
        return when (lDatabaseProvider) {
            DatabaseProvider.PGSQL -> Database.connect(dataSource = lDataSource, dialect = PostgreSqlDialect())
            DatabaseProvider.H2 -> Database.connect(dataSource = lDataSource)
        }
    }

}