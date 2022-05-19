package operationEvent

import org.koin.core.module.Module
import org.koin.dsl.module

object OperationEventDependencyModule {
    operator fun invoke(): Module {
        return module {
            single { OperationEventRepository(get()) }
            single { OperationEventAPI(get()) }
        }
    }
}