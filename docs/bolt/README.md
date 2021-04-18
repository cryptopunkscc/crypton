# BOLT

A notes and investigation about the BOLT protocol.

## Overview

* Session initialization
    * message encryption
    * message authentication


```
    Noise_XK(s, rs):
       <- s
       ...
       -> e, es
       <- e, ee
       -> s, se
```

## Keywords

* `AEAD` - Authenticated Encryption with Associated Data
* `ECDH` - Elliptic-Curve Diffie-Hellman
* `Noise_XK` - the handshake.
    * Performing in 3 acts.
* `Noise_XK_secp256k1_ChaChaPoly_SHA256` - Lightning network variant of the Noise protocol.
* `TripleDH` - The incremental handshake created from mixed ECDH outputs.
* Keying material
    * `es`
    * `ee`
    * `se`
* `ck` - The chaining key.
    * The accumulated hash of all previous ECDH outputs. At the end of the handshake.
    * Is used to derive the encryption keys for Lightning messages.
* `h` - The handshake hash.
    * The accumulated hash of all handshake data that has been sent and received during the handshake process.
* The intermediate keys - are used to encrypt and decrypt the zero-length `AEAD` payloads at the end of each handshake
  message.
    * `temp_k1`
    * `temp_k2`
    * `temp_k3`
* `e` - public ephemeral key
    * For each session, a node MUST generate a new ephemeral key with strong cryptographic randomness.
* `s` - public static key (usually a nodeId)
    * `ls` - for local
    * `rs` - for remote

* `ECDH(k, rk)` - Performs an Elliptic-Curve Diffie-Hellman operation.
    * `k` - A valid `secp256k1` private key.
    * `rk` - A valid public key.
    * The returned value is the `SHA256` of the compressed format of the generated point.

* `HKDF(salt,ikm)` - A function defined in `RFC 5869`.
    * Evaluated with a zero-length `info` field.
    * All invocations of `HKDF` implicitly return 64 bytes of cryptographic randomness using the extract-and-expand
      component of the `HKDF`.

* `encryptWithAD(k, n, ad, plaintext)` - Outputs `encrypt(k, n, ad, plaintext)`
    * Where `encrypt` is an evaluation of `ChaCha20-Poly1305` (IETF variant)
      with the passed arguments, with nonce `n` encoded as 32 zero bits, followed by a *little-endian* 64-bit value.
    * Note: this follows the Noise Protocol convention, rather than our normal endian.

* `decryptWithAD(k, n, ad, ciphertext)` - outputs `decrypt(k, n, ad, ciphertext)`
    * Where `decrypt` is an evaluation of `ChaCha20-Poly1305` (IETF variant)
      with the passed arguments, with nonce `n` encoded as 32 zero bits, followed by a *little-endian* 64-bit value.

* `generateKey()` - generates and returns a fresh `secp256k1` keypair, where
    * `.pub` - which returns an abstract object representing the public key
    * `.priv` - which represents the private key used to generate the public key
    * `.serializeCompressed()`

* `a || b` denotes the concatenation of two byte strings `a` and `b`
