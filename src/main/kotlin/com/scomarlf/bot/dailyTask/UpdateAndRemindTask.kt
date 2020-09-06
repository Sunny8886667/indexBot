package com.scomarlf.bot.dailyTask

class UpdateAndRemindTask(callback: Callback?) : DailyTask {

    interface Callback {
        fun dailyStatistics()
    }

    private var callback: Callback? = null

    init {
        this.callback = callback
    }

    override fun execute() {
        callback!!.dailyStatistics()
    }
}