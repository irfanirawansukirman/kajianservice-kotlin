package controller

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import database.DbConnection
import model.*
import spark.Spark.*
import util.baseResponse
import util.dateFormat
import util.generateToken

class RouteController {
    fun runApp() {

        //get connection
        val dbConnection = DbConnection.getDatabaseConnection()

        //get controller
        val userDao = DaoManager.createDao(dbConnection, UserModel::class.java) as Dao<UserModel, kotlin.String>
        val kajianDao = DaoManager.createDao(dbConnection, KajianModel::class.java) as Dao<KajianModel, Int>
        val masjidDao = DaoManager.createDao(dbConnection, MasjidModel::class.java) as Dao<MasjidModel, Int>

        //route for api service
        path("/kajian") {
            get("/timeline/:page") { requestuest, response ->
                response.type("application/json")

                val page: Int = requestuest.params("page").toInt()
                val userQuery = userDao.queryBuilder()
                val masjidQuery = masjidDao.queryBuilder()
                val kajianQuery = kajianDao.queryBuilder()
                        .join(userQuery)
                        .join(masjidQuery)
                        .offset((requestuest.params("page").toLong() - 1) * 10)
                        .limit(10)
                        .prepare()

                val kajianList = kajianDao.query(kajianQuery)
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

                val baseModel = BaseModel(status_code = response.status(), message = message, page = page, total_page = countPage, data = kajianList)
                response.baseResponse(baseModel)
            }
        }

        path("/user") {
            post("/reg") { request, response ->
                response.header("Content-Type", "application/json")
                val nama = request.queryParams("name")
                val email = request.queryParams("email")
                val telepon = request.queryParams("telepon")
                val token = String().generateToken(telepon + "_" + email)
                val timestamp = String().dateFormat().toString()

                val userModel = UserModel(nama = nama, email = email, telepon = telepon, token = token, foto = "", timestamp = timestamp)
                val message:String?

                if(userModel == null){
                    message = "Failed"
                } else {
                    message = "Success"
                    userDao.create(userModel)
                }

                val baseModelWithoutPage = BaseModelWithoutPage(status_code = response.status(), message = message, data = userModel)
                response.baseResponse(baseModelWithoutPage)
            }
        }
    }
}

