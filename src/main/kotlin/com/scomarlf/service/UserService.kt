package com.scomarlf.service

import com.scomarlf.generated.tables.pojos.User
import com.scomarlf.utils.HikariManager
import com.scomarlf.generated.tables.User.USER
import java.time.LocalDateTime

class UserService {

    /**
     * get data by id from database
     */
    fun getById(userId: Long): User? {
        val create = HikariManager.getDSLContext()
        return try {
            // select user from database
            val user: User = create.select().from(USER).where(USER.ID.eq(userId)).fetchSingle().into(User::class.java)
            user
        } catch (e: Exception) {
            null
        } finally {
            create.close()
        }
    }

    /**
     * save or update user
     */
    fun saveUser(telegramUser: org.telegram.telegrambots.meta.api.objects.User) {
        val create = HikariManager.getDSLContext()
        try {
            // select user from database
            var user: User? = getById(telegramUser.id.toLong())
            if (user == null) {
                // if not exist,insert data
                user = User()
                user.setId(telegramUser.id.toLong())
                user.setFirstname(telegramUser.firstName)
                user.setLastname(telegramUser.lastName)
                user.setIsbot(telegramUser.bot)
                user.setLanguagecode(telegramUser.languageCode)
                user.setCreatetime(LocalDateTime.now())
                user.setUpdatetime(LocalDateTime.now())
                create.newRecord(USER, user).insert()
            } else {
                // if exist,update data
                create.update(USER)
                    .set(USER.FIRSTNAME, telegramUser.firstName)
                    .set(USER.LASTNAME, telegramUser.lastName)
                    .set(USER.ISBOT, telegramUser.bot)
                    .set(USER.LANGUAGECODE, telegramUser.languageCode)
                    .set(USER.UPDATETIME, LocalDateTime.now())
                    .where(USER.ID.eq(telegramUser.id.toLong()))
                    .execute()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            create.close()
        }
    }

}