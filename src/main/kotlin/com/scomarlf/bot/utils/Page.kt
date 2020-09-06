package com.scomarlf.bot.utils

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class Page<T> {

    var records: List<T>? = null
    var total: Long = 0
    var size: Int = 0
    var pageCurrent: Long = 0
    var pageSize: Long = 0
    var hasPre = false
    var hasNext = false

    companion object {

        val defaultSize = 30

        fun init(records: List<Any>, total: Long, size: Int, pageCurrent: Long): Page<Any> {
            val result = Page<Any>()
            result.records = records
            result.total = total
            result.size = size
            result.pageSize = Math.ceil(total.toFloat() / size.toDouble()).toLong()
            result.pageCurrent = pageCurrent
            // check has per or next page
            val prePage = pageCurrent - 1
            val nextPage = pageCurrent + 1
            if (prePage > 0)
                result.hasPre = true
            if (nextPage <= result.pageSize)
                result.hasNext

            return result
        }

    }

}