package cc.cryptopunks.crypton.json

import com.google.gson.GsonBuilder
import kotlin.reflect.KClass

private val gson = GsonBuilder()
    .setExclusionStrategies(DelegatePropertyExclusionStrategy)
    .registerTypeAdapter(CharSequence::class.java, CharSequenceDeserializer)
    .create()

private val prettyGson = GsonBuilder()
    .setPrettyPrinting()
    .setExclusionStrategies(DelegatePropertyExclusionStrategy)
    .registerTypeAdapter(CharSequence::class.java, CharSequenceDeserializer)
    .create()

fun <T : Any> T.formatJson(): String = gson.toJson(this)
fun <T : Any> T.formatJsonPretty(): String = prettyGson.toJson(this)
fun <T : Any> String.parseJson(toClass: KClass<T>): T = gson.fromJson(this, toClass.java)
inline fun <reified T : Any> String.parseJson(): T = parseJson(T::class)
