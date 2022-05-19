package operationEvent

object OperationEventConstants {

    object Types {
        const val created = "created"
        const val startedProcessing = "startedProcessing"
        const val succeeded = "succeeded"
        const val failed = "failed"

        operator fun invoke(): List<String> {
            return listOf(
                created,
                startedProcessing,
                succeeded,
                failed
            )
        }
    }

}