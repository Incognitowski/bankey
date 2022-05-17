package operation

import framework.exception.BusinessRuleException
import framework.extensionFunctions.isNotIn

object OperationValidator {

    fun validate(aOperationEntity: OperationEntity) {
        if (aOperationEntity.account.isEmpty())
            throw BusinessRuleException("Operation account identifier cannot be empty.")
        if (aOperationEntity.protocol.isEmpty())
            throw BusinessRuleException("Operation protocol cannot be empty.")
        if (aOperationEntity.type.isEmpty())
            throw BusinessRuleException("Operation type cannot be empty.")
        if (aOperationEntity.type.isNotIn(OperationConstants.Type()))
            throw BusinessRuleException("Operation type '${aOperationEntity.type}' is invalid. Use one of ${OperationConstants.Type()}.")
        if (aOperationEntity.value <= 0.0)
            throw BusinessRuleException("Operation value must be greater than zero..")
        if (aOperationEntity.status.isEmpty())
            throw BusinessRuleException("Operation status cannot be empty.")
        if (aOperationEntity.status.isNotIn(OperationConstants.Status()))
            throw BusinessRuleException("Operation status '${aOperationEntity.status}' is invalid. Use one of ${OperationConstants.Status()}.")

    }

}