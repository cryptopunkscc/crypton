package cc.cryptopunks.crypton.aesgcm

import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val KEY_TYPE = "AES"
private const val CIPHER_MODE = "AES/GCM/NoPadding"
private const val TAG_LENGTH_BIT = 128

private val aesCipher get() = Cipher.getInstance(CIPHER_MODE)

fun InputStream.withAes(
    iv: ByteArray,
    key: ByteArray,
    mode: Int = Cipher.ENCRYPT_MODE
): InputStream =
    CipherInputStream(this, getAesCipher(iv, key, mode))

fun OutputStream.withAes(
    iv: ByteArray,
    key: ByteArray,
    mode: Int = Cipher.ENCRYPT_MODE
): OutputStream =
    CipherOutputStream(this, getAesCipher(iv, key, mode))

private fun getAesCipher(
    iv: ByteArray,
    key: ByteArray,
    mode: Int = Cipher.ENCRYPT_MODE
): Cipher = aesCipher.apply {
    init(
        mode,
        SecretKeySpec(key, KEY_TYPE),
        GCMParameterSpec(TAG_LENGTH_BIT, iv)
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
