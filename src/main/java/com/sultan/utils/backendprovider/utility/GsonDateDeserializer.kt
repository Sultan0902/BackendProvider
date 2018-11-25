package com.sultan.utils.backendprovider.utility

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Sultan Ahmed on 22/04/2018.
 */

class GsonDateDeserializer : JsonDeserializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(
            json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date? {
        try {
            return Date(json.asJsonPrimitive.asLong)
        } catch (ex: Exception) {
            val dateString = json.asString
            val dateFormat = SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH)
            try {
                return dateFormat.parse(dateString)
            } catch (ignored: ParseException) {
                return null
            }

        }

    }
}

