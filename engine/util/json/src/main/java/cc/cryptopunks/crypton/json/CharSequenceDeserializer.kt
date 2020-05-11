package cc.cryptopunks.crypton.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object CharSequenceDeserializer : JsonDeserializer<CharSequence> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CharSequence = StringBuffer(json.asJsonPrimitive.asString)
}

