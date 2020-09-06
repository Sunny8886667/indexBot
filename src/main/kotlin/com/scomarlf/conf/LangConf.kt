package com.scomarlf.conf

import org.apache.commons.io.FileUtils
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class LangConf {

    companion object {

        private var langJson: JSONObject? = null

        /**
         * read config from lang.json
         */
        fun init(): Boolean {
            return try {
                val file = File("lang.json")
                val content = FileUtils.readFileToString(file, "UTF-8")
                langJson = JSONObject(content).get("zh-CN") as JSONObject?
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun get(str: String): String {
            return try {
                langJson?.get(str).toString()
                    .replace("zh_secretary_bot ","${BotConf.USERNAME} ")
                    .replace("zh_secretary ","${BotConf.BULLETIN_USERNAME} ")
                    .replace("zh_secretary/","${BotConf.BULLETIN_USERNAME}/")
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

    }

}

class LangItems{
    companion object{
        val NEED_SET_APPROVE : String = "needSetApprove"
        val NEED_SET_BULLETIN : String = "needSetBulletin"
        val NO_AUTHORITY : String = "noAuthority"
        val SET_SUCCESS : String = "setSuccess"
        val RESET_SUCCESS : String = "resetSuccess"
        val SET_APPROVE_ERROR : String = "setApproveError"
        val SET_BULLETIN_ERROR : String = "setBulletinError"

        val START : String = "start"
        val LIST : String = "list"
        val ENROLL : String = "enroll"
        val ROLE : String = "role"
        val GROUP_CANNOT_USE = "groupCannotUse"
        val JOIN_GROUP = "joinGroup"
        val JUST_CREATOR = "justCreator"
        val CANNOT_UNDERSTAND = "cannotUnderstand"
        val HELP : String = "help"
        val NOTHING : String = "nothing"
        val NEED_IN_GROUP = "needInGroup"
        val NEED_IN_CHANNEL= "needInChannel"
        val ENROLLED= "enrolled"
        val ENROLL_SUCCESS= "enrollSuccess"
        val ENROLL_ERROR= "enrollError"
        val SEARCH_FAIL = "searchFail"
        val TOO_LONG = "tooLong"
        val WAIT_APPROVE= "waitApprove"
        val APPROVE_COMMAND = "approveCommand"
        val NO_WAIT_APPROVE = "noWaitApprove"

        val DAILY_USER_ADD  = "dailyUserAdd"
        val DAILY_USER_ACTIVE = "dailyUserActive"
        val USER_COUNT  = "userCount"
        val RECORD_COUNT = "recordCount"

        val EDIT_TITLE_REPLY = "editTitleReply"
        val EDIT_REMARK_REPLY = "editRemarkReply"
        val EDIT_TAG_REPLY = "editTagReply"
        val SUBMIT_REPLY = "submitReply"
        val RESET_STATE_REPLY = "resetStateReply"
        val CANNOT_RESET_STATE_REPLY = "cannotResetStateReply"
        val EDIT_SUCCESS = "editSuccess"
        val EDIT_TITLE = "editTitle"
        val EDIT_REMARK = "editRemark"
        val EDIT_TAG = "editTag"
        val EDIT_CLASS = "editClass"
        val SUBMIT_ENROLL = "submitEnroll"

        val SEARCH = "search"
        val NEXT_PAGE = "nextPage"
        val PREVIOUS_PAGE = "previousPage"
        val PASS = "pass"
        val REFUSE = "refuse"
        val CANCEL = "cancel"
    }
}