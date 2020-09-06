package com.scomarlf.bot.handlers

import com.scomarlf.bot.dailyTask.DailyTaskExecutor
import com.scomarlf.bot.utils.OperState

class Handlers {
    companion object {
        // waiting for operation status
        var operStates: HashMap<Long, OperState>? = null

        // daily task
        var dailyTaskExecutor: DailyTaskExecutor? = null

        // main response handler
        var mainResponseHandler: MainResponseHandler? = null

        // response handler for enroll
        var enrollResponseHandler: EnrollResponseHandler? = null

        // response handler for approve
        var approveResponseHandler: ApproveResponseHandler? = null

        // response handler for reply message
        var msgResponseHandler: MsgResponseHandler? = null
    }
}