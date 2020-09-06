package com.scomarlf.service

import com.scomarlf.bot.utils.Page
import com.scomarlf.generated.tables.Enroll.ENROLL
import com.scomarlf.generated.tables.Record.RECORD
import com.scomarlf.generated.tables.VEnroll.V_ENROLL
import com.scomarlf.generated.tables.VRecord.V_RECORD
import com.scomarlf.generated.tables.pojos.Enroll
import com.scomarlf.generated.tables.pojos.Record
import com.scomarlf.generated.tables.pojos.VEnroll
import com.scomarlf.generated.tables.pojos.VRecord
import com.scomarlf.utils.HikariManager
import org.jooq.Condition
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDateTime
import java.util.*

class EnrollService {

    /**
     * get data by id
     */
    fun getById(enrollId: String): Enroll? {
        val create = HikariManager.getDSLContext()
        return try {
            // search data
            val enroll = create.select().from(ENROLL).where(ENROLL.ID.eq(enrollId)).fetchOne().into(Enroll::class.java)
            enroll
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            create.close()
        }
    }

    /**
     * get view data by id
     */
    fun getViewById(enrollId: String): VEnroll? {
        val create = HikariManager.getDSLContext()
        return try {
            // search data
            val enroll =
                create.select().from(V_ENROLL).where(V_ENROLL.ID.eq(enrollId)).fetchOne().into(VEnroll::class.java)
            enroll
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            create.close()
        }
    }

    /**
     * check the group/channel/bot is enrolled
     */
    fun isEnrolled(record: Record): Boolean {
        val create = HikariManager.getDSLContext()
        return try {
            // search data
            // approve pass or don't submit the enroll
            val count = create.select().from(V_ENROLL)
                .where(V_ENROLL.RECORDID.eq(record.id), V_ENROLL.STATUS.isFalse.or(V_ENROLL.APPROVESTATUS.isTrue))
                .count()
            // is enrolled if the search data not null
            count > 0
        } catch (e: Exception) {
            e.printStackTrace()
            true
        } finally {
            create.close()
        }
    }

    /**
     * save enroll for channel/group
     */
    fun save4channelOrGroup(
        record: Record?,
        chat: Chat,
        inviteLink: String?,
        membersCount: Long,
        user: User
    ): Long {
        val create = HikariManager.getDSLContext()
        return try {
            val chatTitle = if (chat.title.length > 50) chat.title.substring(0, 49) else chat.title
            val type = if (chat.isChannelChat) "channel" else "group"

            var recordSave = record
            if (recordSave == null) {
                recordSave = Record()
                recordSave.id = chat.id
                recordSave.username = chat.userName
                recordSave.invitelink = inviteLink
                recordSave.title = chatTitle
                recordSave.remark = chat.description
                recordSave.membernumber = membersCount
                recordSave.type = type
                recordSave.status = false
                recordSave.createtime = LocalDateTime.now()
                recordSave.createuser = user.id.toLong()
                create.newRecord(RECORD, recordSave).insert()
            } else {
                create.update(RECORD)
                    .set(RECORD.USERNAME, chat.userName)
                    .set(RECORD.INVITELINK, inviteLink)
                    .set(RECORD.MEMBERNUMBER, membersCount)
                    .where(RECORD.ID.eq(recordSave.id)).execute()
            }

            val enroll = Enroll()
            enroll.id = UUID.randomUUID().toString()
            enroll.recordid = recordSave.id
            enroll.status = false
            enroll.createtime = LocalDateTime.now()
            enroll.createuser = user.id.toLong()
            create.newRecord(ENROLL, enroll).insert()

            // return record data id
            recordSave.id
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            create.close()
        }
    }

    /**
     * submit the enroll
     */
    fun submit(recordId: Long): Boolean {
        val create = HikariManager.getDSLContext()
        return try {
            val enroll =
                create.select().from(ENROLL)
                    .where(ENROLL.RECORDID.eq(recordId), ENROLL.STATUS.isFalse)
                    .fetchSingle().into(Enroll::class.java)
            // Number of items affected
            val number = create.update(ENROLL).set(ENROLL.STATUS, true).where(ENROLL.ID.eq(enroll.id)).execute()
            number > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            create.close()
        }
    }

}