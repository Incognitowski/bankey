package parameter

import org.koin.core.module.Module
import org.koin.dsl.module

object ParameterDependencyModule {
    operator fun invoke() : Module {
        return module {
            single { ParameterRepository(get()) }
            single { ParameterAPI(get()) }
        }
    }
}