package operation

import framework.exception.BusinessRuleException
import framework.exception.EntityNotFoundException
import framework.extensionFunctions.isIn
import framework.extensionFunctions.isNotIn
import framework.injectFromKoin
import operationEvent.OperationEventAPI
import operationEvent.OperationEventConstants
import operationEvent.OperationEventEntity
import parameter.ParameterAPI
import java.time.Instant
import java.util.*
import kotlin.NoSuchElementException

class OperationAPI(private val mOperationRepository: OperationRepository) {

    private val mParameterAPI: ParameterAPI = injectFromKoin()
    private val mOperationEventAPI: OperationEventAPI = injectFromKoin()

    fun findById(aOperationId: Long): OperationEntity? = mOperationRepository.findById(aOperationId)

    private fun validateIfShouldProcessOperations() {
        val lParameterEntity = try {
            mParameterAPI.findLatest()
        } catch (e: NoSuchElementException) {
            throw BusinessRuleException("No application parameters were found.")
        }
        if (!lParameterEntity.processOperations)
            throw BusinessRuleException("Operations were temporarily disabled.")
    }

    fun create(aOperationEntity: OperationEntity) {
        validateIfShouldProcessOperations()
        aOperationEntity.protocol = UUID.randomUUID().toString()
        aOperationEntity.createdAt = Instant.now()
        aOperationEntity.updatedAt = null
        aOperationEntity.status = OperationConstants.Status.awaiting
        OperationValidator.validate(aOperationEntity)
        mOperationRepository.create(aOperationEntity)
        mOperationEventAPI.create(
            OperationEventEntity(
                operationId = aOperationEntity.operationId,
                type = OperationEventConstants.Types.created,
            )
        )
    }

    fun update(aOperationEntity: OperationEntity) {
        validateIfShouldProcessOperations()
        aOperationEntity.updatedAt = Instant.now()
        OperationValidator.validate(aOperationEntity)
        validateUpdate(aOperationEntity)
        mOperationRepository.update(aOperationEntity)
    }

    private fun validateUpdate(aOperationEntity: OperationEntity) {
        val lOperationEntity = findById(aOperationEntity.operationId)
            ?: throw EntityNotFoundException("Operation not found with ID ${aOperationEntity.operationId}")
        val lCurrentStatus = lOperationEntity.status
        val lNewStatus = aOperationEntity.status
        val lFinalStates = listOf(OperationConstants.Status.failure, OperationConstants.Status.success)
        if (lOperationEntity.type != aOperationEntity.type)
            throw BusinessRuleException("Operation types cannot be changed.")
        if (lCurrentStatus.isIn(lFinalStates))
            throw BusinessRuleException("Operation cannot be updated because it has already reached a conclusive status.")
        if (lCurrentStatus == OperationConstants.Status.awaiting && lNewStatus != OperationConstants.Status.processing)
            throw BusinessRuleException("Operations awaiting execution can only be set to the processing status.")
        if (lCurrentStatus == OperationConstants.Status.processing && lNewStatus.isNotIn(lFinalStates))
            throw BusinessRuleException("Operations in process cannot be rolled back to the awaiting status.")
    }

    fun findByProtocol(aOperationProtocol: String): OperationEntity? = mOperationRepository.findByProtocol(aOperationProtocol)

    fun listByStatus(aStatus: String): List<OperationEntity> {
        if (aStatus.isNotIn(OperationConstants.Status()))
            throw BusinessRuleException("Status '$aStatus' is not valid. Use one of: ${OperationConstants.Status()}")
        return mOperationRepository.listByStatus(aStatus)
    }

    fun listByAccount(aAccountIdentifier: String): List<OperationEntity> = mOperationRepository.listByAccount(aAccountIdentifier)

}