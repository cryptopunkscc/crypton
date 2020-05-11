package cc.cryptopunks.crypton.json

import cc.cryptopunks.crypton.context.Route
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

object RouteAdapterFactory : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
        if (!Route::class.java.isAssignableFrom(type!!.rawType)) return null
        return RouteTypeAdapter as TypeAdapter<T>
    }

    object RouteTypeAdapter : TypeAdapter<Route>() {
        private val gson = GsonBuilder()
            .setExclusionStrategies(DelegatePropertyExclusionStrategy)
            .create()

        override fun write(out: JsonWriter, value: Route) {
            out
                .beginObject()
                .name("type")
                .value(value::class.java.name.split("$")[1])
                .name("value")
                .jsonValue(gson.toJson(value))
                .endObject()
        }

        override fun read(`in`: JsonReader): Route {
            var result: Route
            `in`.run {
                beginObject()
                nextName()
                val type = "cc.cryptopunks.crypton.context.Route\$${nextString()}"
                nextName()
                result = Class.forName(type).run {
                    val instance = kotlin.objectInstance as? Route
                    val parsed = gson.fromJson<Route>(`in`, this)
                    instance ?: parsed
                }
                endObject()
            }
            return result
        }
    }
}
