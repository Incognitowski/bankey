package parameter

import BaseTest
import framework.injectFromKoin
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ParameterAPITest : BaseTest() {

    @Test
    fun `Should properly create ParameterEntity`() {
        val lParameterAPI: ParameterAPI = injectFromKoin()
        lParameterAPI.create(ParameterEntity(processOperations = true))
        assertNotNull(lParameterAPI.findLatest())
    }

    @Test
    fun `Should properly update ParameterEntity`() {
        val lParameterAPI: ParameterAPI = injectFromKoin()
        val lParameterEntity = ParameterEntity(processOperations = true)
        lParameterAPI.create(lParameterEntity)
        lParameterEntity.processOperations = false
        lParameterAPI.update(lParameterEntity)
        val lParameterEntityForValidation = lParameterAPI.findLatest()
        assertNotNull(lParameterEntityForValidation)
        assertFalse(lParameterEntityForValidation.processOperations)
    }

}