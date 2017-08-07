package database

import com.j256.ormlite.db.MysqlDatabaseType
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import util.*
import java.sql.SQLException

object DbConnection {

    @Throws(SQLException::class, ClassNotFoundException::class)
    fun getDatabaseConnection(): ConnectionSource {
        val dbUrl = DB_URL + DB_HOST + DB_NAME
        val databaseType = MysqlDatabaseType()
        val connectionSource = JdbcConnectionSource(dbUrl, databaseType)
        connectionSource.setUsername(DB_USER)
        connectionSource.setPassword(DB_PASSWORD)
        return connectionSource
    }
}