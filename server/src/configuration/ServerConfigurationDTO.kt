package configuration

data class ServerConfigurationDTO(
    val port: Int = 7070,
    val database: DatabaseConfigurationDTO = DatabaseConfigurationDTO(),
    val kafka: KafkaConfigurationDTO = KafkaConfigurationDTO(),
)