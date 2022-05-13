import configuration.DatabaseConfigurationDTO
import framework.exception.DatabaseConfigurationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DatabaseProviderTest {

    @Test
    fun `Should raise exception when getting connection with invalid database provider`() {
        assertThrows<DatabaseConfigurationException> {
            DatabaseProvider.getDatabaseConnectionAndMigrate(DatabaseConfigurationDTO(provider = "invalidProvider"))
        }
    }

    @Test
    fun `Should successfully get connection with H2 database`() {
        assertDoesNotThrow {
            DatabaseProvider.getDatabaseConnectionAndMigrate(DatabaseConfigurationDTO(provider = "H2"))
        }
    }

}