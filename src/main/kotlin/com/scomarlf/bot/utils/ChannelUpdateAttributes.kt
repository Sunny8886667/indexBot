package com.scomarlf.bot.utils

import com.scomarlf.conf.BotConf
import org.telegram.telegrambots.meta.api.objects.Update
import java.lang.Exception

class ChannelUpdateAttributes {

    private var chatId = 0L
    private var isCommand = false
    private var command: String? = null

    companion object {

        fun Init(update: Update): ChannelUpdateAttributes? {
            try {
                // 实例化
                val updateAttributes = ChannelUpdateAttributes()
                updateAttributes.chatId = update.channelPost.chatId
                // 是否使用命令
                updateAttributes.isCommand =
                    update.channelPost.hasText() && updateAttributes.isCommand(update.channelPost.text)
                // command
                updateAttributes.command =
                    if (updateAttributes.isCommand) update.channelPost.text.replace("@${BotConf.USERNAME}", "")
                    else null
                // 返回实例
                return updateAttributes
            } catch (e: Exception) {
                return null
            }
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

    fun getChatId(): Long {
        return chatId
    }

    fun isCommand(): Boolean {
        return isCommand
    }

    fun getCommand(): String? {
        return command
    }

}