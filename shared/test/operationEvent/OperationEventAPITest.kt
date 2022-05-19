package operationEvent

import BaseTest
import framework.exception.EntityNotFoundException
import framework.injectFromKoin
import operation.OperationAPI
import operation.OperationEntity
import operation.getValidOperationEntity
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import parameter.ParameterAPI
import parameter.ParameterEntity
import kotlin.test.assertEquals

class OperationEventAPITest : BaseTest() {

    private fun createOperation(): OperationEntity {
        injectFromKoin<ParameterAPI>().create(ParameterEntity())
        return getValidOperationEntity().also { injectFromKoin<OperationAPI>().create(it) }
    }

    @Test
    fun `Should properly insert operation event`() {
        val lOperationEventAPI: OperationEventAPI = injectFromKoin()
        val lOperationEntity: OperationEntity = createOperation()
        val lOperationEventEntity = getValidOperationEventEntity().also {
            it.operationId = lOperationEntity.operationId
            it.type = OperationEventConstants.Types.startedProcessing
        }
        assertDoesNotThrow {
            lOperationEventAPI.create(lOperationEventEntity)
        }
    }

    @Test
    fun `Should raise exception when trying to create an event for an operation that does not exist`() {
        val lOperationEventAPI: OperationEventAPI = injectFromKoin()
        val lOperationEventEntity = getValidOperationEventEntity().also { it.operationId = 999_999 }
        assertThrows<EntityNotFoundException> {
            lOperationEventAPI.create(lOperationEventEntity)
        }
    }

    @Test
    fun `Should properly query events by operation ID`() {
        val lOperationEventAPI: OperationEventAPI = injectFromKoin()
        val lOperationEntity: OperationEntity = createOperation()
        getValidOperationEventEntity().also {
            it.operationId = lOperationEntity.operationId
            it.type = OperationEventConstants.Types.startedProcessing
            lOperationEventAPI.create(it)
        }
        getValidOperationEventEntity().also {
            it.operationId = lOperationEntity.operationId
            it.type = OperationEventConstants.Types.succeeded
            lOperationEventAPI.create(it)
        }
        val lListOfOperationEventEntity = lOperationEventAPI.listByOperationId(lOperationEntity.operationId)
        assertEquals(3, lListOfOperationEventEntity.size)
    }

}