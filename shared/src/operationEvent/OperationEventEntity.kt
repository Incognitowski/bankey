package operationEvent

import java.time.Instant

data class OperationEventEntity(
    var operationEventId: Long = 0,
    var operationId: Long = 0,
    var type: String = "",
    var payload: String = "",
    var createdAt: Instant = Instant.now(),
)