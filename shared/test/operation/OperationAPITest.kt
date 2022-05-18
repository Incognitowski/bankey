package operation

import BaseTest
import framework.exception.BusinessRuleException
import framework.exception.EntityNotFoundException
import framework.injectFromKoin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import parameter.ParameterAPI
import parameter.ParameterEntity
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OperationAPITest : BaseTest() {

    private fun createNewParameters(processOperations: Boolean = true) {
        injectFromKoin<ParameterAPI>().create(ParameterEntity(processOperations = processOperations))
    }

    @Test
    fun `Should properly insert operation`() {
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity()
        assertDoesNotThrow {
            lOperationAPI.create(lOperationEntity)
            assertNotNull(lOperationAPI.findByProtocol(lOperationEntity.protocol))
        }
    }

    @Test
    fun `Should raise exception when trying to create an operation when operation processing is disabled`() {
        createNewParameters(false)
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity()
        assertThrows<BusinessRuleException> {
            lOperationAPI.create(lOperationEntity)
        }
    }

    @Test
    fun `Should raise exception when trying to create an operation when parameters where not created`() {
        beforeAll()
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity()
        assertThrows<BusinessRuleException> {
            lOperationAPI.create(lOperationEntity)
        }
    }

    @Test
    fun `Should raise exception when trying to update an operation when operation processing is disabled`() {
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity()
        lOperationAPI.create(lOperationEntity)
        createNewParameters(false)
        assertThrows<BusinessRuleException> {
            lOperationEntity.status = OperationConstants.Status.processing
            lOperationAPI.update(lOperationEntity)
        }
    }

    @Test
    fun `Should raise exception when trying to update non existent operation`() {
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity()
        lOperationAPI.create(lOperationEntity)
        lOperationEntity.operationId = 999_999
        assertThrows<EntityNotFoundException> {
            lOperationAPI.update(lOperationEntity)
        }
    }

    @Test
    fun `Should raise exception when trying to skip processing status of operation`() {
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity()
        lOperationAPI.create(lOperationEntity)
        lOperationEntity.status = OperationConstants.Status.success
        assertThrows<BusinessRuleException> {
            lOperationAPI.update(lOperationEntity)
        }
        lOperationEntity.status = OperationConstants.Status.failure
        assertThrows<BusinessRuleException> {
            lOperationAPI.update(lOperationEntity)
        }
    }

    @Test
    fun `Should raise exception when trying to roll back status of operation`() {
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity()
        lOperationAPI.create(lOperationEntity)
        lOperationEntity.status = OperationConstants.Status.processing
        lOperationAPI.update(lOperationEntity)
        lOperationEntity.status = OperationConstants.Status.awaiting
        assertThrows<BusinessRuleException> {
            lOperationAPI.update(lOperationEntity)
        }
        lOperationEntity.status = OperationConstants.Status.success
        lOperationAPI.update(lOperationEntity)
        lOperationEntity.status = OperationConstants.Status.processing
        assertThrows<BusinessRuleException> {
            lOperationAPI.update(lOperationEntity)
        }
    }

    @Test
    fun `Should raise exception when trying to change type of operation`() {
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity().also { it.type = OperationConstants.Type.deposit }
        lOperationAPI.create(lOperationEntity)
        lOperationEntity.type = OperationConstants.Type.withdrawal
        assertThrows<BusinessRuleException> {
            lOperationAPI.update(lOperationEntity)
        }
    }

    @Test
    fun `Should raise exception when listing operations by invalid status`() {
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        assertThrows<BusinessRuleException> {
            lOperationAPI.listByStatus("definitely_invalid_status")
        }
    }

    @Test
    fun `Should properly query operations by status`() {
        beforeAll()
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        repeat(3) { lOperationAPI.create(getValidOperationEntity()) }
        val lListOfOperationEntity = lOperationAPI.listByStatus(OperationConstants.Status.awaiting)
        assertEquals(3, lListOfOperationEntity.size)
    }

    @Test
    fun `Should properly query operations by account identifier`() {
        createNewParameters()
        val lOperationAPI: OperationAPI = injectFromKoin()
        repeat(3) { lOperationAPI.create(getValidOperationEntity().also { it.account = "myAccountIdentifier" }) }
        val lListOfOperationEntity = lOperationAPI.listByAccount("myAccountIdentifier")
        assertEquals(3, lListOfOperationEntity.size)
    }

}