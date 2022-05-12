package parameters

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.long

object ParameterTable : BaseTable<ParameterEntity>("parameter") {

    var parameterId = long("parameter_id").primaryKey()
    var processOperations = boolean("process_operations")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): ParameterEntity {
        return ParameterEntity(
            parameterId = row[parameterId] ?: 0,
            processOperations = row[processOperations] ?: false,
        )
    }
}