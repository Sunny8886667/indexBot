package com.scomarlf.bot.handlers

import com.scomarlf.bot.utils.*
import com.scomarlf.service.Instantiate
import com.scomarlf.conf.BotConf
import com.scomarlf.conf.LangConf
import com.scomarlf.conf.LangItems
import com.scomarlf.generated.tables.pojos.Record
import com.scomarlf.generated.tables.pojos.VEnroll
import com.scomarlf.generated.tables.pojos.VRecord
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat


class MainResponseHandler(sender: MessageSender?) {
    var sender: MessageSender? = null;

    init {
        this.sender = sender
    }

    fun start(chatId: Long) {
        // need set approve group
        if (BotConf.APPROVE_ID == 0L)
            Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(
                chatId,
                LangConf.get(LangItems.NEED_SET_APPROVE),
                null
            )
        // need set bulletin channel
        if (BotConf.BULLETIN_ID == 0L)
            Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(
                chatId,
                LangConf.get(LangItems.NEED_SET_BULLETIN),
                null
            )
        // start msg
        Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(
            chatId,
            LangConf.get(LangItems.START),
            null
        )
    }

    /**
     * 根据类型查找群组列表
     *
     * @param update
     */
    fun getGroupsByType(update: Update) {
        val type = update.message.text
        makeChannelPageByTypeOrTag(update.message.chatId, null, type, 1)
    }

    /**
     * 根据类型查找群组列表(翻页)
     *
     * @param update
     */
    fun getPreOrNextGroupsByType(update: Update, btnCallBack: BtnCallBack) {
        // 会话及消息ID
        val chatId = update.callbackQuery.from.id.toLong()
        val messageId = update.callbackQuery.message.messageId.toLong()
        // 编辑跳页消息
        makeChannelPageByTypeOrTag(chatId, messageId, btnCallBack.type, btnCallBack.content.toLong())
    }

    /**
     * 制作频道列表（并返回）
     *
     * @param chatId
     * @param typeOrTag
     * @param currentPage
     * @throws TelegramApiException
     */
    private fun makeChannelPageByTypeOrTag(chatId: Long, messageId: Long?, typeOrKeyword: String, pageCurrent: Long) {
        // 查询列表内容
        val page: Page<Record>? =
            if (GroupType.getTypes().contains(typeOrKeyword))
                Instantiate.recordService.getPageByType(typeOrKeyword, pageCurrent)
            else
                Instantiate.recordService.getPageByKeyword(typeOrKeyword, pageCurrent)
        // 无数据回执无数据消息
        if (page == null || page.records?.isEmpty()!!) {
            Handlers.msgResponseHandler!!.sendEasyMsg(chatId, LangConf.get(LangItems.NOTHING))
            return
        }
        // 返回列表
        val channelListStr: String = getChannelListStr(page)
        // 如果是点击类型，则发送消息
        if (messageId == null)
            Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(
                chatId,
                channelListStr,
                KeyboardFactory.getChannelPageKeyBorard(typeOrKeyword, page)
            )
        // 如果是点击跳页按钮，则编辑信息
        else
            Handlers.msgResponseHandler!!.editHtmlWithBtnMsg(
                chatId,
                messageId.toInt(),
                channelListStr,
                KeyboardFactory.getChannelPageKeyBorard(typeOrKeyword, page)
            )
    }

    /**
     * statistics for user and channel/group/bot
     */
    fun dailyStatistics() {
        // get data from database
        val dailyData = Instantiate.statisticsService.daily()
        if (dailyData.isEmpty())
            return
        // send statistics message to approve group
        val sb = java.lang.StringBuilder()
        sb.append(LangConf.get(LangItems.DAILY_USER_ADD) + dailyData[0] + "\n")
        sb.append(LangConf.get(LangItems.DAILY_USER_ACTIVE) + dailyData[1] + "\n")
        sb.append(LangConf.get(LangItems.USER_COUNT) + dailyData[2] + "\n")
        sb.append(LangConf.get(LangItems.RECORD_COUNT) + dailyData[3])
        Handlers.msgResponseHandler?.sendEasyMsg(BotConf.APPROVE_ID!!, sb.toString())
    }

    /**
     * save user data
     */
    fun saveUser(update: Update) {
        val saveUserThread = Thread {
            try {
                // get telegram user
                val telegramUser =
                    if (update.message != null) update.message.from
                    else update.callbackQuery.from
                // save or update user data
                Instantiate.userService.saveUser(telegramUser)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        saveUserThread.start()
    }

    /**
     * 是否有待操作状态
     *
     * @param chatId 会话ID
     * @return 是否有待操作状态
     */
    fun hasOperState(chatId: Long?): Boolean {
        val operState: OperState? = Handlers.operStates!![chatId]
        return operState != null
    }

    /**
     * set operate state
     */
    fun setOperState(btnCallBack: BtnCallBack, botState: BotState) {
        val operState = OperState()
        operState.setBotState(botState)
        operState.setRecordId(btnCallBack.content.toLong())
        operState.setMessageId(btnCallBack.messageId)
        Handlers.operStates!![btnCallBack.chatId] = operState
    }

    /**
     * reset operate state
     */
    fun resetOperState(chatOrUserId: Long) {
        val operState: OperState? = Handlers.operStates!![chatOrUserId]
        Handlers.operStates!!.remove(chatOrUserId)
        if (operState == null)
            Handlers.msgResponseHandler!!.sendEasyMsg(chatOrUserId, LangConf.get(LangItems.CANNOT_RESET_STATE_REPLY))
        else
            Handlers.msgResponseHandler!!.sendEasyMsg(chatOrUserId, LangConf.get(LangItems.RESET_STATE_REPLY))
    }

    /**
     * check user or bot authority
     */
    fun checkForGroup(updateAttributes: GroupUpdateAttributes?): Boolean {
        // if is join bot message
        // if bot don't has authority
        if (updateAttributes!!.isJoinBot() || !updateAttributes.botHasAdmin() || !updateAttributes.botHasAuthority()) {
            Handlers.msgResponseHandler!!.sendEasyMsg(
                updateAttributes.getChatId(),
                LangConf.get(LangItems.JOIN_GROUP)
            )
            return false
        }
        // just response for command
        if (!updateAttributes.isCommand())
            return false
        // just for admin command
        if (!updateAttributes.isAdmin()) {
            Handlers.msgResponseHandler!!.sendEasyMsg(
                updateAttributes.getChatId(),
                LangConf.get(LangItems.JUST_CREATOR)
            )
            return false
        }
        return true
    }

    /**
     * make group is approve group    ---    type="approve"
     * make channel is bulletin channel    ---    type="bulletin"
     */
    fun ecologyInit(chat: Chat, userId: Int, type: String) {
        val value = if (type.equals("approve")) BotConf.APPROVE_ID else BotConf.BULLETIN_ID
        val defaultErr = if (type.equals("approve")) LangItems.SET_APPROVE_ERROR else LangItems.SET_BULLETIN_ERROR

        if (userId != BotConf.CREATER) {
            Handlers.msgResponseHandler!!.sendEasyMsg(chat.id, LangConf.get(LangItems.NO_AUTHORITY))
        } else if (value != null) {
            Handlers.msgResponseHandler!!.sendEasyMsg(BotConf.CREATER.toLong(), LangConf.get(defaultErr))
        } else {
            Instantiate.ecologyService.setConf(chat, type)
            Instantiate.ecologyService.getConf()
            Handlers.msgResponseHandler!!.sendEasyMsg(chat.id, LangConf.get(LangItems.SET_SUCCESS))
        }
    }

    // 频道、群组、bot详情
    fun replyChannelEdit(
        chatId: Long,
        userId: Int?,
        messageId: Int?,
        recordOrEnrollId: String,
        type: String?
    ): Int? {
        // 尝试获取详情
        var record: VRecord?
        var enroll: VEnroll? = null
        try {
            record = Instantiate.recordService.getViewById(recordOrEnrollId.toLong())
        } catch (e: Exception) {
            enroll = Instantiate.enrollService.getViewById(recordOrEnrollId)
            record = Instantiate.recordService.getViewById(enroll!!.recordid)
        }
        // 拼接消息内容
        val msgStr: String? = getChannelDetailStr(record)
        // 生成按钮阵列
        val replyKeyboard: ReplyKeyboard?
        val keyboardType = type ?: "default"
        replyKeyboard = when (keyboardType) {
            "edit" -> KeyboardFactory.getEnrollKeyBorard(record!!.id, userId!!)
            "channel" -> KeyboardFactory.getSelectKeyBorard(record!!.id)
            "approve" -> KeyboardFactory.getApproveKeyBorard(enroll!!.id,userId!!)
            "class" -> KeyboardFactory.getClassKeyBorard(record!!.id,userId!!)
            else -> null
        }
        // 发送消息
        val resultMsg: Message?
        resultMsg =
            if (messageId == null)
            // 发送消息
                Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(chatId, msgStr!!, replyKeyboard)
            else
            // 编辑消息
                Handlers.msgResponseHandler!!.editHtmlWithBtnMsg(chatId, messageId, msgStr!!, replyKeyboard)
        return resultMsg!!.messageId
    }

    /**
     * 整理列表内容
     *
     * @param page
     * @return
     */
    fun getChannelListStr(page: Page<Record>?): String {
        val resultSB = StringBuilder()
        for (record in page!!.records!!) {
            // 频道或群组图标
            when (record.getType()) {
                "private" -> resultSB.append("\uD83D\uDD10 ")
                "group" -> resultSB.append("\uD83D\uDC65 ")
                "channel" -> resultSB.append("\uD83D\uDCE2 ")
                "bot" -> resultSB.append("\uD83E\uDD16 ")
            }
            // 成员数量(bot无法获取成员数量)
            if (!record.getType().equals("bot"))
                resultSB.append(getMemberUnit(record.membernumber)).append(" | ")
            else
                resultSB.append("| ")
            // 名称及地址
            resultSB.append(
                """
                <a href='https://t.me/${BotConf.USERNAME}?start=${record.getId()}'>
                ${record.title!!.replace("<", "&lt;").replace(">", "&gt;")}
                </a>

                """.trimIndent()
            )
        }
        return resultSB.toString()
    }

    fun getChannelDetailStr(record: VRecord?): String? {
        // 判断使用私有链接还是公有链接
        val link = if (record!!.username != null) "https://t.me/" + record.username else record.invitelink
        // 拼接字符串
        val sb = StringBuilder()
        sb.append(
            """
            <b>标题</b>： <a href="$link">${record.getTitle()}</a>
            
            """.trimIndent()
        )
        sb.append(
            """
            <b>标签</b>： ${if (record.getTag() == null) "暂无" else record.getTag()}
            
            """.trimIndent()
        )
        sb.append(
            """
            <b>分类</b>： ${if (record.getClassification() == null) "审核后显示" else record.classificationname}
            
            """.trimIndent()
        )
        sb.append(
            """
            <b>简介</b>： 
            ${if (record.getRemark() == null) "" else record.getRemark().replace("<", "&lt;").replace(">", "&gt;")}
            
            
            """.trimIndent()
        )
        return sb.toString()
    }

    /**
     * 为数字增加单位
     *
     * @param count
     * @return
     */
    fun getMemberUnit(count: Long): String? {
        var unit = ""
        var size = count.toDouble()
        // 数值过大增加单位
        if (count > 1000000) {
            size = count / 1000000.0
            unit = "M"
        } else if (count > 1000) {
            size = count / 1000.0
            unit = "K"
        }
        // 保留1位小数
        val formater = DecimalFormat()
        formater.maximumFractionDigits = 1
        formater.groupingSize = 0
        formater.roundingMode = RoundingMode.FLOOR
        return formater.format(size) + unit
    }

}