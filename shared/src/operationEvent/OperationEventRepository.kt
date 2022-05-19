package operationEvent

import org.ktorm.database.Database
import org.ktorm.dsl.*

class OperationEventRepository(private val mDatabase: Database) {

    fun create(aOperationEventEntity: OperationEventEntity) {
        aOperationEventEntity.operationEventId = mDatabase.insertAndGenerateKey(OperationEventTable) {
            set(it.operationId, aOperationEventEntity.operationId)
            set(it.type, aOperationEventEntity.type)
            set(it.payload, aOperationEventEntity.payload)
            set(it.createdAt, aOperationEventEntity.createdAt)
        } as Long
    }

    fun listByOperationId(aOperationId: Long): List<OperationEventEntity> {
        return mDatabase
            .from(OperationEventTable)
            .select()
            .where { OperationEventTable.operationId eq aOperationId }
            .map { OperationEventTable.createEntity(it) }
    }

}