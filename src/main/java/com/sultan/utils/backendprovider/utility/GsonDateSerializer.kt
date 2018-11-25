package com.sultan.utils.backendprovider.utility

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.*

/**
 * Created by Sultan Ahmed on 22/04/2018.
 */

class GsonDateSerializer : JsonSerializer<Date> {

    override fun serialize(date: Date?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
        return if (date == null) null else JsonPrimitive(date.time)
    }
}
