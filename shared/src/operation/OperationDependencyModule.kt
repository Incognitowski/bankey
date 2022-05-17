package operation

import org.koin.core.module.Module
import org.koin.dsl.module

object OperationDependencyModule {
    operator fun invoke(): Module {
        return module {
            single { OperationRepository(get()) }
            single { OperationAPI(get()) }
        }
    }
}