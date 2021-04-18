package cc.cryptopunks.playground.ecdh

import java.nio.ByteBuffer
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.KeyAgreement

// https://neilmadden.blog/2016/05/20/ephemeral-elliptic-curve-diffie-hellman-key-agreement-in-java/

fun ecdhExample() {
    val console = System.console()
    // Generate ephemeral ECDH keypair
    val kpg = KeyPairGenerator.getInstance("EC")
    kpg.initialize(256)
    val kp = kpg.generateKeyPair()
    val ourPk: ByteArray = kp.public.encoded

    // Display our public key
    console.printf("Public Key: %s%n", printHexBinary(ourPk))

    // Read other's public key:
    val otherPk: ByteArray = parseHexBinary(console.readLine("Other PK: "))
    val kf = KeyFactory.getInstance("EC")
    val pkSpec = X509EncodedKeySpec(otherPk)
    val otherPublicKey = kf.generatePublic(pkSpec)

    // Perform key agreement
    val ka = KeyAgreement.getInstance("ECDH")
    ka.init(kp.private)
    ka.doPhase(otherPublicKey, true)

    // Read shared secret
    val sharedSecret = ka.generateSecret()
    console.printf("Shared secret: %s%n", printHexBinary(sharedSecret))

    // Derive a key from the shared secret and both public keys
    val hash = MessageDigest.getInstance("SHA-256")
    hash.update(sharedSecret)
    // Simple deterministic ordering
    val keys = listOf(ByteBuffer.wrap(ourPk), ByteBuffer.wrap(otherPk)).sorted()
    Collections.sort(keys)
    hash.update(keys[0])
    hash.update(keys[1])
    val derivedKey = hash.digest()
    console.printf("Final key: %s%n", printHexBinary(derivedKey))
}

fun parseHexBinary(string: String): ByteArray {
    TODO()
}

fun printHexBinary(bytes: ByteArray) {
    TODO()
}