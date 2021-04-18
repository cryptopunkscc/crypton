package cc.cryptopunks.crypton.gson

import cc.cryptopunks.crypton.register
import cc.cryptopunks.crypton.transform.jsonToByteArray
import cc.cryptopunks.crypton.transform.jsonToPrettyString
import cc.cryptopunks.crypton.transform.jsonToString
import cc.cryptopunks.crypton.transform.typedByteArrayToJson
import cc.cryptopunks.crypton.transform.typedStringToJson

fun initGsonTransformations() {

    jsonToByteArray register { gson.toJson(this).toByteArray(Charsets.UTF_8) }
    typedByteArrayToJson register { gson.fromJson(bytes.toString(Charsets.UTF_8), type) }

    jsonToString register { gson.toJson(this) }
    jsonToPrettyString register { prettyGson.toJson(this) }
    typedStringToJson register {
        try {
            gson.fromJson(string, type)
        } catch (e: Throwable) {
            throw e
        }
    }
}
