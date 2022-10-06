package app.kaster.core

import com.password4j.HashingFunction
import com.password4j.Password
import com.password4j.ScryptFunction
import java.security.MessageDigest

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
