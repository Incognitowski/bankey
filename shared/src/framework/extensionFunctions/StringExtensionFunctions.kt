package framework.extensionFunctions

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

inline fun <reified T> String.parseFromJson() : T {
    val lObjectMapper = jacksonObjectMapper().also {
        it.registerModule(JavaTimeModule())
    }
    return lObjectMapper.readValue(this, T::class.java)
}