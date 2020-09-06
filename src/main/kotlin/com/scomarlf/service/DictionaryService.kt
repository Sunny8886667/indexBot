package com.scomarlf.service

import com.scomarlf.bot.utils.GroupType
import com.scomarlf.generated.tables.pojos.User
import com.scomarlf.utils.HikariManager
import com.scomarlf.generated.tables.User.USER
import com.scomarlf.generated.tables.pojos.Dictionary
import java.time.LocalDateTime

class DictionaryService {

    /**
     * get list by parent id from database
     */
    fun getListByParentId(parentId: String): List<Dictionary> {
        val create = HikariManager.getDSLContext()
        return try {
            val dics: List<Dictionary> = create.select().from(com.scomarlf.generated.tables.Dictionary.DICTIONARY)
                .where(com.scomarlf.generated.tables.Dictionary.DICTIONARY.PARENTID.eq(parentId))
                .orderBy(com.scomarlf.generated.tables.Dictionary.DICTIONARY.SORT)
                .fetchInto(Dictionary::class.java)
            dics
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            create.close()
        }
    }

}