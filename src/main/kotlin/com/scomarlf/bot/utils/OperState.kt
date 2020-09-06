package com.scomarlf.bot.utils

enum class BotState {
    AWAITING_EDIT_TITLE,
    AWAITING_EDIT_TAG,
    AWAITING_EDIT_REMARK
}

class OperState {

    private var botState: BotState? = null
    private var content: String? = null
    private var channelId: String? = null
    private var recordId: Long? = null
    private var messageId: Int? = null

    fun OperState() {}

    fun OperState(
        botState: BotState?,
        channelId: String?,
        content: String?,
        messageId: Int?
    ) {
        this.botState = botState
        this.channelId = channelId
        this.messageId = messageId
        this.content = content
    }

    fun getBotState(): BotState? {
        return botState
    }

    fun setBotState(botState: BotState?) {
        this.botState = botState
    }

    fun getContent(): String? {
        return content
    }

    fun setContent(content: String?) {
        this.content = content
    }

    fun getRecordId(): Long? {
        return recordId
    }

    fun setRecordId(recordId: Long?) {
        this.recordId = recordId
    }

    fun getChannelId(): String? {
        return channelId
    }

    fun setChannelId(channelId: String?) {
        this.channelId = channelId
    }

    fun getMessageId(): Int? {
        return messageId
    }

    fun setMessageId(messageId: Int?) {
        this.messageId = messageId
    }
}