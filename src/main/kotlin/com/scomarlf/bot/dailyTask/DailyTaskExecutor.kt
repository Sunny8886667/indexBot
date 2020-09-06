package com.scomarlf.bot.dailyTask

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

class DailyTaskExecutor(dailyTask: DailyTask?) {

    private var executorService: ScheduledExecutorService? = null
    private var dailyTask: DailyTask? = null

    init {
        executorService = Executors.newScheduledThreadPool(1)
        this.dailyTask = dailyTask
    }

    fun startExecutionAt(targetHour: Int, targetMin: Int, targetSec: Int) {
        val taskWrapper = Runnable {
            dailyTask!!.execute()
            startExecutionAt(targetHour, targetMin, targetSec)
        }
        val delay = computeNextDelay(targetHour, targetMin, targetSec)
        executorService!!.schedule(taskWrapper, delay, TimeUnit.SECONDS)
    }

    private fun computeNextDelay(targetHour: Int, targetMin: Int, targetSec: Int): Long {
        val localNow = LocalDateTime.now()
        val currentZone = ZoneId.systemDefault()
        val zonedNow = ZonedDateTime.of(localNow, currentZone)
        var zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec)
        if (zonedNow.compareTo(zonedNextTarget) >= 0) zonedNextTarget = zonedNextTarget.plusDays(1)
        val duration = Duration.between(zonedNow, zonedNextTarget)
        return duration.seconds
    }

    fun stop() {
        executorService!!.shutdown()
        try {
            executorService!!.awaitTermination(1, TimeUnit.DAYS)
        } catch (ex: InterruptedException) {
            Logger.getLogger(DailyTaskExecutor::class.java.name).log(Level.SEVERE, null, ex)
        }
    }

}