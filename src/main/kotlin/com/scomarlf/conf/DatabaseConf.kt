package com.scomarlf.conf

import org.apache.commons.io.FileUtils
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class DatabaseConf {
    companion object {

        var URL: String = ""
        var USERNAME: String = ""
        var PASSWORD: String = ""

        /**
         * read config from conf.json
         */
        fun init(): Boolean {
            return try {
                val file = File("conf.json")
                val content = FileUtils.readFileToString(file, "UTF-8")
                val jsonObject = JSONObject(content)
                // for database
                val databaseConfObj = jsonObject.get("database") as JSONObject
                URL = databaseConfObj.get("url") as String
                USERNAME = databaseConfObj.get("username") as String
                PASSWORD = databaseConfObj.get("password") as String
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}