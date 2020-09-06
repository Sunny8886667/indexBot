package com.scomarlf.service

import com.scomarlf.bot.utils.Page
import com.scomarlf.generated.tables.Record.RECORD
import com.scomarlf.generated.tables.VRecord.V_RECORD
import com.scomarlf.generated.tables.pojos.Record
import com.scomarlf.generated.tables.pojos.VRecord
import com.scomarlf.generated.tables.records.RecordRecord
import com.scomarlf.utils.HikariManager
import org.jooq.Condition
import org.jooq.TableField
import java.time.LocalDateTime

class RecordService {

    /**
     * search page list by type
     */
    fun getPageByType(type: String, pageCurrent: Long): Page<com.scomarlf.generated.tables.pojos.Record>? {
        val create = HikariManager.getDSLContext()
        return try {
            val total = create.select().from(RECORD).where(RECORD.TYPE.eq(type)).count()

            val records = create.select().from(RECORD)
                .where(RECORD.TYPE.eq(type))
                .orderBy(RECORD.MEMBERNUMBER.desc())
                .seek(pageCurrent * Page.defaultSize).limit(Page.defaultSize)
                .fetchInto(com.scomarlf.generated.tables.pojos.Record::class.java)

            val page = Page.init(records, total.toLong(), Page.defaultSize, pageCurrent);
            page as Page<com.scomarlf.generated.tables.pojos.Record>
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            create.close()
        }
    }

    /**
     * search page list by keyword
     */
    fun getPageByKeyword(keyword: String, pageCurrent: Long): Page<com.scomarlf.generated.tables.pojos.Record>? {
        val create = HikariManager.getDSLContext()
        return try {
            val total = create.select().from(RECORD)
                .where(RECORD.TITLE.contains(keyword), RECORD.TAG.contains(keyword)).count()

            val records = create.select().from(RECORD)
                .where(RECORD.TITLE.contains(keyword), RECORD.TAG.contains(keyword))
                .orderBy(RECORD.MEMBERNUMBER.desc())
                .seek(pageCurrent * Page.defaultSize).limit(Page.defaultSize)
                .fetchInto(com.scomarlf.generated.tables.pojos.Record::class.java)

            val page = Page.init(records, total.toLong(), Page.defaultSize, pageCurrent);
            page as Page<com.scomarlf.generated.tables.pojos.Record>
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
    fun getViewById(id: Long): VRecord? {
        val create = HikariManager.getDSLContext()
        return try {
            val record = create.select().from(V_RECORD).where(V_RECORD.ID.eq(id)).fetchOne().into(VRecord::class.java)
            record
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            create.close()
        }
    }

    /**
     * get data by id or username
     */
    fun getByIdOrUsername(idOrUsername: String):Record?{
        val create = HikariManager.getDSLContext()
        return try {
            // query conditions
            val condition: Condition =
                try {
                    RECORD.ID.eq(idOrUsername.toLong())
                } catch (e: java.lang.Exception) {
                    RECORD.USERNAME.eq(idOrUsername)
                }
            // search data
            val record = create.select().from(RECORD)
                .where(condition).orderBy(RECORD.CREATETIME.desc())
                .fetchAny().into(Record::class.java)
            record
        } catch (e: Exception) {
            null
        } finally {
            create.close()
        }
    }

    /**
     * get data by id
     */
    fun getById(id: Long): Record? {
        val create = HikariManager.getDSLContext()
        return try {
            val record = create.select().from(RECORD).where(RECORD.ID.eq(id)).fetchOne().into(Record::class.java)
            record
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            create.close()
        }
    }

    fun updateById(id: Long, field: TableField<RecordRecord, String>, value: String, userId: Int): Boolean {
        val create = HikariManager.getDSLContext()
        return try {
            val count = create.update(RECORD)
                .set(field, value)
                .set(RECORD.UPDATETIME, LocalDateTime.now())
                .set(RECORD.UPDATEUSER, userId.toLong())
                .where(RECORD.ID.eq(id)).execute()
            count > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            create.close()
        }
    }

}