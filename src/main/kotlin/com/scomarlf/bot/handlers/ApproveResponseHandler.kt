package com.scomarlf.bot.handlers

import com.scomarlf.bot.utils.BtnCallBack
import com.scomarlf.bot.utils.GroupType
import com.scomarlf.conf.BotConf
import com.scomarlf.conf.LangConf
import com.scomarlf.conf.LangItems
import com.scomarlf.generated.tables.pojos.Enroll
import com.scomarlf.generated.tables.pojos.VEnroll
import com.scomarlf.service.Instantiate
import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMembersCount
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.time.LocalDateTime
import java.util.*


class ApproveResponseHandler(sender: MessageSender?) {
    var sender: MessageSender? = null;

    init {
        this.sender = sender
    }

    /**
     * 提醒共有多少条记录待审批
     */
    fun remindApprove() {
        val count: Int = Instantiate.statisticsService.getWaitApproveCount();
        val messageContent = "${LangConf.get(LangItems.WAIT_APPROVE)}$count\n${LangConf.get(LangItems.APPROVE_COMMAND)}"
        Handlers.msgResponseHandler!!.sendEasyMsg(BotConf.APPROVE_ID!!, messageContent)
    }

    /**
     * 开始审核
     *
     * @param chatId
     */
    fun startApprove(chatId: Long,userId:Int) {
        val enroll: VEnroll? = Instantiate.approveService.getApproveNext()
        if (enroll != null)
            Handlers.mainResponseHandler!!.replyChannelEdit(chatId, userId, null, enroll.id, "approve")
        else
            Handlers.msgResponseHandler!!.sendEasyMsg(chatId, LangConf.get(LangItems.NO_WAIT_APPROVE))
    }

    /**
     * 回执审核结果，并开始下一个
     *
     * @param update
     */
    fun approve(update: Update, btnCallBack: BtnCallBack) {
        try {
            // btnCallBack.type ---- pass refuse
            val isPass = btnCallBack.type.equals("pass")
            // 发布收录公告
            var channelMsgId: Int? = null
            if (btnCallBack.type.equals("pass"))
                channelMsgId = Handlers.mainResponseHandler!!.replyChannelEdit(
                    BotConf.BULLETIN_ID!!,
                    null,
                    null,
                    btnCallBack.content,
                    "channel"
                )
            // 获取申请信息
            val enroll: VEnroll? = Instantiate.enrollService.getViewById(btnCallBack.content)
            // 记录审批信息  并  使更改收录表状态使其可展示
            Instantiate.approveService.approve(enroll!!.id, isPass, btnCallBack.userId!!.toLong())
            // 删除审核操作信息
            Handlers.msgResponseHandler!!.deleteMsg(btnCallBack.chatId, btnCallBack.messageId)
            // 回执信息
            val link = if (enroll.username != null) "https://t.me/" + enroll.username else enroll.invitelink
            // 回执 审核群组 收录结果
            replyApproveResule(BotConf.APPROVE_ID!!, isPass, enroll.title, link, channelMsgId)
            // 回执 申请收录人  审核人  收录结果
            replyApproveResule(enroll.createuser, isPass, enroll.title, link, channelMsgId)
            // 开始下一条审核
            startApprove(btnCallBack.chatId,btnCallBack.userId)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    /**
     * 通知审核结果审核
     *
     * @param chatId
     * @param result
     * @param channelName
     * @param type
     * @param channelMsgId
     */
    fun replyApproveResule(chatId: Long, isPass: Boolean, channelName: String?, link: String, channelMsgId: Int?) {
        val sb = StringBuilder()
        if (isPass) {
            sb.append("<a href='$link'>$channelName</a>${LangConf.get(LangItems.ENROLL_SUCCESS)}\n")
            sb.append("<a href='https://t.me/${BotConf.BULLETIN_USERNAME}/${channelMsgId.toString()}'>点击查看详情</a>")
        } else {
            sb.append("<a href='$link'>$channelName</a>${LangConf.get(LangItems.ENROLL_ERROR)}")
        }
        Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(chatId, sb.toString(), null)
    }

}