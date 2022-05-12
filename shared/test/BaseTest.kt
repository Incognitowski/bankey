import configuration.DatabaseConfigurationDTO
import framework.databaseDependencyModule
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import parameter.parameterDependencyModule

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest : KoinTest {

    private val mDatabaseConfigurationDTO = DatabaseConfigurationDTO(provider = "H2")

    @JvmField
    @RegisterExtension
    @Suppress("unused")
    val koinTestExtension = KoinTestExtension.create {
        modules(
            databaseDependencyModule(DatabaseProvider.getDatabaseConnection(mDatabaseConfigurationDTO)),
            parameterDependencyModule(),
        )
    }

    @BeforeAll
    fun beforeAll() {
        Flyway.configure()
            .dataSource(DatabaseProvider.getDatabaseDataSource(mDatabaseConfigurationDTO))
            .locations("migrations/h2")
            .load()
            .migrate()
    }

}