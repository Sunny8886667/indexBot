package com.scomarlf.bot.handlers

import com.scomarlf.bot.utils.BotState
import com.scomarlf.bot.utils.BtnCallBack
import com.scomarlf.bot.utils.OperState
import com.scomarlf.conf.LangConf
import com.scomarlf.conf.LangItems
import com.scomarlf.generated.tables.Record.RECORD
import com.scomarlf.generated.tables.pojos.Enroll
import com.scomarlf.generated.tables.pojos.Record
import com.scomarlf.service.Instantiate
import org.jvnet.hk2.internal.InstantiationServiceImpl
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMembersCount
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.time.LocalDateTime
import java.util.*


class EnrollResponseHandler(sender: MessageSender?) {
    var sender: MessageSender? = null;

    init {
        this.sender = sender
    }

    /**
     * enroll the bot
     */
    fun enroll(update: Update) {
        // get bot username
        val username = update.message.text.replace("@", "").replace("https://t.me/", "")
        try {
            // check the bot is enrolled
            val record = Instantiate.recordService.getByIdOrUsername(username)
            val isEnrolled: Boolean = if (record == null) false else Instantiate.enrollService.isEnrolled(record)
            // check if it's a bot
            val getChat = GetChat()
            getChat.chatId = "@$username"
            val chat = sender!!.execute(getChat)
            if (chat.isGroupChat || chat.isSuperGroupChat) {
                Handlers.msgResponseHandler!!.sendEasyMsg(update.message.chatId, LangConf.get(LangItems.NEED_IN_GROUP))
                return
            } else if (chat.isChannelChat) {
                Handlers.msgResponseHandler!!.sendEasyMsg(
                    update.message.chatId,
                    LangConf.get(LangItems.NEED_IN_CHANNEL)
                )
                return
            }
            // get bot detail

            // save the enroll data
            if (!isEnrolled) {
//                val chatTitle = if (chat.title.length > 50) chat.title.substring(0, 49) else chat.title
//                val enroll = Enroll()
//                enroll.setId(UUID.randomUUID().toString())
//                enroll.setUserId(update.message.chat.id)
//                enroll.setChannelId(chat.id)
//                enroll.setChannelCode(chat.userName)
//                enroll.setTitle(chatTitle)
//                enroll.setRemark(chat.description)
//                enroll.setTime(LocalDateTime.now())
//                enroll.setStatus(false)
//                Handlers.enrollService.insert(enroll)
//                // 回执详情，以供修改
//                Handlers.mainResponseHandler!!.replyChannelEdit(
//                    update.message.chatId,
//                    update.message.from.id,
//                    null,
//                    enroll.getId(),
//                    "edit"
//                )
            } else {
                Handlers.msgResponseHandler!!.sendEasyMsg(update.message.chatId, LangConf.get(LangItems.ENROLLED))
            }
        } catch (e: TelegramApiException) {
            Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(
                update.message.chatId,
                LangConf.get(LangItems.SEARCH_FAIL),
                null
            )
        }
    }

    /**
     * enroll the channel or group
     */
    fun enrollGroup(chatId: Long, user: User) {
        // group detail
        val getChat = GetChat()
        getChat.setChatId(chatId)
        val chat = sender!!.execute(getChat)
        // check the channel or group is enrolled
        val record = Instantiate.recordService.getByIdOrUsername(chat!!.id.toString())
        val isEnrolled: Boolean = if (record == null) false else Instantiate.enrollService.isEnrolled(record)
        if (!isEnrolled) {
            // create invite link if it's private group
            var inviteLink: String? = null
            if (chat.userName == null) {
                val exportChatInviteLink = ExportChatInviteLink()
                exportChatInviteLink.setChatId(chat.id)
                inviteLink = sender!!.execute(exportChatInviteLink)
            }
            // get member count
            val getChatMembersCount = GetChatMembersCount()
            getChatMembersCount.setChatId(chat.id)
            val membersCount = sender!!.execute(getChatMembersCount)
            // save enroll data
            val recordId = Instantiate.enrollService.save4channelOrGroup(record,chat, inviteLink, membersCount.toLong(), user)
            // reply
            Handlers.mainResponseHandler!!.replyChannelEdit(
                user.id.toLong(),
                user.id,
                null,
                recordId.toString(),
                "edit"
            )
        } else {
            Handlers.msgResponseHandler!!.sendEasyMsg(chat.id, LangConf.get(LangItems.ENROLLED))
        }
    }


    /**
     * manage the chat status for enroll
     */
    fun enrollStatusManage(btnCallBack: BtnCallBack) {
        // update status
        var resultMsg = ""
        when (btnCallBack.type) {
            "title" -> {
                resultMsg = LangConf.get(LangItems.EDIT_TITLE_REPLY)
                Handlers.mainResponseHandler!!.setOperState(btnCallBack, BotState.AWAITING_EDIT_TITLE)
            }
            "remark" -> {
                resultMsg = LangConf.get(LangItems.EDIT_REMARK_REPLY)
                Handlers.mainResponseHandler!!.setOperState(btnCallBack, BotState.AWAITING_EDIT_REMARK)
            }
            "tag" -> {
                resultMsg = LangConf.get(LangItems.EDIT_TAG_REPLY)
                Handlers.mainResponseHandler!!.setOperState(btnCallBack, BotState.AWAITING_EDIT_TAG)
            }
            "class" -> {
                resultMsg = LangConf.get(LangItems.EDIT_TAG_REPLY)
                Handlers.mainResponseHandler!!.setOperState(btnCallBack, BotState.AWAITING_EDIT_TAG)
            }
            "submit" -> {
                // update enroll status to submit status
                Instantiate.enrollService.submit(btnCallBack.content.toLong())
                // remind
                Handlers.approveResponseHandler!!.remindApprove()
                // delete console message
                Handlers.msgResponseHandler!!.deleteMsg(btnCallBack.chatId, btnCallBack.messageId)
                resultMsg = LangConf.get(LangItems.SUBMIT_REPLY)
                Handlers.operStates!!.remove(btnCallBack.chatId)
            }
        }
        // reply prompt message
        Handlers.msgResponseHandler!!.sendEasyMsg(btnCallBack.chatId, resultMsg)
    }

    /**
     * set channel/bot/group classification
     */
    fun setRecordClass(btnCallBack: BtnCallBack) {
        // update the data
        if (!btnCallBack.type.equals("cancel"))
            Instantiate.recordService.updateById(
                btnCallBack.content.toLong(),
                RECORD.CLASSIFICATION,
                btnCallBack.type,
                btnCallBack.userId!!.toInt()
            )
        // edit the message
        Handlers.mainResponseHandler!!.replyChannelEdit(
            btnCallBack.chatId,
            btnCallBack.userId,
            btnCallBack.messageId,
            btnCallBack.content,
            "edit"
        )
    }


    /**
     * 根据待操作状态
     *
     * @param update
     */
    fun operByState(update: Update) {
        var editSuccess = false
        val text = update.message.text
        val operState = Handlers.operStates!![update.message.chatId]
        val recordId = operState!!.getRecordId()!!
        when (operState.getBotState()) {
            BotState.AWAITING_EDIT_TITLE -> {
                if (text.length < 26) {
                    editSuccess = true
                    Instantiate.recordService.updateById(recordId, RECORD.TITLE, text, update.message.from.id)
                }
            }
            BotState.AWAITING_EDIT_TAG -> {
                if (text.length < 150) {
                    editSuccess = true
                    Instantiate.recordService.updateById(recordId, RECORD.TAG, text, update.message.from.id)
                }
            }
            BotState.AWAITING_EDIT_REMARK -> {
                editSuccess = true
                Instantiate.recordService.updateById(recordId, RECORD.REMARK, text, update.message.from.id)
            }
        }

        if (editSuccess) {
            Handlers.operStates!!.remove(update.message.chatId)
            Handlers.msgResponseHandler!!.sendEasyMsg(update.message.chatId, LangConf.get(LangItems.EDIT_SUCCESS))
            Handlers.mainResponseHandler!!.replyChannelEdit(
                update.message.chatId,
                update.message.from.id,
                operState.getMessageId(),
                recordId.toString(),
                "edit"
            )
        } else {
            Handlers.msgResponseHandler!!.sendEasyMsg(update.message.chatId, LangConf.get(LangItems.TOO_LONG))
        }
    }


}