package com.scomarlf.bot.utils

import com.scomarlf.generated.tables.pojos.Dictionary
import java.util.*


class GroupType {

    companion object {
        private var dictionaries: List<Dictionary>? = null
        private var types: MutableList<String> = ArrayList()

        fun init(dics: List<Dictionary>?) {
            dictionaries = dics
            types = ArrayList()
            for (item in dictionaries!!)
                types.add(item.getLabel())
        }

        fun getName(id: String?): String {
            for (item in dictionaries!!)
                if (item.getId().equals(id))
                    return item.getLabel()
            return ""
        }

        fun getType(type: String?): String {
            for (item in dictionaries!!)
                if (item.getLabel().equals(type))
                    return item.getId()
            return ""
        }

        val getDics: List<Dictionary>? get() = dictionaries

        fun getTypes(): List<String> {
            return types
        }
    }

}