package com.scomarlf.service

import com.scomarlf.service.StatisticsService
import com.scomarlf.service.UserService

class Instantiate {

    companion object {

        var userService = UserService()
        var statisticsService = StatisticsService()
        var recordService = RecordService()
        var approveService = ApproveService()
        var enrollService = EnrollService()
        var dictionaryService = DictionaryService()
        var ecologyService = EcologyService()

    }

    init {

    }

}