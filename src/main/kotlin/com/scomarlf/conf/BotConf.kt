package com.scomarlf.conf

import org.apache.commons.io.FileUtils
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class BotConf {
    companion object {
        // bot detail
        var TOKEN: String = ""
        var USERNAME: String = ""
        var ID: Int = 0

        // admin detail
        var CREATER: Int = 0

        // bulletin channel
        var BULLETIN_USERNAME: String? = null
        var BULLETIN_ID: Long? = null

        // approve group
        var APPROVE_USERNAME: String? = null
        var APPROVE_ID: Long? = null

        /**
         * read config from conf.json
         */
        fun init(): Boolean {
            return try {
                val file = File("conf.json")
                val content = FileUtils.readFileToString(file, "UTF-8")
                val jsonObject = JSONObject(content)
                // for bot
                val botConfObj = jsonObject.get("bot") as JSONObject
                TOKEN = botConfObj.get("bot_token") as String
                USERNAME = botConfObj.get("bot_username") as String
                ID = TOKEN.split(':')[0].toInt()
                CREATER = botConfObj.get("creater") as Int
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}