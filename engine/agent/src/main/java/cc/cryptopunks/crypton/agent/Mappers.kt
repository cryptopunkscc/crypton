package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.Transform
import cc.cryptopunks.crypton.TypedString
import cc.cryptopunks.crypton.flip
import cc.cryptopunks.crypton.register
import cc.cryptopunks.crypton.transform
import cc.cryptopunks.crypton.transform.YAML_STRATEGY
import cc.cryptopunks.crypton.transform.jsonToPrettyString
import cc.cryptopunks.crypton.transform.jsonToString
import cc.cryptopunks.crypton.transform.stringToYaml
import cc.cryptopunks.crypton.transform.typedStringToJson
import cc.cryptopunks.crypton.transformation
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer


internal val datagramToByteArray by lazy {
    val encodeJson by lazy { transform(jsonToString) }
    transformation<Any, ByteArray>("datagram") register {
        "${javaClass.name}:${encodeJson()}".toByteArray(Charsets.UTF_8)
    }
}


internal val byteArrayToDatagram by lazy {
    datagramToByteArray.flip() register {
        toString(Charsets.UTF_8).split(':', limit = 2).run {
            transform(typedStringToJson)(TypedString(
                type = Class.forName(first()) as Class<Any>,
                string = last()
            ))
        }
    }
}


internal val decodeStoryHeader: ByteArray.() -> StoryHeader by lazy {
    transform(byteArrayToStoryHeader)
}

internal val decodeDatagram: ByteArray.() -> Any by lazy { transform(byteArrayToDatagram) }
internal val encodeDatagram: Any.() -> ByteArray by lazy { transform(datagramToByteArray) }

internal val encodeYaml: Any.() -> String by lazy { transform(jsonToPrettyString) } // TODO
internal val decodeYaml: String.() -> Any by lazy { transform(stringToYaml) }

internal val encodeYamlWithType: Any.() -> ByteArray by lazy {
    { "!!${javaClass.name}\n${encodeYaml()}".toByteArray(Charsets.UTF_8) }
}

internal val decodeYamlWithType: ByteArray.() -> Any by lazy {
    {
        ByteArrayInputStream(this).bufferedReader(Charsets.UTF_8).run {
            transform(stringToYaml.copy(outputType = readLine()
                .apply { require(startsWith("!!")) }.drop(2)
                .let { Class.forName(it) as Class<Any> }
            ))(readText())
        }
    }
}


val byteArrayToStoryHeader by lazy {
    val decode = transform(typedStringToJson) as Transform<TypedString<StoryHeader>, StoryHeader>
    transformation<ByteArray, StoryHeader>(YAML_STRATEGY) register {
        TypedString(StoryHeader::class.java, toString(Charsets.UTF_8)).decode()
    }
}


//fun Short.toByteArray(): ByteArray = ByteBuffer
//    .allocate(Short.SIZE_BYTES)
//    .putShort(this)
//    .array()

fun Int.toByteArray(): ByteArray = ByteBuffer
    .allocate(Int.SIZE_BYTES)
    .putInt(this)
    .array()

fun Long.toByteArray(): ByteArray = ByteBuffer
    .allocate(Long.SIZE_BYTES)
    .putLong(this)
    .array()

fun ByteArray.toShort(): Long = ByteBuffer
    .allocate(Short.SIZE_BYTES)
    .put(copyOfRange(0, Short.SIZE_BYTES))
    .apply { flip() }
    .long

fun ByteArray.toLong(): Long = ByteBuffer
    .allocate(Long.SIZE_BYTES)
    .put(this)
    .apply { flip() }
    .long

fun ByteArray.toInt(): Long = ByteBuffer
    .allocate(Long.SIZE_BYTES)
    .put(this)
    .apply { flip() }
    .long
