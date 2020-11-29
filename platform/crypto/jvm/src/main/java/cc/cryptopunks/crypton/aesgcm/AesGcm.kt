package cc.cryptopunks.crypton.aesgcm

import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val KEY_TYPE = "AES"
private const val CIPHER_MODE = "AES/GCM/NoPadding"

private val aesCipher get() = Cipher.getInstance(CIPHER_MODE)

fun InputStream.aesEncrypt(
    iv: ByteArray,
    key: ByteArray
): InputStream =
    CipherInputStream(this, getAesCipher(key, iv))

fun OutputStream.aesDecrypt(
    iv: ByteArray,
    key: ByteArray
): OutputStream =
    CipherOutputStream(this, getAesCipher(key, iv))

private fun getAesCipher(
    iv: ByteArray,
    key: ByteArray
): Cipher = aesCipher.apply {
    init(
        Cipher.ENCRYPT_MODE,
        SecretKeySpec(key, KEY_TYPE),
        IvParameterSpec(iv)
    )
}

// TODO deal with mime types
//val VALID_IMAGE_EXTENSIONS = listOf("webp", "jpeg", "jpg", "png", "jpe")
//val VALID_CRYPTO_EXTENSIONS = listOf("pgp", "gpg")
//
//fun String.getRelevantExtension(
//    ignoreCryptoExtension: Boolean = false
//): String = this
//    .getFileName()
//    .getExtensions()
//    .run {
//        if (!ignoreCryptoExtension) this
//        else dropLastWhile { it in VALID_CRYPTO_EXTENSIONS }
//    }
//    .last()
//
//
//fun String.getFileName() = split("/").last()
//
//fun String.getExtensions() = split(".")
