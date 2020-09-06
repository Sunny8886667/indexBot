package com.scomarlf.bot.handlers

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback
import java.lang.Exception

class CallbackHandler: SentCallback<Boolean>{
    override fun onResult(p0: BotApiMethod<Boolean>?, p1: Boolean?) {
        TODO("Not yet implemented")
    }

    override fun onError(p0: BotApiMethod<Boolean>?, p1: TelegramApiRequestException?) {
        TODO("Not yet implemented")
    }

    override fun onException(p0: BotApiMethod<Boolean>?, p1: Exception?) {
        TODO("Not yet implemented")
    }
}