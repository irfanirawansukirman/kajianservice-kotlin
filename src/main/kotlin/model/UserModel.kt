package model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable


@DatabaseTable(tableName = "tb_user")
data class UserModel(
        @DatabaseField(generatedId = true)
        var id_user: Int? = null,

        @DatabaseField(columnName = "nama")
        var nama: String = "",

        @DatabaseField(columnName = "email")
        var email: String = "",

        @DatabaseField(columnName = "password")
        var password: String = "",

        @DatabaseField(columnName = "telepon")
        var telepon: String = "",

        @DatabaseField(columnName = "token")
        var token: String = "",

        @DatabaseField(columnName = "foto")
        var foto: String = "",

        @DatabaseField(columnName = "timestamp")
        var timestamp: String = ""
)
