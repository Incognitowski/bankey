package operation

object OperationConstants {

    object Type {
        const val deposit = "deposit"
        const val withdrawal = "withdrawal"

        operator fun invoke(): List<String> {
            return listOf(deposit, withdrawal)
        }
    }

    object Status {
        const val awaiting = "awaiting"
        const val processing = "processing"
        const val success = "success"
        const val failure = "failure"

        operator fun invoke(): List<String> {
            return listOf(awaiting, processing, success, failure)
        }
    }

}