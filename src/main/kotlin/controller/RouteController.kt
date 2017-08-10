package controller

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import database.DbConnection
import model.*
import spark.Spark.*
import util.*

class RouteController {
    fun runApp() {

        //get connection
        val dbConnection = DbConnection.getDatabaseConnection()

        //get controller
        val userDao = DaoManager.createDao(dbConnection, UserModel::class.java) as Dao<UserModel, String>
        val kajianDao = DaoManager.createDao(dbConnection, KajianModel::class.java) as Dao<KajianModel, Int>
        val masjidDao = DaoManager.createDao(dbConnection, MasjidModel::class.java) as Dao<MasjidModel, Int>

        //route for api service
        path("/kajian") {
            get("/timeline/:page") { request, response ->
                response.header("Content-Type", "application/json")
                val token = request.queryParams("token")
                val isTokenValid: Boolean? = false
                val page: Int = request.params("page").toInt()
                val userQuery = userDao.queryBuilder()
                val masjidQuery = masjidDao.queryBuilder()
                val kajianList = kajianDao.query(
                        kajianDao.queryBuilder()
                                .join(userQuery)
                                .join(masjidQuery)
                                .offset((request.params("page").toLong() - 1) * 10)
                                .limit(10)
                                .prepare())

                var totalPage: Int = Math.round(kajianDao.queryForAll().size.toDouble() / 10).toInt()
                var countPage: Int = 0

                if ((totalPage * 10) < kajianDao.queryForAll().size) {
                    countPage = totalPage + 1
                }

                val message: String?

                if (kajianList == null) {
                    message = "Failed"
                } else {
                    message = "Success"
                }

                val baseModel: BaseModel?

                if (isTokenValid?.getToken(token) == true) {
                    baseModel = BaseModel(status_code = response.status(), message = message, page = page, total_page = countPage, data = kajianList)
                } else {
                    baseModel = BaseModel(status_code = 404, message = message, page = 0, total_page = 0, data = "Data tidak ditemukan")
                }

                response.baseResponse(baseModel)
            }

            get("/timeline/detail/:id_kajian") { request, response -> }
        }

        path("/user") {
            post("/register") { request, response ->
                response.header("Content-Type", "application/json")
                val nama = request.body().jsonToMap().get("name")
                val email = request.body().jsonToMap().get("email")
                val password = String().setToMd5Format(request.body().jsonToMap().get("password").toString())
                val telepon = request.body().jsonToMap().get("telepon")
                val token = String().generateToken("$telepon" + "_" + "$email")
                val timestamp = String().dateFormat().toString()

                val userModel = UserModel(nama = nama.toString(), email = email.toString(), password = password, telepon = telepon.toString(), token = token, foto = "", timestamp = timestamp)
                val message: String?

                if (userModel == null) {
                    message = "Failed"
                } else {
                    message = "Success"
                    userDao.create(userModel)
                }

                val baseModelWithoutPage = BaseModelWithoutPage(status_code = response.status(), message = message, data = userModel)
                response.baseResponse(baseModelWithoutPage)
            }

            post("/login") { request, response ->
                response.header("Content-Type", "application/json")
                val token = request.body().jsonToMap().get("token")
                val booleanToken: Boolean = false
                val isTokenValid: Boolean = booleanToken.getToken(token.toString())

                val message: String?

                if (isTokenValid) {
                    message = "Success"

                    val userData = userDao.query(
                            userDao.queryBuilder()
                                    .where()
                                    .eq("token", token)
                                    .prepare())
                    val userDataResult = UserModel(id_user = userData.get(0).id_user, nama = userData.get(0).nama, email = userData.get(0).email, password = userData.get(0).password, telepon = userData.get(0).telepon, token = userData.get(0).token, foto = userData.get(0).foto, timestamp = userData.get(0).timestamp)
                    val baseModel = BaseModelWithoutPage(status_code = response.status(), message = message, data = userDataResult)

                    response.baseResponse(baseModel)
                } else {
                    message = "Failed"

                    val baseModel = BaseModelWithoutPage(status_code = response.status(), message = message, data = isTokenValid)

                    response.baseResponse(baseModel)
                }
            }

            //nanti ada dua metode kirim pass [sms dan email] => next phase
            post("/forgot") { request, response -> }

            post("/change_pass/:id_user") { request, response ->
                response.header("Content-Type", "application/json")
                val idUser = request.params("id_user")
                val newPass = String().setToMd5Format(request.body().jsonToMap().get("new_pass").toString())

                val updateBuilder = userDao.updateBuilder()
                updateBuilder.updateColumnValue("password", newPass)
                        .where()
                        .eq("id_user", idUser)
                updateBuilder.update()

                val userModelUpdate = userDao.query(
                        userDao.queryBuilder()
                                .where()
                                .eq("id_user", idUser)
                                .prepare())

                val userDataResultUpdate = UserModel(id_user = userModelUpdate.get(0).id_user, nama = userModelUpdate.get(0).nama, email = userModelUpdate.get(0).email, password = userModelUpdate.get(0).password, telepon = userModelUpdate.get(0).telepon, token = userModelUpdate.get(0).token, foto = userModelUpdate.get(0).foto, timestamp = userModelUpdate.get(0).timestamp)

                val message:String?

                if (userDataResultUpdate != null) {
                    message = "Success"
                    response.baseResponse(BaseModelWithoutPage(status_code = response.status(), message = message, data = userDataResultUpdate))
                } else {
                    message = "Failed"
                    response.baseResponse(BaseModelWithoutPage(status_code = 404, message = message, data = ""))
                }
            }
        }
    }
}
