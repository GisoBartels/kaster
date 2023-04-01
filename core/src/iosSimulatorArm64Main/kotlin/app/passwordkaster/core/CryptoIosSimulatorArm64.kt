@file:OptIn(ExperimentalUnsignedTypes::class)

package app.passwordkaster.core

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pin
import kotlinx.cinterop.ptr
import libsodium.crypto_auth_hmacsha256_BYTES
import libsodium.crypto_auth_hmacsha256_final
import libsodium.crypto_auth_hmacsha256_init
import libsodium.crypto_auth_hmacsha256_state
import libsodium.crypto_auth_hmacsha256_update
import libsodium.crypto_hash_sha256
import libsodium.crypto_hash_sha256_BYTES
import libsodium.crypto_pwhash_scryptsalsa208sha256_ll

internal actual fun scrypt(password: String, salt: ByteArray): ByteArray {
    val pinnedPassword = password.encodeToByteArray().asUByteArray().pin()
    val pinnedSalt = salt.asUByteArray().pin()

    val pinnedBuffer = UByteArray(ScryptParameters.DERIVED_KEY_LENGTH).pin()

    crypto_pwhash_scryptsalsa208sha256_ll(
        pinnedPassword.addressOf(0),
        pinnedPassword.get().size.convert(),
        pinnedSalt.addressOf(0),
        pinnedSalt.get().size.convert(),
        ScryptParameters.WORK_FACTOR.convert(),
        ScryptParameters.RESOURCES.convert(),
        ScryptParameters.PARALLELIZATION.convert(),
        pinnedBuffer.addressOf(0),
        ScryptParameters.DERIVED_KEY_LENGTH.convert()
    )

    val result = pinnedBuffer.get().toByteArray()

    pinnedPassword.unpin()
    pinnedSalt.unpin()
    pinnedBuffer.unpin()

    return result
}

internal actual fun sha256(message: ByteArray): ByteArray {
    val pinnedBuffer = UByteArray(crypto_hash_sha256_BYTES.toInt()).pin()
    val pinnedMessage = message.asUByteArray().pin()

    crypto_hash_sha256(
        pinnedBuffer.addressOf(0),
        pinnedMessage.addressOf(0),
        pinnedMessage.get().size.convert()
    )

    val result = pinnedBuffer.get().toByteArray()

    pinnedMessage.unpin()
    pinnedBuffer.unpin()

    return result
}

internal actual fun hmacSha256(key: ByteArray, message: ByteArray): ByteArray {
    val pinnedBuffer = UByteArray(crypto_auth_hmacsha256_BYTES.toInt()).pin()
    val pinnedMessage = message.asUByteArray().pin()
    val pinnedKey = key.asUByteArray().pin()

    memScoped {
        val state = alloc<crypto_auth_hmacsha256_state>()
        crypto_auth_hmacsha256_init(
            state = state.ptr,
            key = pinnedKey.addressOf(0), keylen = pinnedKey.get().size.convert()
        )
        crypto_auth_hmacsha256_update(
            state = state.ptr,
            `in` = pinnedMessage.addressOf(0), inlen = pinnedMessage.get().size.convert()
        )
        crypto_auth_hmacsha256_final(
            state = state.ptr,
            out = pinnedBuffer.addressOf(0)
        )
    }

    val result = pinnedBuffer.get().toByteArray()

    pinnedKey.unpin()
    pinnedMessage.unpin()
    pinnedBuffer.unpin()

    return result
}
