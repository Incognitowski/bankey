package framework.exception

open class OwnedException : Exception {

    constructor(aMessage: String) : super(aMessage)

    constructor(aMessage: String, aException: Exception) : super(aMessage, aException)

}