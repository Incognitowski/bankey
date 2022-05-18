package framework.extensionFunctions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

inline fun <reified T> String.parseFromJson() : T {
    return jacksonObjectMapper().readValue(this, T::class.java)
}