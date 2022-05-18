package framework.extensionFunctions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun Any.toJSON() : String {
    return jacksonObjectMapper().writeValueAsString(this)
}