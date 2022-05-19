package operationEvent

import framework.exception.BusinessRuleException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class OperationEventValidatorTest {

    @Test
    fun `Should properly validate operation event`() {
        var lOperationEventEntity = getValidOperationEventEntity().also { it.type = "" }
        assertThrows<BusinessRuleException> {
            OperationEventValidator.validate(lOperationEventEntity)
        }
        lOperationEventEntity = getValidOperationEventEntity().also { it.type = "definitely_not_a_valid_type" }
        assertThrows<BusinessRuleException> {
            OperationEventValidator.validate(lOperationEventEntity)
        }
        lOperationEventEntity = getValidOperationEventEntity().also { it.operationId = 0 }
        assertThrows<BusinessRuleException> {
            OperationEventValidator.validate(lOperationEventEntity)
        }
        assertDoesNotThrow {
            OperationEventValidator.validate(getValidOperationEventEntity())
        }
    }

}