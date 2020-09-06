package com.scomarlf.bot.utils

import org.telegram.telegrambots.meta.api.objects.Update

class BtnCallBack private constructor(
    val chatId : Long,
    val messageId : Int,
    // 可操作用户Id
    val userId: Int?,
    // 操作
    val oper: String,
    // 类型
    val type: String,
    // 数据
    val content: String
) {

    companion object {
        fun Init(update: Update): BtnCallBack? {
            val chatId = update.callbackQuery.message.chat.id
            val messageId = update.callbackQuery.message.messageId
            // 获取callback操作数据
            val callBackData = update.callbackQuery.data
            val oper = callBackData.substring(0, callBackData.indexOf(':'))
            val callBackDataSplited = callBackData.replace("$oper:", "").split("&").toTypedArray()
            val type = callBackDataSplited[0]
            val content = callBackDataSplited[1]
            val userId = if (callBackDataSplited.size > 2) Integer.valueOf(callBackDataSplited[2]) else null
            // 判断是否有权操作
            return if (userId == null)
                BtnCallBack(chatId,messageId,userId, oper, type, content)
            else if (update.callbackQuery.from.id == userId)
                BtnCallBack(chatId,messageId,userId, oper, type, content)
            else
                null
        }
    }

}