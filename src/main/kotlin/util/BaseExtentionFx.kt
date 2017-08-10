package util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import database.DbConnection
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.crypto.MacProvider
import model.UserModel
import spark.Response
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

fun Response.baseResponse(data: Any): String {
    return jacksonObjectMapper().writeValueAsString(data)
}

fun getToken(token: String): Boolean {
    //get connection
    val dbConnection = DbConnection.getDatabaseConnection()

    //get controller
    val userDao = DaoManager.createDao(dbConnection, UserModel::class.java) as Dao<UserModel, String>
    val queryBuilder = userDao.queryBuilder()
            .where()
            .eq("token", token)
            .prepare()

    try {
        return userDao.query(queryBuilder).get(0).token.equals(token)
    } catch (e: Exception) {
        return false
    }
}

fun String.setToMd5Format(string: String): String {
    val messageDigest = MessageDigest.getInstance("MD5")
    messageDigest.update(StandardCharsets.UTF_8.encode(string))
    return String.format("%032x", BigInteger(1, messageDigest.digest()))
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