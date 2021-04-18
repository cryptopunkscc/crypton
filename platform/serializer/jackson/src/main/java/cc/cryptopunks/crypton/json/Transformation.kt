package cc.cryptopunks.crypton.json

import cc.cryptopunks.crypton.register
import cc.cryptopunks.crypton.transform.jsonToByteArray
import cc.cryptopunks.crypton.transform.jsonToPrettyString
import cc.cryptopunks.crypton.transform.jsonToString
import cc.cryptopunks.crypton.transform.typedByteArrayToJson
import cc.cryptopunks.crypton.transform.typedByteArrayToYaml
import cc.cryptopunks.crypton.transform.typedStringToJson
import cc.cryptopunks.crypton.transform.yamlToByteArray
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule


private val yaml: ObjectMapper = YAMLMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .registerModule(kotlinModule())

private val json: ObjectMapper = JsonMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .registerModule(kotlinModule())

private val jsonPretty = json.writerWithDefaultPrettyPrinter()

fun initJacksonTransformations() {

    jsonToByteArray register { json.writeValueAsBytes(this) }
    typedByteArrayToJson register { json.readValue(bytes, type) }

    jsonToString register { json.writeValueAsString(this) }
    jsonToPrettyString register { jsonPretty.writeValueAsString(this) }
    typedStringToJson register { json.readValue(string, type) }

    yamlToByteArray register { yaml.writeValueAsBytes(this) }
    typedByteArrayToYaml register { yaml.readValue(bytes, type) }

}
