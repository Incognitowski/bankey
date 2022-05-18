package configuration

data class KafkaConfigurationDTO(
    val bootstrapServers: String = "",
    val jaasConfig: String = "",
)
