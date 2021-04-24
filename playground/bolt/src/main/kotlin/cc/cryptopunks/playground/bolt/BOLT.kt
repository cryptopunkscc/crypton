package cc.cryptopunks.playground.bolt

import cc.cryptopunks.playground.bolt.BoltHandshake.KeyPair

private const val PROTOCOL_NAME = "Noise_XK_secp256k1_ChaChaPoly_SHA256"
private const val PROLOGUE = "lightning"
private const val PROTOCOL_VERSION: Byte = 0
private val zero = byteArrayOf()
private val emptyKeyPair = KeyPair(byteArrayOf(), byteArrayOf())

/**
 * The implementation of Lightning Network's handshake specified in the transport layer
 * [documentation](https://github.com/lightningnetwork/lightning-rfc/blob/50b7391a6ef5310021c2a6378334e65e04e46876/08-transport.md)
 */
object BoltHandshake {

    /**
     * The representation of BOLT handshake state
     */
    class State(
        crypto: Crypto,
        val s: KeyPair,
        var e: KeyPair = emptyKeyPair,
        var ck: ByteArray = byteArrayOf(),
        var h: ByteArray = byteArrayOf(),
        var temp_k1: ByteArray = byteArrayOf(),
        var temp_k2: ByteArray = byteArrayOf(),
        var temp_k3: ByteArray = byteArrayOf()
    ) : Crypto by crypto

    class KeyPair(
        val pub: ByteArray,
        val priv: ByteArray
    )

    interface Crypto {
        /**
         * Generates and returns a fresh secp256k1 keypair
         */
        fun generateKey(): KeyPair

        /**
         * Calculate SHA 256 digest
         */
        fun sha256(input: ByteArray): ByteArray

        /**
         * Serialize in Bitcoin's compressed format.
         */
        fun ByteArray.serializeCompressed(): ByteArray

        /**
         * ECDH(k, rk): performs an Elliptic-Curve Diffie-Hellman operation
         * @param privateEphemeralKey A valid secp256k1 private key,
         * @param publicStaticRemoteKey A valid public key
         * @return The returned value is the SHA256 of the compressed format of the generated point.
         */
        fun ECDH(
            privateEphemeralKey: ByteArray,
            publicStaticRemoteKey: ByteArray
        ): ByteArray

        fun HKDF(
            salt: ByteArray,
            ikm: ByteArray
        ): Pair<ByteArray, ByteArray>

        /**
         * Encrypt plain text using AEAD_CHACHA20_POLY1305 algorithm (IETF variant).
         * Note: this follows the Noise Protocol convention, rather than our normal endian.
         *
         * @param k A 256-bit key.
         * @param n A value used to calculate a 96-bit nonce.
         * @param ad An additional data.
         * @param plaintext - Data for encryption.
         * @return Encrypted cipher text.
         */
        fun encryptWithAD(
            k: ByteArray,
            n: Long,
            ad: ByteArray,
            plaintext: ByteArray
        ): ByteArray

        /**
         * Decrypt cipher text using AEAD_CHACHA20_POLY1305 algorithm (IETF variant).
         * Note: this follows the Noise Protocol convention, rather than our normal endian.
         *
         * @param k A 256-bit key
         * @param n A value used to calculate a 96-bit nonce
         * @param ad An additional data
         * @param ciphertext - An encrypted data.
         * @return Decrypted plain text.
         */
        fun decryptWithAD(
            k: ByteArray,
            n: Long,
            ad: ByteArray,
            ciphertext: ByteArray
        ): ByteArray
    }
}

// Initialization

/**
 * Handshake state initialization.
 * @param rs The static public key.
 * * initiator have to provide responder's static public key.
 * * responder have to provide its own local static public key.
 */
fun BoltHandshake.State.init(rs: ByteArray) {
    ck = sha256(PROTOCOL_NAME.toByteArray())
    h = sha256(ck + PROLOGUE.toByteArray())
    h = sha256(h + rs.serializeCompressed())
}

// Act 1

/**
 * First action of act 1
 * @receiver The [BoltHandshake.State] initialized using [init] with responder's static public key
 * @param rs The responder public static key
 * @return The message for the responder
 */
fun BoltHandshake.State.initiatorAct1(rs: ByteArray): ByteArray {

    e = generateKey()

    // The newly generated ephemeral key is accumulated into the running handshake digest.
    h = sha256(h + e.pub.serializeCompressed())

    // The initiator performs an ECDH between its newly generated ephemeral key and the remote node's static public key.
    val es = ECDH(e.priv, rs)

    // A new temporary encryption key is generated, which is used to...
    HKDF(ck, es).run {
        ck = first
        temp_k1 = second
    }

    // Generate the authenticating MAC.
    val c = encryptWithAD(temp_k1, 0, h, zero)

    // Finally, the generated ciphertext is accumulated into the authenticating handshake digest.
    h = sha256(h + c)

    // At last, building a message for the responder.
    return byteArrayOf(0) + e.pub.serializeCompressed() + c
}


/**
 * Second action of act 1
 * @receiver The [BoltHandshake.State] initialized using [init] with responder's static public key.
 * @param m The first message of the handshake received from initiator.
 * @return The initiator's ephemeral public key.
 */
fun BoltHandshake.State.responderAct1(m: ByteArray): ByteArray {

    require(m.size == 50) {
        "Invalid responder act 1 message size, required 50 but received: ${m.size}"
    }
    require(m.first() == PROTOCOL_VERSION) {
        "Invalid protocol version, required $PROTOCOL_VERSION but received ${m.first()}"
    }

    // Read the initiator public ephemeral key
    val re = m.copyOfRange(1, 34)

    // Read the authentication MAC
    val c = m.copyOfRange(34, 50)

    // The responder accumulates the initiator's ephemeral key into the authenticating handshake digest.
    h = sha256(h + re.serializeCompressed())

    // The responder performs an ECDH between its static private key and the initiator's ephemeral public key.
    val es = ECDH(s.priv, re)

    // A new temporary encryption key is generated, which will shortly be used to check the authenticating MAC.
    HKDF(ck, es).run {
        ck = first
        temp_k1 = second
    }

    // If the MAC check in this operation fails, then the initiator MUST terminate the connection without any further messages.
    val p = decryptWithAD(temp_k1, 0, h, c)

    h = sha256(h + c)

    return re
}

// Act 2

/**
 * First action of act 2
 * @receiver The [BoltHandshake.State] of the responder, right after act 1.
 * @param re The ephemeral key of the initiator, which was received during Act One.
 * @return The message for the initiator
 */
fun BoltHandshake.State.responderAct2(re: ByteArray): ByteArray {

    e = generateKey()

    // The newly generated ephemeral key is accumulated into the running handshake digest.
    h = sha256(h + e.pub.serializeCompressed())

    // re is the ephemeral key of the initiator, which was received during Act One
    val ee = ECDH(e.priv, re)

    // A new temporary encryption key is generated, which is used to...
    HKDF(ck, ee).run {
        ck = first
        temp_k2 = second
    }

    // Generate the authenticating MAC.
    val c = encryptWithAD(temp_k2, 0, h, zero)

    // Finally, the generated ciphertext is accumulated into the authenticating handshake digest.
    h = sha256(h + c)

    // At last, building a message for the responder.
    return byteArrayOf(0) + e.pub.serializeCompressed() + c
}

/**
 * Second action of act 2
 * @receiver The [BoltHandshake.State] of the initiator, right after act 1.
 * @param m The message from the responder generated during act 2
 * @return The responder's ephemeral public key.
 */
fun BoltHandshake.State.initiatorAct2(m: ByteArray): ByteArray {

    require(m.size == 50) {
        "Invalid initiator act 2 message size, required 50 but received: ${m.size}"
    }
    require(m.first() == PROTOCOL_VERSION) {
        "Invalid protocol version, required $PROTOCOL_VERSION but received ${m.first()}"
    }

    // Read the responder public ephemeral key
    val re = m.copyOfRange(1, 34)

    // Read the authentication MAC
    val c = m.copyOfRange(34, 50)

    // The initiator accumulates the responder's ephemeral key into the authenticating handshake digest.
    h = sha256(h + re.serializeCompressed())

    // The responder performs an ECDH between its static private key and the initiator's ephemeral public key.
    val ee = ECDH(s.priv, re)

    // A new temporary encryption key is generated, which will shortly be used to check the authenticating MAC.
    HKDF(ck, ee).run {
        ck = first
        temp_k2 = second
    }

    // If the MAC check in this operation fails, then the initiator MUST terminate the connection without any further messages.
    val p = decryptWithAD(temp_k2, 0, h, c)

    h = sha256(h + c)

    return re
}

// Act 3

/**
 * First action of act 3.
 * @receiver The [BoltHandshake.State] of the initiator, right after act 2.
 * @param re The ephemeral public key of the responder
 * @return The [Pair] of [KeyPair] and [ByteArray] where result nr:
 * * [KeyPair.priv] - sk, is the key to be used by the initiator to encrypt messages to the responder.
 * * [KeyPair.pub] - rk, is the key to be used by the initiator to decrypt messages sent by the responder.
 * * [ByteArray] - is the exactly 66 bytes of message for the responder
 */
fun BoltHandshake.State.initiatorAct3(re: ByteArray): Pair<KeyPair, ByteArray> {

    // where s is the static public key of the initiator
    val c = encryptWithAD(temp_k2, 1, h, s.pub.serializeCompressed())

    h = sha256(h + c)

    // re is the ephemeral public key of the responder
    val se = ECDH(s.priv, re)

    // The final intermediate shared secret is mixed into the running chaining key.
    HKDF(ck, se).run {
        ck = first
        temp_k3 = second
    }

    val t = encryptWithAD(temp_k3, 0, h, zero)

    val (
        sk, // the key to be used by the initiator to encrypt messages to the responder
        rk, // the key to be used by the initiator to decrypt messages sent by the responder
    ) = HKDF(ck, zero)

    // The sending and receiving nonces are initialized to 0.
    // rn = 0, sn = 0

    return Pair(
        first = KeyPair(
            priv = sk,
            pub = rk,
        ),
        second = byteArrayOf(0) + c + t
    )
}

/**
 * Second action of act 3.
 * @receiver The [BoltHandshake.State] of the responder, right after act 2.
 * @param re The ephemeral public key of the responder
 * @return The [List<ByteArray>] of [ByteArray] where result nr:
 * * [KeyPair.priv] - sk, is the key to be used by the responder to encrypt messages to the initiator.
 * * [KeyPair.pub] - rk, is the key to be used by the responder to decrypt the messages sent by the initiator.
 */
fun BoltHandshake.State.responderAct3(m: ByteArray): KeyPair {

    require(m.size == 66) {
        "Invalid responder act 3 message size, required 66 but received: ${m.size}"
    }
    require(m.first() == PROTOCOL_VERSION) {
        "Invalid protocol version, required $PROTOCOL_VERSION but received ${m.first()}"
    }

    val c = m.copyOfRange(1, 50)

    val t = m.copyOfRange(50, 66)

    // At this point, the responder has recovered the static public key of the initiator.
    // If the MAC check in this operation fails, then the responder MUST terminate the connection without any further messages.
    val rs = decryptWithAD(temp_k2, 1, h, c)

    h = sha256(h + c)

    // e is the responder's original ephemeral key
    val se = ECDH(e.priv, rs)

    HKDF(ck, se).run {
        ck = first
        temp_k3 = second
    }

    // If the MAC check in this operation fails, then the responder MUST terminate the connection without any further messages.
    val p = decryptWithAD(temp_k3, 0, h, t)

    val (
        rk, // the key to be used by the responder to decrypt the messages sent by the initiator.
        sk, // the key to be used by the responder to encrypt messages to the initiator.
    ) = HKDF(ck, zero)

    return KeyPair(
        pub = rk,
        priv = sk,
    )
}
