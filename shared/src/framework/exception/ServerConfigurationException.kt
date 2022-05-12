package framework.exception

class ServerConfigurationException : OwnedException {

    constructor(aMessage: String) : super(aMessage)

    constructor(aMessage: String, aException: Exception) : super(aMessage, aException)

}