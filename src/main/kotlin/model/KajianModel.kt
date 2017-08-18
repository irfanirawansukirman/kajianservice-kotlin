package model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "tb_kajian")
data class KajianModel(
        @DatabaseField(generatedId = true)
        var id_kajian: Int? = null,

        @DatabaseField(columnName = "nama")
        var nama: String = "",

        @DatabaseField(columnName = "deskripsi")
        var deskripsi: String = "",

        @DatabaseField(columnName = "pemateri")
        var pemateri: String = "",

        @DatabaseField(columnName = "tanggal_waktu")
        var tanggal_waktu: String = "",

        @DatabaseField(columnName = "alamat")
        var alamat: String = "",

        @DatabaseField(columnName = "foto")
        var foto: String = "",

        @DatabaseField(foreign = true, columnName = "id_user", foreignAutoRefresh = true)
        var user_data: UserModel = UserModel(),

        @DatabaseField(foreign = true, columnName = "id_masjid", foreignAutoRefresh = true)
        var masjid_data: MasjidModel = MasjidModel()
)