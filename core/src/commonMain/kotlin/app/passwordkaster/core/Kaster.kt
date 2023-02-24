package app.passwordkaster.core

object Kaster {
    fun generatePassword(
        username: String,
        masterPassword: String,
        domain: String,
        counter: Int,
        type: PasswordType,
        scope: Scope
    ): String {
        val masterKey = MasterKey(username, masterPassword)
        val siteKey = SiteKey(masterKey, domain, counter, scope)
        return SitePassword.generate(siteKey, type)
    }

    enum class PasswordType {
        Maximum,
        Long,
        Medium,
        Short,
        Basic,
        PIN,
        Name,
        Phrase,
    }

    enum class Scope {
        Authentication,
        Identification,
        Recovery,
    }
}


