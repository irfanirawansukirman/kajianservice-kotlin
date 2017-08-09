package util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.crypto.MacProvider
import spark.Response
import java.text.SimpleDateFormat
import java.util.*

fun Response.baseResponse(data: Any): String {
    return jacksonObjectMapper().writeValueAsString(data)
}

fun Boolean.getToken(token: String): Boolean {
    return false
}

fun String.generateToken(baseToken: String): String {
    val key = MacProvider.generateKey()
    val compactJws: String = Jwts.builder()
            .setSubject(baseToken)
            .signWith(SignatureAlgorithm.HS512, key)
            .compact()

    return compactJws
}

fun String.dateFormat(): String? {
    val timeMilis = System.currentTimeMillis()
    val date = Date(timeMilis)
    val formatters = SimpleDateFormat(DATE_DDMMYYY_HHMMSS)
    return formatters.format(date).toString()
}