package com.scomarlf.service

import com.scomarlf.generated.tables.Record.RECORD
import com.scomarlf.generated.tables.User.USER
import com.scomarlf.generated.tables.VEnroll.V_ENROLL
import com.scomarlf.generated.tables.pojos.User
import com.scomarlf.generated.tables.pojos.VRecord
import com.scomarlf.utils.HikariManager
import java.time.LocalDateTime

class StatisticsService {

    /**
     * daily statistic for channel and users
     */
    fun daily(): IntArray {
        val create = HikariManager.getDSLContext()
        return try {
            val dailyNewUser: Int =
                create.select().from(USER).where(USER.CREATETIME.startsWith(LocalDateTime.now())).count()
            val dailyActiveUser: Int =
                create.select().from(USER).where(USER.UPDATETIME.startsWith(LocalDateTime.now())).count()
            val userCount: Int = create.select().from(USER).count()
            val channelCount: Int = create.select().from(RECORD).count()
            val result: IntArray = intArrayOf(dailyNewUser, dailyActiveUser, userCount, channelCount)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            IntArray(0)
        } finally {
            create.close()
        }
    }


    fun getWaitApproveCount(): Int {
        val create = HikariManager.getDSLContext()
        return try {
            val count = create.select().from(V_ENROLL)
                .where(V_ENROLL.STATUS.eq(true), V_ENROLL.APPROVESTATUS.isNull)
                .count()
            count
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            create.close()
        }
    }

}