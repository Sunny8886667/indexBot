package com.scomarlf.service

import com.scomarlf.conf.BotConf
import com.scomarlf.generated.tables.Ecology.ECOLOGY
import com.scomarlf.utils.HikariManager
import com.scomarlf.generated.tables.pojos.Ecology
import org.telegram.telegrambots.meta.api.objects.Chat

class EcologyService {

    /**
     * get all record from database
     */
    fun getConf(): Boolean {
        val create = HikariManager.getDSLContext()
        return try {
            val ecology: List<Ecology> = create.select().from(ECOLOGY).fetchInto(Ecology::class.java)
            for (item: Ecology in ecology) {
                if (item.chatid == null)
                    continue
                if (item.id.equals("approve")) {
                    // approve group
                    BotConf.APPROVE_ID = item.chatid
                    BotConf.APPROVE_USERNAME = item.username
                } else if (item.id.equals("bulletin")) {
                    // bulletin channel
                    BotConf.BULLETIN_ID = item.chatid
                    BotConf.BULLETIN_USERNAME = item.username
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            create.close()
        }
    }

    /**
     * set ecology config
     */
    fun setConf(chat: Chat?, type: String): Boolean {
        val create = HikariManager.getDSLContext()
        return try {
            if (chat == null)
                create.update(ECOLOGY)
                    .setNull(ECOLOGY.CHATID)
                    .setNull(ECOLOGY.USERNAME)
                    .where(ECOLOGY.ID.eq(type)).execute()
            else
                create.update(ECOLOGY)
                    .set(ECOLOGY.CHATID, chat.id)
                    .set(ECOLOGY.USERNAME, chat.userName)
                    .where(ECOLOGY.ID.eq(type)).execute()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            create.close()
        }
    }

}