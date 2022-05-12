package framework.exception

class DatabaseConfigurationException : OwnedException {

    constructor(aMessage: String) : super(aMessage)

    constructor(aMessage: String, aException: Exception) : super(aMessage, aException)

}