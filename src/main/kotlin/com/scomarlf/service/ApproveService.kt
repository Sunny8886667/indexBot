package com.scomarlf.service

import com.scomarlf.generated.tables.Approve.APPROVE
import com.scomarlf.generated.tables.Record.RECORD
import com.scomarlf.generated.tables.VEnroll.V_ENROLL
import com.scomarlf.generated.tables.pojos.Approve
import com.scomarlf.generated.tables.pojos.VEnroll
import com.scomarlf.utils.HikariManager
import java.time.LocalDateTime
import java.util.*

class ApproveService {

    fun getApproveNext(): VEnroll? {
        val create = HikariManager.getDSLContext()
        return try {
            // search data
            val enroll = create.select().from(V_ENROLL)
                .where(V_ENROLL.APPROVESTATUS.isNull)
                .orderBy(V_ENROLL.CREATETIME)
                .fetchOne().into(VEnroll::class.java)
            enroll
        } catch (e: Exception) {
            null
        } finally {
            create.close()
        }
    }

    fun approve(enrollId: String, isPass: Boolean, userId: Long): Boolean {
        val create = HikariManager.getDSLContext()
        return try {
            // search data
            val enroll = create.select().from(V_ENROLL)
                .where(V_ENROLL.ID.eq(enrollId))
                .fetchOne().into(VEnroll::class.java)
            // insert approve record
            val approve = Approve()
            approve.id = UUID.randomUUID().toString()
            approve.enrollid = enrollId
            approve.status = isPass
            approve.createuser = userId
            approve.createtime = LocalDateTime.now()
            create.newRecord(APPROVE,approve).insert()
            // if pass,make record status to true
            create.update(RECORD).set(RECORD.STATUS, isPass).where(RECORD.ID.eq(enroll.recordid)).execute()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            create.close()
        }
    }

}