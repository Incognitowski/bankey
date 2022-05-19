package operationEvent

import framework.exception.EntityNotFoundException
import framework.injectFromKoin
import operation.OperationAPI
import java.time.Instant

class OperationEventAPI(private val mOperationEventRepository: OperationEventRepository) {

    private fun validateIfOperationExists(aOperationId: Long) {
        injectFromKoin<OperationAPI>().findById(aOperationId) ?: throw EntityNotFoundException("Operation not found with ID $aOperationId")
    }

    fun create(aOperationEventEntity: OperationEventEntity) {
        validateIfOperationExists(aOperationEventEntity.operationId)
        aOperationEventEntity.createdAt = Instant.now()
        OperationEventValidator.validate(aOperationEventEntity)
        mOperationEventRepository.create(aOperationEventEntity)
    }

    fun listByOperationId(aOperationId: Long): List<OperationEventEntity> = mOperationEventRepository.listByOperationId(aOperationId)

}