package operation

import BaseTest
import framework.exception.BusinessRuleException
import framework.exception.EntityNotFoundException
import framework.injectFromKoin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull

class OperationAPITest : BaseTest() {

    @Test
    fun `Should properly insert operation`() {
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity()
        assertDoesNotThrow {
            lOperationAPI.create(lOperationEntity)
            assertNotNull(lOperationAPI.findByProtocol(lOperationEntity.protocol))
        }
    }

    @Test
    fun `Should raise exception when trying to update non existent operation`() {
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
        val lOperationAPI: OperationAPI = injectFromKoin()
        val lOperationEntity = getValidOperationEntity().also { it.type = OperationConstants.Type.deposit }
        lOperationAPI.create(lOperationEntity)
        lOperationEntity.type = OperationConstants.Type.withdrawal
        assertThrows<BusinessRuleException> {
            lOperationAPI.update(lOperationEntity)
        }
    }

}