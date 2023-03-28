package app.passwordkaster.core

import app.passwordkaster.core.Kaster.Scope
import kotlin.test.Test
import kotlin.test.assertEquals

class SiteKeyTest {

    @Test
    fun testSiteKeySalt() {
        val salt = SiteKey.salt("benderbrau.robot", 1, Scope.Authentication)
        assertEquals(
            "c7d46f4660b17847e90e1d7c92b0aacd330b264573cf6ce18fac960967df2214",
            sha256(salt).toHexString()
        )
    }

    @Test
    fun verifySiteKeyAlgorithm() {
        val masterKey = MasterKey("Bender", "BiteMyShinyMetalAss!")
        val siteKey = SiteKey(masterKey, "benderbrau.robot", 1, Scope.Authentication)

        assertEquals(
            "6710f3341efe6f12374e83407466812dad670683b7618818e71ab6ea70aa21e7",
            sha256(siteKey.key).toHexString()
        )
    }
}
