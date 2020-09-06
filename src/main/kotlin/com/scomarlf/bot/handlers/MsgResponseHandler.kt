package com.scomarlf.bot.handlers

import org.telegram.abilitybots.api.sender.MessageSender
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard

class MsgResponseHandler(sender: MessageSender?) {

    private var sender: MessageSender? = null

    init {
        this.sender = sender
    }

    /**
     * Reply button clicked
     */
    fun sendAnswerCallbackQuery(callbackId: String, text: String?) {
        return try {
            val answerCallbackQuery = AnswerCallbackQuery()
            answerCallbackQuery.callbackQueryId = callbackId
            answerCallbackQuery.text = text
            sender!!.executeAsync(answerCallbackQuery,CallbackHandler())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Simple text message
     *
     * @param chatId chat id
     * @param msg text message
     * @return send result
     */
    fun sendEasyMsg(chatId: Long, msg: String): Message? {
        return try {
            val sendMessage = SendMessage()
            sendMessage.setChatId(chatId)
            sendMessage.text = msg
            sender!!.execute(sendMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Send text in Html format
     *
     * @param chatId chat id
     * @param msg text message
     * @param keyboard keyboard
     * @return send result
     */
    fun sendHtmlWithBtnMsg(chatId: Long, msg: String, keyboard: ReplyKeyboard?): Message? {
        return try {
            val sendMessage = SendMessage()
            sendMessage.setChatId(chatId)
            sendMessage.text = msg
            sendMessage.enableHtml(true)
            sendMessage.disableWebPagePreview()
            sendMessage.replyMarkup = keyboard
            sender!!.execute(sendMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Edit text in Html format
     *
     * @param chatId chat id
     * @param messageId message to be edited
     * @param msg text message
     * @param keyboard keyboard
     * @return send result
     */
    fun editHtmlWithBtnMsg(chatId: Long, messageId: Int, msg: String, keyboard: ReplyKeyboard?): Message? {
        return try {
            val message = EditMessageText()
            message.setChatId(chatId)
            message.messageId = messageId
            message.text = msg
            message.enableHtml(true)
            message.disableWebPagePreview()
            message.replyMarkup = keyboard as InlineKeyboardMarkup?
            sender!!.execute(message) as Message
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    /**
     * delete message
     *
     * @param chatId chat id
     * @param msgId message id
     * @return result of the delete operation
     */
    fun deleteMsg(chatId: Long, msgId: Int): Boolean {
        return try {
            val deleteMessage = DeleteMessage(chatId, msgId)
            sender!!.execute(deleteMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}