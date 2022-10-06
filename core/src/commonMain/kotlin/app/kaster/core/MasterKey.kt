package app.kaster.core

class MasterKey(username: String, masterPassword: String, scope: Scope) {

    val key = scrypt(masterPassword, salt(username, scope))

    enum class Scope(internal val id: String) {
        Authentication("com.lyndir.masterpassword"),
        Identification("com.lyndir.masterpassword.login"),
        Recovery("com.lyndir.masterpassword.answer"),
    }

    companion object {
        internal fun salt(username: String, scope: Scope): ByteArray =
            scope.id.toByteArray() + username.length.toBigEndianBytes() + username.toByteArray()
    }
}
