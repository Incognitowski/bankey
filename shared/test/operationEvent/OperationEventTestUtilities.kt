package operationEvent

import kotlin.random.Random

fun getValidOperationEventEntity() : OperationEventEntity = OperationEventEntity(
    operationId = Random.nextLong(),
    type = OperationEventConstants.Types().random(),
)