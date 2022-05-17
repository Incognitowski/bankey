package operation

import org.ktorm.database.Database
import org.ktorm.dsl.*

class OperationRepository(private val mDatabase: Database) {

    private fun fillFields(aAssignmentsBuilder: AssignmentsBuilder, aOperationTable: OperationTable, aOperationEntity: OperationEntity) {
        aAssignmentsBuilder.set(aOperationTable.account, aOperationEntity.account)
        aAssignmentsBuilder.set(aOperationTable.protocol, aOperationEntity.protocol)
        aAssignmentsBuilder.set(aOperationTable.type, aOperationEntity.type)
        aAssignmentsBuilder.set(aOperationTable.value, aOperationEntity.value)
        aAssignmentsBuilder.set(aOperationTable.status, aOperationEntity.status)
        aAssignmentsBuilder.set(aOperationTable.created_at, aOperationEntity.createdAt)
        aAssignmentsBuilder.set(aOperationTable.updated_at, aOperationEntity.updatedAt)
    }

    fun create(aOperationEntity: OperationEntity) {
        aOperationEntity.operationId = mDatabase.insertAndGenerateKey(OperationTable) {
            fillFields(this, it, aOperationEntity)
        } as Long
    }

    fun update(aOperationEntity: OperationEntity) {
        mDatabase.update(OperationTable) {
            fillFields(this, it, aOperationEntity)
            where { it.operationId eq aOperationEntity.operationId }
        }
    }

    fun findById(aOperationId: Long): OperationEntity? {
        return mDatabase
            .from(OperationTable)
            .select()
            .where { OperationTable.operationId eq aOperationId }
            .map { OperationTable.createEntity(it) }
            .firstOrNull()
    }

    fun findByProtocol(aOperationProtocol: String): OperationEntity? {
        return mDatabase
            .from(OperationTable)
            .select()
            .where { OperationTable.protocol eq aOperationProtocol }
            .map { OperationTable.createEntity(it) }
            .firstOrNull()
    }

    fun listByStatus(aStatus: String): List<OperationEntity> {
        return mDatabase
            .from(OperationTable)
            .select()
            .where {
                OperationTable.status eq aStatus
            }
            .map { OperationTable.createEntity(it) }
    }

    fun listByAccount(aAccountIdentifier: String): List<OperationEntity> {
        return mDatabase
            .from(OperationTable)
            .select()
            .where {
                OperationTable.account eq aAccountIdentifier
            }
            .map { OperationTable.createEntity(it) }
    }

}