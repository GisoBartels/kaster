package app.kaster.core

class SiteKey(masterKey: MasterKey, domain: String, counter: Int, scope: Scope) {
    val key: ByteArray = hmacSha256(masterKey.key, salt(domain, counter, scope))

    enum class Scope(internal val id: String) {
        Authentication("com.lyndir.masterpassword"),
        Identification("com.lyndir.masterpassword.login"),
        Recovery("com.lyndir.masterpassword.answer"),
    }

    companion object {
        internal fun salt(domain: String, counter: Int, scope: Scope): ByteArray =
            scope.id.toByteArray() + domain.length.toBigEndianBytes() + domain.toByteArray() + counter.toBigEndianBytes()
    }
}
