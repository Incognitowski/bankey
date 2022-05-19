package framework.extensionFunctions

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun Any.toJSON() : String {
    val lObjectMapper = jacksonObjectMapper().also {
        it.registerModule(JavaTimeModule())
    }
    return lObjectMapper.writeValueAsString(this)
}