package operation

import java.time.Instant

data class OperationEntity(
    var operationId: Long = 0,
    var account: String = "",
    var protocol: String = "",
    var type: String = "",
    var value: Double = 0.0,
    var status: String = "",
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant? = null,
)
