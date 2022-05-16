package framework.exception

class EntityNotFoundException : OwnedException {

    constructor(aMessage: String) : super(aMessage)

    constructor(aMessage: String, aException: Exception) : super(aMessage, aException)

}