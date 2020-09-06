package com.scomarlf.conf

interface CommandConf {
    companion object {
        // public command
        const val START = "/start"
        const val LIST = "/list"
        const val ENROLL = "/enroll"
        const val HELP = "/help"
        const val CANCEL = "/cancel"

        // manage command
        const val INIT = "/init"
        const val PUSH = "/push"
        const val APPROVE = "/approve"
        const val ADMIN = "/admin"
        const val ROLE = "/role"
        const val DAILY = "/daily"

        // link command
        // make a group to approve the enroll request
        const val APPROVE_INIT = "/approve_init"
        // after the enroll is passed, the announcement will be posted to the channel
        const val BULLETIN_INIT = "/bulletin_init"
        const val APPROVE_RESET = "/approve_reset"
        const val BULLETIN_RESET = "/bulletin_reset"

    }
}
