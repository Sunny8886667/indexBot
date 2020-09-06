package com.scomarlf

import com.scomarlf.bot.IndexBot
import com.scomarlf.bot.utils.GroupType
import com.scomarlf.conf.BotConf
import com.scomarlf.conf.DatabaseConf
import com.scomarlf.conf.LangConf
import com.scomarlf.generated.tables.Dictionary.DICTIONARY
import com.scomarlf.generated.tables.Ecology.ECOLOGY
import com.scomarlf.generated.tables.pojos.Ecology
import com.scomarlf.generated.tables.pojos.Dictionary
import com.scomarlf.service.Instantiate
import com.scomarlf.utils.HikariManager
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.*

fun main(args: Array<String>) {
    // read config file
    val readStatus = BotConf.init() && DatabaseConf.init() && LangConf.init()
    if (readStatus) {
        // create database connection
        HikariManager.createConnection()
        // get approve group and bulletin channel
        Instantiate.ecologyService.getConf()
        // get group type
        getGroupType()
        // start bot
        botStart()
    }
}

/**
 * get group/channel/bot type from database
 */
fun getGroupType() {
    val dics: List<Dictionary> = Instantiate.dictionaryService.getListByParentId("classificationType")
    GroupType.init(dics)
}

/**
 * start bot application
 */
fun botStart() {
    // Initialize Api Context
    ApiContextInitializer.init()
    // Set up Http proxy
    val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)
    botOptions.proxyHost = "127.0.0.1"
    botOptions.proxyPort = 7890
    botOptions.proxyType = DefaultBotOptions.ProxyType.HTTP
    // Instantiate Telegram Bots API
    val botsApi = TelegramBotsApi()
    // Register bot
    try {
        // don't use proxy
        // botsApi.registerBot(IndexBot(DefaultBotOptions()))
        // use proxy
        botsApi.registerBot(IndexBot(botOptions));
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
}

