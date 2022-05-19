package framework

import configuration.KafkaConfigurationDTO
import org.koin.dsl.module

object KafkaConfigurationDependencyModule {

    operator fun invoke(aKafkaConfigurationDTO: KafkaConfigurationDTO) : org.koin.core.module.Module {
        return module {
            single { aKafkaConfigurationDTO }
        }
    }

}