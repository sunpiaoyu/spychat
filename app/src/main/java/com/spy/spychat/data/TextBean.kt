package com.spy.spychat.data

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser

data class TextBean(
    val role: String,
    val content: String
) {
    override fun toString(): String {
        return "{\"role\": \"$role\", \"content\": \"$content\"}"
    }
}

fun String.parseNoHeaderJArray(): MutableList<TextBean> {
    val jsonArray: JsonArray = JsonParser.parseString(this).asJsonArray
    val gson = Gson()
    val textBeanList = mutableListOf<TextBean>()
    jsonArray.onEach {
        val text = gson.fromJson(it, TextBean::class.java)
        textBeanList.add(text)
    }
    return textBeanList
}