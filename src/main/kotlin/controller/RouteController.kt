package controller

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import database.DbConnection
import model.BaseModel
import model.KajianModel
import model.MasjidModel
import model.UserModel
import spark.Spark.*
import util.baseResponse

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
            get("/timeline/:page") { request, response ->
                val page: Int = request.params("page").toInt()
                val userQuery = userDao.queryBuilder()
                val masjidQuery = masjidDao.queryBuilder()
                val kajianQuery = kajianDao.queryBuilder()
                        .join(userQuery)
                        .join(masjidQuery)
                        .offset((request.params("page").toLong() - 1) * 10)
                        .limit(10)
                        .prepare()

                val kajianList = kajianDao.query(kajianQuery)
                var totalPage:Int = Math.round(kajianDao.queryForAll().size.toDouble() / 10).toInt()
                var countPage: Int = 0

                if ((totalPage * 10) < kajianDao.queryForAll().size){
                    countPage = totalPage + 1
                }

                val message : String?

                if (kajianList == null){
                    message = "Failed"
                } else {
                    message = "Success"
                }

                val baseModel = BaseModel(status_code = response.status(), message = message, page = page, total_page = countPage, data = kajianList)
                response.baseResponse(baseModel)
            }

            post("/user/register"){ request, response ->

            }
        }
    }

}