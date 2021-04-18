package cc.cryptopunks.crypton.transform

import cc.cryptopunks.crypton.TypedByteArray
import cc.cryptopunks.crypton.flip
import cc.cryptopunks.crypton.transformation


const val YAML_STRATEGY = "yaml"
val yamlToString = transformation<Any, String>(YAML_STRATEGY)
val stringToYaml = jsonToString.flip()
val yamlToByteArray = transformation<Any, ByteArray>(YAML_STRATEGY)
val byteArrayToYaml = yamlToByteArray.flip()
val typedByteArrayToYaml = transformation<TypedByteArray<Any>, Any>(YAML_STRATEGY)
