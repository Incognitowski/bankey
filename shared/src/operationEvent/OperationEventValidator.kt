package operationEvent

import framework.exception.BusinessRuleException
import framework.extensionFunctions.isNotIn

object OperationEventValidator {

    fun validate(aOperationEventEntity: OperationEventEntity) {
        if (aOperationEventEntity.operationId == 0L)
            throw BusinessRuleException("You must inform the operation ID referenced by this event.")
        if (aOperationEventEntity.type.isEmpty())
            throw BusinessRuleException("You must inform an event type.")
        if (aOperationEventEntity.type.isNotIn(OperationEventConstants.Types()))
            throw BusinessRuleException("Invalid event type '${aOperationEventEntity.type}'. Use one of: ${OperationEventConstants.Types()}")
    }

}