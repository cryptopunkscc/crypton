package cc.cryptopunks.crypton.gson

import com.google.gson.GsonBuilder

internal val gson = GsonBuilder()
    .setExclusionStrategies(DelegatePropertyExclusionStrategy)
    .registerTypeAdapter(CharSequence::class.java, CharSequenceDeserializer)
    .create()

internal val prettyGson = GsonBuilder()
    .setPrettyPrinting()
    .setExclusionStrategies(DelegatePropertyExclusionStrategy)
    .registerTypeAdapter(CharSequence::class.java, CharSequenceDeserializer)
    .create()

fun <T : Any> T.formatJson(): String = gson.toJson(this)
fun <T : Any> T.formatJsonPretty(): String = prettyGson.toJson(this)
fun <T : Any> String.parseJson(toClass: Class<T>): T = gson.fromJson(this, toClass)
inline fun <reified T : Any> String.parseJson(): T = parseJson(T::class.java)
