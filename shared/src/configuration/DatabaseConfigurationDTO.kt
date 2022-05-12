package configuration

data class DatabaseConfigurationDTO(
    val provider: String = "",
    val host: String = "",
    val port: Int = 0,
    val name: String = "",
    val user: String = "",
    val password: String = "",
)