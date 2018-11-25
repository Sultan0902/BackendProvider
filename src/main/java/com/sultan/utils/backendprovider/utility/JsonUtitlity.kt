package com.sultan.utils.backendprovider.utility

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type
import java.util.*

/**
 * Created by Sultan Ahmed on 22/04/2018.
 */

class JsonUtitlity {

    fun toJson(obj: Any): String {
        val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, GsonDateSerializer())
                .create()

        return gson.toJson(obj)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, GsonDateDeserializer())
                .create()

        return gson.fromJson(json, clazz)
    }

    fun <T> fromJson(gson: Gson, json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }

    fun <T> fromJson(json: String, type: Type): T? {
        val gson = Gson()

        return gson.fromJson<T>(json, type)
    }

    fun <T> fromTypedJson(json: String, typeToken: Type): T? {
        val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, GsonDateDeserializer())
                .create()

        return gson.fromJson<T>(json, typeToken)
    }
}
