package util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Response

fun Response.baseResponse(data: Any): String {
    return jacksonObjectMapper().writeValueAsString(data)
}