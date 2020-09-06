package com.scomarlf.bot.utils

import com.scomarlf.conf.BotConf
import org.telegram.telegrambots.meta.api.objects.Update

class PrivateUpdateAttributes {

    private var isPrivateMsg = false
    private var isGroupMsg = false
    private var isChannelMsg = false
    private var hasTextMsg = false
    private var isBtnClicked = false
    private var isUseCommand = false
    private var isFromManageGroup = false
    private var content: String? = null

    companion object {

        fun Init(update: Update): PrivateUpdateAttributes? {
            // 实例化
            val updateAttributes = PrivateUpdateAttributes()
            // 消息内容
            val message = update.message
            // 是否为点击的消息行内按钮
            updateAttributes.isBtnClicked = update.callbackQuery != null
            // 是否文字消息
            updateAttributes.hasTextMsg = update.hasMessage() && update.message.hasText()
            // 文字消息内容
            updateAttributes.content = if (update.hasMessage()) update.message.text else null
            // 是否为私聊回复的文字消息
            updateAttributes.isPrivateMsg = update.hasMessage() && update.message.isUserMessage
            // 是否是群组的消息
            updateAttributes.isGroupMsg =
                update.hasMessage() && (update.message.isGroupMessage || update.message.isSuperGroupMessage)
            // 是否是频道的消息
            if (update.channelPost != null) {
                updateAttributes.isChannelMsg = true
                updateAttributes.hasTextMsg = update.channelPost.hasText()
                updateAttributes.content = update.channelPost.text
            }
            // 是否使用命令
            updateAttributes.isUseCommand = updateAttributes.hasTextMsg && updateAttributes.isCommand(updateAttributes.content!!)
            // 是否来自于审核群组
            updateAttributes.isFromManageGroup = updateAttributes.isGroupMsg && message.chatId == BotConf.APPROVE_ID
            // 返回实例
            return updateAttributes
        }

    }

    /**
     * 判断是否为命令信息
     *
     * @param text 用户发送的信息
     * @return
     */
    private fun isCommand(text: String): Boolean {
        return text.startsWith("/")
    }

    fun isPrivateMsg(): Boolean {
        return isPrivateMsg
    }

    fun isGroupMsg(): Boolean {
        return isGroupMsg
    }

    fun isChannelMsg(): Boolean {
        return isChannelMsg
    }

    fun isHasTextMsg(): Boolean {
        return hasTextMsg
    }

    fun isBtnClicked(): Boolean {
        return isBtnClicked
    }

    fun isUseCommand(): Boolean {
        return isUseCommand
    }

    fun isFromManageGroup(): Boolean {
        return isFromManageGroup
    }
}