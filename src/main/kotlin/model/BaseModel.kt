package model

data class BaseModel(val status_code: Int, val message: String, val page: Int, val total_page: Int, var data: Any)