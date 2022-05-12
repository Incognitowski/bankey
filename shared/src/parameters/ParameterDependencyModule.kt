package parameters

import org.koin.core.module.Module
import org.koin.dsl.module

fun parameterDependencyModule(): Module {
    return module {
        single { ParameterRepository(get()) }
        single { ParameterAPI(get()) }
    }
}