package operationEvent

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.long
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import java.time.Instant

object OperationEventTable : BaseTable<OperationEventEntity>("operation_event") {

    var operationEventId = long("operation_event_id").primaryKey()
    var operationId = long("operation_id")
    var type = varchar("type")
    var payload = varchar("payload")
    var createdAt = timestamp("created_at")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): OperationEventEntity {
        return OperationEventEntity(
            operationEventId = row[operationEventId] ?: 0,
            operationId = row[operationId] ?: 0,
            type = row[type] ?: "",
            payload = row[payload] ?: "",
            createdAt = row[createdAt] ?: Instant.now(),
        )
    }

}