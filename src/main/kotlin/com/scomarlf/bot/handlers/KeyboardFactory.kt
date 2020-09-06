package com.scomarlf.bot.handlers

import com.scomarlf.bot.utils.GroupType
import com.scomarlf.bot.utils.Page
import com.scomarlf.conf.BotConf
import com.scomarlf.conf.LangConf
import com.scomarlf.conf.LangItems
import com.scomarlf.generated.tables.pojos.Record
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.*


class KeyboardFactory {

    companion object {

        /**
         * 为开始创建按钮阵列
         *
         * @return
         */
        fun getStartKeyBorard(): ReplyKeyboardMarkup {
            // 添加群组类型键盘
            val keyboardMarkup = ReplyKeyboardMarkup()
            val keyboard: MutableList<KeyboardRow> = ArrayList()
            // 遍历群组类型，添加至键盘
            var row = KeyboardRow()
            for (type in GroupType.getTypes()) {
                row.add(type)
                if (row.size % 3 == 0 || GroupType.getTypes().indexOf(type) == GroupType.getTypes().size - 1) {
                    keyboard.add(row)
                    row = KeyboardRow()
                }
            }
            keyboardMarkup.keyboard = keyboard
            return keyboardMarkup
        }

        /**
         * 为审核创建按钮阵列
         *
         * @return
         */
        fun getApproveKeyBorard(enrollId: String,userId: Int): ReplyKeyboard {
            val inlineKeyboard = InlineKeyboardMarkup()
            val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()
            val rowInline: MutableList<InlineKeyboardButton> = ArrayList()
            rowInline.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.PASS))
                    .setCallbackData("approve:pass&$enrollId&$userId")
            )
            rowInline.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.REFUSE))
                    .setCallbackData("approve:refuse&$enrollId&$userId")
            )
            rowsInline.add(rowInline)
            inlineKeyboard.keyboard = rowsInline
            return inlineKeyboard
        }

        /**
         * 为频道列表创建按钮阵列
         *
         * @return
         */
        fun getChannelPageKeyBorard(typeOrKeyword: String, page: Page<Record>?): ReplyKeyboard {
            val inlineKeyboard = InlineKeyboardMarkup()
            val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()
            val rowInline: MutableList<InlineKeyboardButton> = ArrayList()
            if (page!!.hasPre)
                rowInline.add(
                    InlineKeyboardButton().setText(LangConf.get(LangItems.NEXT_PAGE))
                        .setCallbackData("page:$typeOrKeyword&${page.pageCurrent - 1}")
                )
            if (page.hasNext)
                rowInline.add(
                    InlineKeyboardButton().setText(LangConf.get(LangItems.PREVIOUS_PAGE))
                        .setCallbackData("page:$typeOrKeyword&${page.pageCurrent + 1}")
                )
            rowsInline.add(rowInline)
            inlineKeyboard.keyboard = rowsInline
            return inlineKeyboard
        }

        /**
         * 为推送频道创建查询按钮
         *
         * @return
         */
        fun getSelectKeyBorard(channelId: Long): ReplyKeyboard {
            val inlineKeyboard = InlineKeyboardMarkup()
            val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()
            val rowInline: MutableList<InlineKeyboardButton> = ArrayList()
            rowInline.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.SEARCH))
                    .setUrl("https://t.me/" + BotConf.USERNAME + "?start=" + channelId)
            )
            rowsInline.add(rowInline)
            inlineKeyboard.keyboard = rowsInline
            return inlineKeyboard
        }

        /**
         * 为收录申请创建编辑按钮
         *
         * @return
         */
        fun getEnrollKeyBorard(channelId: Long, userId: Int): ReplyKeyboard {
            val inlineKeyboard = InlineKeyboardMarkup()
            val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()
            val rowInline1: MutableList<InlineKeyboardButton> = ArrayList()
            rowInline1.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.EDIT_TITLE))
                    .setCallbackData("detail:title&$channelId&$userId")
            )
            rowInline1.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.EDIT_REMARK))
                    .setCallbackData("detail:remark&$channelId&$userId")
            )
            rowsInline.add(rowInline1)
            val rowInline2: MutableList<InlineKeyboardButton> = ArrayList()
            rowInline2.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.EDIT_TAG))
                    .setCallbackData("detail:tag&$channelId&$userId")
            )
            rowInline2.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.EDIT_CLASS))
                    .setCallbackData("detail:class&$channelId&$userId")
            )
            rowsInline.add(rowInline2)
            val rowInline3: MutableList<InlineKeyboardButton> = ArrayList()
            rowInline3.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.SUBMIT_ENROLL))
                    .setCallbackData("detail:submit&$channelId&$userId")
            )
            rowsInline.add(rowInline3)
            inlineKeyboard.keyboard = rowsInline
            return inlineKeyboard
        }

        /**
         * update record class
         */
        fun getClassKeyBorard(recordId: Long, userId: Int): ReplyKeyboard? {
            val inlineKeyboard = InlineKeyboardMarkup()
            val rowsInline: MutableList<List<InlineKeyboardButton>> = ArrayList()

            // 添加各个分类
            var rowInline: MutableList<InlineKeyboardButton> = ArrayList()
            for (type in GroupType.getDics!!) {
                rowInline.add(
                    InlineKeyboardButton().setText(type.getLabel())
                        .setCallbackData("class:${type.getId()}&$recordId&$userId")
                )
                if (rowInline.size % 3 == 0 || GroupType.getTypes()
                        .indexOf(type.getLabel()) == GroupType.getTypes().size - 1
                ) {
                    rowsInline.add(rowInline)
                    rowInline = ArrayList()
                }
            }
            // cancel button
            rowInline = ArrayList()
            rowInline.add(
                InlineKeyboardButton().setText(LangConf.get(LangItems.CANCEL))
                    .setCallbackData("class:cancel&$recordId&$userId")
            )
            rowsInline.add(rowInline)
            inlineKeyboard.keyboard = rowsInline
            return inlineKeyboard
        }

    }

}