package parameter

import BaseTest
import framework.exception.EntityNotFoundException
import framework.injectFromKoin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    @Test
    fun `Should raise exception when trying to update a non-existent parameter`() {
        val lParameterAPI: ParameterAPI = injectFromKoin()
        assertThrows<EntityNotFoundException> {
            lParameterAPI.update(ParameterEntity(parameterId = 9999, processOperations = true))
        }
    }

}