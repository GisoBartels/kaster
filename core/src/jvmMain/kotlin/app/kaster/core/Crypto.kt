package app.kaster.core

import com.password4j.HashingFunction
import com.password4j.Password
import com.password4j.ScryptFunction
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal actual fun scrypt(password: String, salt: ByteArray): ByteArray =
    Password
        .hash(password)
        .addSalt(salt.decodeToString())
        .with(scryptConfiguredForMP)
        .bytes

private val scryptConfiguredForMP: HashingFunction
    get() = ScryptFunction.getInstance(
        ScryptParameters.WORK_FACTOR,
        ScryptParameters.RESOURCES,
        ScryptParameters.PARALLELIZATION,
        ScryptParameters.DERIVED_KEY_LENGTH
    )


internal actual fun sha256(message: ByteArray): ByteArray =
    MessageDigest.getInstance("SHA-256").digest(message)

private const val HmacSHA256 = "HmacSHA256"

internal actual fun hmacSha256(key: ByteArray, message: ByteArray): ByteArray =
    with(Mac.getInstance(HmacSHA256)) {
        init(SecretKeySpec(key, HmacSHA256))
        doFinal(message)
    }
