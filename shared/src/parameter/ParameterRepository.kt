package parameter

import org.ktorm.database.Database
import org.ktorm.dsl.*

class ParameterRepository(private val mDatabase: Database) {

    fun findLatest(): ParameterEntity? {
        return mDatabase
            .from(ParameterTable)
            .select()
            .map { ParameterTable.createEntity(it) }
            .lastOrNull()
    }

    fun findById(aParameterId: Long): ParameterEntity? {
        return mDatabase
            .from(ParameterTable)
            .select()
            .where {
                (ParameterTable.parameterId eq aParameterId)
            }
            .map { ParameterTable.createEntity(it) }
            .firstOrNull()
    }

    fun create(aParameterEntity: ParameterEntity) {
        aParameterEntity.parameterId = mDatabase.insertAndGenerateKey(ParameterTable) {
            set(it.processOperations, aParameterEntity.processOperations)
        } as Long
    }

    fun update(aParameterEntity: ParameterEntity) {
        mDatabase.update(ParameterTable) {
            set(it.processOperations, aParameterEntity.processOperations)
            where { it.parameterId eq aParameterEntity.parameterId }
        }
    }

}