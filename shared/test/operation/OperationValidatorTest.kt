package operation

import framework.exception.BusinessRuleException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class OperationValidatorTest {

    @Test
    fun `Should properly validate operation`() {
        var lOperationEntity = getValidOperationEntity().also { it.account = "" }
        assertThrows<BusinessRuleException> {
            OperationValidator.validate(lOperationEntity)
        }
        lOperationEntity = getValidOperationEntity().also { it.protocol = "" }
        assertThrows<BusinessRuleException> {
            OperationValidator.validate(lOperationEntity)
        }
        lOperationEntity = getValidOperationEntity().also { it.type = "" }
        assertThrows<BusinessRuleException> {
            OperationValidator.validate(lOperationEntity)
        }
        lOperationEntity = getValidOperationEntity().also { it.type = "definitely_invalid" }
        assertThrows<BusinessRuleException> {
            OperationValidator.validate(lOperationEntity)
        }
        lOperationEntity = getValidOperationEntity().also { it.value = 0.0 }
        assertThrows<BusinessRuleException> {
            OperationValidator.validate(lOperationEntity)
        }
        lOperationEntity = getValidOperationEntity().also { it.value = -1_000.99 }
        assertThrows<BusinessRuleException> {
            OperationValidator.validate(lOperationEntity)
        }
        lOperationEntity = getValidOperationEntity().also { it.status = "" }
        assertThrows<BusinessRuleException> {
            OperationValidator.validate(lOperationEntity)
        }
        lOperationEntity = getValidOperationEntity().also { it.status = "definitely_invalid" }
        assertThrows<BusinessRuleException> {
            OperationValidator.validate(lOperationEntity)
        }
        assertDoesNotThrow { OperationValidator.validate(getValidOperationEntity()) }
    }

}