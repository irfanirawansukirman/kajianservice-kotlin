package model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "tb_masjid")
data class MasjidModel(
        @DatabaseField(generatedId = true)
        var id_masjid: Int? = null,

        @DatabaseField(columnName = "nama")
        var nama: String = "",

        @DatabaseField(columnName = "alamat")
        var alamat: String = "",

        @DatabaseField(columnName = "longitude")
        var longitude: String = "",

        @DatabaseField(columnName = "latitude")
        var latitude: String = "",

        @DatabaseField(columnName = "foto")
        var foto: String = ""
)