package framework

import org.ktorm.database.Database
import org.ktorm.database.Transaction
import org.ktorm.database.TransactionIsolation

fun runInTransaction(isolation: TransactionIsolation? = null, handler: (Transaction) -> Unit) {
    injectFromKoin<Database>().useTransaction(isolation) {
        handler(it)
    }
}