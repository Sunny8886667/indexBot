package com.scomarlf.bot.utils

import com.scomarlf.conf.BotConf
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.objects.Update
import java.lang.Exception

class GroupUpdateAttributes {

    private var chatId = 0L
    private var userId = 0
    private var isLeftBot = false
    private var isJoinBot = false
    private var botHasAdmin = false
    private var botHasAuthority = false
    private var isAdmin = false
    private var isCommand = false
    private var command: String? = null

    companion object {

        fun Init(update: Update, sender: MessageSender): GroupUpdateAttributes? {
            try {
                // 实例化
                val updateAttributes = GroupUpdateAttributes()

                updateAttributes.chatId = update.message.chatId
                updateAttributes.userId = update.message.from.id
                // bot加入或被移出
                val newMembers = update.message.newChatMembers
                val leftUser = update.message.leftChatMember
                updateAttributes.isLeftBot = leftUser != null && leftUser.userName == BotConf.USERNAME
                for (newMember in newMembers)
                    if (newMember.userName != null && newMember.userName == BotConf.USERNAME)
                        updateAttributes.isJoinBot = true
                // 是否是创建者请求
                val getChatMember4user = GetChatMember()
                getChatMember4user.setChatId(update.message.chatId)
                getChatMember4user.userId = update.message.from.id
                val memberUser = sender.execute(getChatMember4user)
                updateAttributes.isAdmin = memberUser.status == "creator" || memberUser.status == "administrator"
                // 检查bot权限
                val getChatMember4bot = GetChatMember()
                getChatMember4bot.setChatId(update.message.chatId)
                getChatMember4bot.userId = BotConf.ID
                val memberBot = sender.execute(getChatMember4bot)
                updateAttributes.botHasAdmin = memberBot.status == "administrator"
                val canDeleteMsg = if (updateAttributes.botHasAdmin) memberBot.canDeleteMessages else false
                val canInviteUsers = if (updateAttributes.botHasAdmin) memberBot.canInviteUsers else false
                updateAttributes.botHasAuthority = canDeleteMsg && canInviteUsers
                // 是否使用命令
                updateAttributes.isCommand =
                    update.hasMessage() && update.message.hasText() && update.message.text
                        .startsWith("/") && update.message.text.endsWith("@" + BotConf.USERNAME)
                updateAttributes.command =
                    if (updateAttributes.isCommand) update.message.text.replace("@" + BotConf.USERNAME, "")
                    else null

                // 返回实例
                return updateAttributes
            } catch (e: Exception) {
                return null
            }
        }

    }

    fun getChatId(): Long {
        return chatId
    }

    fun getUserId ():Int{
        return userId
    }

    fun isLeftBot(): Boolean {
        return isLeftBot
    }

    fun isJoinBot(): Boolean {
        return isJoinBot
    }

    fun botHasAdmin(): Boolean {
        return botHasAdmin
    }

    fun botHasAuthority(): Boolean {
        return botHasAuthority
    }

    fun isAdmin(): Boolean {
        return isAdmin
    }

    fun isCommand(): Boolean {
        return isCommand
    }

    fun getCommand(): String? {
        return command
    }

}