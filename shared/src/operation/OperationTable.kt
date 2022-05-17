package operation

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.*
import java.time.Instant

object OperationTable : BaseTable<OperationEntity>("operation") {

    var operationId = long("operation_id").primaryKey()
    var account = varchar("account")
    var protocol = varchar("protocol")
    var type = varchar("type")
    var value = double("value")
    var status = varchar("status")
    var created_at = timestamp("created_at")
    var updated_at = timestamp("updated_at")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): OperationEntity {
        return OperationEntity(
            operationId = row[operationId] ?: 0,
            account = row[account] ?: "",
            protocol = row[protocol] ?: "",
            type = row[type] ?: "",
            value = row[value] ?: 0.0,
            status = row[status] ?: "",
            createdAt = row[created_at] ?: Instant.now(),
            updatedAt = row[updated_at],
        )
    }

}