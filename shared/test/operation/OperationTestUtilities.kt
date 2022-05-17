package operation

import java.util.UUID
import kotlin.random.Random

fun getValidOperationEntity(): OperationEntity = OperationEntity(
    account = UUID.randomUUID().toString().take(6),
    type = OperationConstants.Type().random(),
    value = Random.nextDouble(5.00, 50.00),
    status = OperationConstants.Status.awaiting,
    protocol = UUID.randomUUID().toString(),
)