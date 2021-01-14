package cc.cryptopunks.crypton.fs.util

import java.io.File
import java.io.InputStream

fun idFromFile(path: String) = sha256FromFile(path).toHex()

fun File.readId() = inputStream().readId()

fun String.calculateId() = sha256().toHex()

fun InputStream.readId() = sha256().toHex()
