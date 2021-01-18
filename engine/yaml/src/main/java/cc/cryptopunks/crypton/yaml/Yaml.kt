package cc.cryptopunks.crypton.yaml

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream
import java.io.Reader

fun parseYamlFromFile(filePath: String): Map<String, Any> =
    Yaml().load(File(filePath).reader())

fun InputStream.parseYaml(): Map<String, Any> =
    Yaml().load(this)

fun Reader.parseYaml(): Map<String, Any> =
    Yaml().load(this)

fun String.parseYaml(): Map<String, Any> =
    Yaml().load(this) ?: emptyMap()

fun Map<String, Any>.formatYaml(): String =
    Yaml().dump(this)
