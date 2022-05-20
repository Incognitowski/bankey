import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import framework.extensionFunctions.parseFromJson
import io.javalin.testtools.JavalinTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.koin.core.context.stopKoin
import parameter.ParameterEntity
import kotlin.test.assertEquals

class ParameterControllerTest {

    private val app = Server().getJavalinApp(
        ConfigLoaderBuilder.default()
            .addResourceSource("/applicationConfig.json")
            .build()
            .loadConfigOrThrow()
    )

    @AfterEach
    fun afterEach() {
        stopKoin()
    }

    @Test
    fun `Properly fetches application parameters`() = JavalinTest.test(app) { _, lClient ->
        assertThat(lClient.get("/parameter").code).isEqualTo(200)
    }

    @Test
    fun `Properly updates application parameters`() = JavalinTest.test(app) { _, lClient ->
        assertDoesNotThrow {
            val lParameterEntity = lClient.get("/parameter").body?.string()?.parseFromJson<ParameterEntity>()
                ?: throw IllegalStateException("GET: /parameter did not return a valid JSON")
            lParameterEntity.processOperations = !lParameterEntity.processOperations
            val lResponse = lClient.put("/parameter/${lParameterEntity.parameterId}", lParameterEntity)
            assertEquals(lResponse.code, 200)
        }
    }

}