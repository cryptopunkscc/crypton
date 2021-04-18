package cc.cryptopunks.crypton.transform

import cc.cryptopunks.crypton.TypedByteArray
import cc.cryptopunks.crypton.TypedString
import cc.cryptopunks.crypton.flip
import cc.cryptopunks.crypton.transformation

const val JSON_STRATEGY = "json"
const val PRETTY_JSON_STRATEGY = "json"

val jsonToString = transformation<Any, String>(JSON_STRATEGY)
val jsonToPrettyString = transformation<Any, String>(PRETTY_JSON_STRATEGY)
val stringToJson = jsonToString.flip()
val typedStringToJson = transformation<TypedString<Any>, Any>(JSON_STRATEGY)


val jsonToByteArray = transformation<Any, ByteArray>(JSON_STRATEGY)
val byteArrayToJson = jsonToByteArray.flip()
val typedByteArrayToJson = transformation<TypedByteArray<Any>, Any>(JSON_STRATEGY)
