package com.scomarlf.bot

import com.scomarlf.bot.dailyTask.DailyTaskExecutor
import com.scomarlf.bot.dailyTask.UpdateAndRemindTask
import com.scomarlf.bot.handlers.*
import com.scomarlf.bot.utils.*
import com.scomarlf.conf.BotConf
import com.scomarlf.conf.CommandConf
import com.scomarlf.conf.LangConf
import com.scomarlf.conf.LangItems
import com.scomarlf.service.Instantiate
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import java.util.regex.Pattern

class IndexBot(botOptions: DefaultBotOptions) : UpdateAndRemindTask.Callback,
    AbilityBot(BotConf.TOKEN, BotConf.USERNAME, botOptions) {

    init {
        println("bot start:" + BotConf.USERNAME)

        Handlers.mainResponseHandler = MainResponseHandler(sender)
        Handlers.msgResponseHandler = MsgResponseHandler(sender)
        Handlers.enrollResponseHandler = EnrollResponseHandler(sender)
        Handlers.approveResponseHandler = ApproveResponseHandler(sender)

        // daily task
        Handlers.dailyTaskExecutor = DailyTaskExecutor(UpdateAndRemindTask(this))
        // set daily execute time
        Handlers.dailyTaskExecutor!!.startExecutionAt(23, 59, 0)
        // operation states recorder
        Handlers.operStates = HashMap()
    }

    override fun creatorId(): Int {
        return BotConf.CREATER;
    }

    /**
     * daily statistics
     */
    override fun dailyStatistics() {
        Handlers.mainResponseHandler!!.dailyStatistics()
    }

    override fun onUpdateReceived(update: Update) {
        // format update oper
        val updateAttributes = PrivateUpdateAttributes.Init(update)

        // click inline button
        if (updateAttributes!!.isBtnClicked())
            excute4button(update)
        // private message use command
        else if (updateAttributes.isPrivateMsg() && updateAttributes.isUseCommand())
            excute4command(update)
        // private message text message
        else if (updateAttributes.isPrivateMsg() && updateAttributes.isHasTextMsg())
            excute4text(update)
        // use command in approve group
        else if (updateAttributes.isGroupMsg() && updateAttributes.isFromManageGroup())
            excute4manageGroupCommand(update)
        // use command in other group
        else if (updateAttributes.isGroupMsg())
            excute4groupCommand(update)
        else if (updateAttributes.isChannelMsg() && updateAttributes.isHasTextMsg())
            excute4channelCommand(update)

        // save user detail
        if (updateAttributes.isPrivateMsg())
            Handlers.mainResponseHandler?.saveUser(update)
    }


    /**
     * 私聊根据命令执行
     *
     * @param update
     */
    private fun excute4command(update: Update) {
        // 会话ID
        val chatId = update.message.chatId
        // 命令代码
        val command = update.message.text
        when (command) {
            CommandConf.START -> Handlers.mainResponseHandler!!.start(chatId)
            CommandConf.LIST -> Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(
                chatId,
                LangConf.get(LangItems.LIST),
                KeyboardFactory.getStartKeyBorard()
            )
            CommandConf.ENROLL -> Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(
                chatId,
                LangConf.get(LangItems.ENROLL),
                null
            )
            CommandConf.HELP -> Handlers.msgResponseHandler!!.sendEasyMsg(chatId, LangConf.get(LangItems.HELP))
            // 重置状态
            CommandConf.CANCEL -> Handlers.mainResponseHandler!!.resetOperState(chatId)
            else -> {
                if (command.startsWith("/start"))
                    excute4superCommand(update)
                else if (chatId == BotConf.CREATER.toLong())
                    excute4creatorCommand(chatId, command)
            }
        }
    }

    /**
     * 群组内使用命令  (管理组)
     *
     * @param update
     */
    fun excute4manageGroupCommand(update: Update) {
        // 会话ID
        val chatId = update.message.chatId
        val userId = update.message.from.id
        // 命令代码
        val command = update.message.text.replace("@" + BotConf.USERNAME, "")
        //  需要在特定群组内  需要@本机器人
        if (!update.message.text.contains(BotConf.USERNAME)) return
        when (command) {
            // 开始审核
            CommandConf.APPROVE -> Handlers.approveResponseHandler!!.startApprove(chatId,userId)
            // 每日汇总
            CommandConf.DAILY -> Handlers.mainResponseHandler!!.dailyStatistics()
            // 审批规则
            CommandConf.ROLE -> Handlers.msgResponseHandler!!.sendHtmlWithBtnMsg(
                chatId,
                LangConf.get(LangItems.ROLE),
                null
            )
            // make this group is approve group
            CommandConf.APPROVE_INIT -> Handlers.mainResponseHandler!!.ecologyInit(
                update.message.chat,
                update.message.from.id,
                "approve"
            )
            else -> Handlers.msgResponseHandler!!.sendEasyMsg(chatId, LangConf.get(LangItems.GROUP_CANNOT_USE))
        }
    }

    /**
     * 群组内使用命令  (普通群组)
     *
     * @param update
     */
    fun excute4groupCommand(update: Update) {
        val updateAttributes = GroupUpdateAttributes.Init(update, sender)
        // check and go on
        val goOn = Handlers.mainResponseHandler!!.checkForGroup(updateAttributes)
        if (!goOn) return
        // execute for group command
        when (updateAttributes!!.getCommand()) {
            // 申请收录
            CommandConf.ENROLL -> Handlers.enrollResponseHandler!!.enrollGroup(
                updateAttributes.getChatId(),
                update.message.from
            )
            // make this group is approve group
            CommandConf.APPROVE_INIT -> Handlers.mainResponseHandler!!.ecologyInit(
                update.message.chat,
                update.message.from.id,
                "approve"
            )
            else -> Handlers.msgResponseHandler!!.sendEasyMsg(
                updateAttributes.getChatId(),
                LangConf.get(LangItems.GROUP_CANNOT_USE)
            )
        }
    }

    /**
     * 频道中使用命令
     */
    fun excute4channelCommand(update: Update) {
        val updateAttributes = ChannelUpdateAttributes.Init(update)
        if (!updateAttributes!!.isCommand()) return
        when (updateAttributes.getCommand()) {
            CommandConf.BULLETIN_INIT -> Handlers.mainResponseHandler!!.ecologyInit(
                update.channelPost.chat,
                BotConf.CREATER,
                "bulletin"
            )
        }
    }

    /**
     * only creator use
     */
    private fun excute4creatorCommand(chatId: Long, command: String) {
        when (command) {
            // reset approve group and bulletin channel
            CommandConf.APPROVE_RESET -> {
                Instantiate.ecologyService.setConf(null, "approve")
                Handlers.msgResponseHandler!!.sendEasyMsg(chatId, LangConf.get(LangItems.RESET_SUCCESS))
            }
            CommandConf.BULLETIN_RESET -> {
                Instantiate.ecologyService.setConf(null, "bulletin")
                Handlers.msgResponseHandler!!.sendEasyMsg(chatId, LangConf.get(LangItems.RESET_SUCCESS))
            }
        }
    }

    /**
     * 根据高级命令执行
     *
     * @param update
     */
    private fun excute4superCommand(update: Update) {
        // 命令内容
        val msg = update.message.text
        if (msg.startsWith("/start") && msg.replace("/start", "").length > 0) // 查询 频道或群组
            Handlers.mainResponseHandler!!.replyChannelEdit(
                update.message.chatId,
                update.message.from.id,
                null,
                msg.replace("/start ", ""),
                null
            )
        else  // 难以理解
            Handlers.msgResponseHandler!!.sendEasyMsg(update.message.chatId, LangConf.get(LangItems.CANNOT_UNDERSTAND))
    }

    /**
     * 根据内容执行
     *
     * @param update
     */
    private fun excute4text(update: Update) {
        val text = update.message.text
        val chatId = update.message.chatId
        if (Handlers.mainResponseHandler!!.hasOperState(chatId))
        // 根据待操作状态执行
            excute4oper(update)
        else if (isUsername(text))
        // 提交收录申请
            Handlers.enrollResponseHandler!!.enroll(update)
        else
        // 根据群组类型获取群组列表
            Handlers.mainResponseHandler!!.getGroupsByType(update)
    }

    /**
     * 根据点击的按钮执行
     *
     * @return
     */
    private fun excute4button(update: Update) {
        // 获取点击按钮的数据
        val btnCallBack: BtnCallBack? = BtnCallBack.Init(update)
        // 有无权限继续操作
        if (btnCallBack == null) {
            Handlers.msgResponseHandler!!.sendAnswerCallbackQuery(update.callbackQuery.id, "无权操作")
            return
        } else {
            Handlers.msgResponseHandler!!.sendAnswerCallbackQuery(update.callbackQuery.id, null)
        }
        when (btnCallBack.oper) {
            // 审核
            "approve" -> Handlers.approveResponseHandler!!.approve(update, btnCallBack)
            // 列表翻页
            "page" -> Handlers.mainResponseHandler!!.getPreOrNextGroupsByType(update, btnCallBack)
            // 设定待操作状态-修改信息
            "detail" -> {
                if (btnCallBack.type.equals("class"))
                    Handlers.mainResponseHandler!!.replyChannelEdit(
                        btnCallBack.chatId,
                        btnCallBack.userId,
                        btnCallBack.messageId,
                        btnCallBack.content,
                        "class"
                    )
                else
                    Handlers.enrollResponseHandler!!.enrollStatusManage(btnCallBack)
            }
            "class"-> Handlers.enrollResponseHandler!!.setRecordClass(btnCallBack)
        }
    }

    /**
     * 根据待操作状态执行
     *
     * @param update
     */
    private fun excute4oper(update: Update) {
        // 查询其待操作状态
        val operState: OperState? = Handlers.operStates!![update.message.chatId]
        when (operState?.getBotState()) {
            BotState.AWAITING_EDIT_TITLE,
            BotState.AWAITING_EDIT_TAG,
            BotState.AWAITING_EDIT_REMARK -> Handlers.enrollResponseHandler!!.operByState(update)
        }
    }

    /**
     * test the content if is bot/group/channel url link
     */
    private fun isUsername(text: String): Boolean {
        if (!text.startsWith("@") && !text.startsWith("https://t.me/")) return false
        val chatId = text.replace("@", "").replace("https://t.me/", "").replace("joinchat/", "")
        return Pattern.matches("^[a-zA-Z][a-zA-Z0-9_-]{4,32}$", chatId)
    }

}