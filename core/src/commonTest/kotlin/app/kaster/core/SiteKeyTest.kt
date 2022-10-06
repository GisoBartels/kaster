package app.kaster.core

import kotlin.test.Test
import kotlin.test.assertEquals

class SiteKeyTest {

    @Test
    fun testSiteKeySalt() {
        val salt = SiteKey.salt("benderbrau.robot", 1, SiteKey.Scope.Authentication)
        assertEquals(
            "C7D46F4660B17847E90E1D7C92B0AACD330B264573CF6CE18FAC960967DF2214",
            sha256(salt).toHexString()
        )
    }

    @Test
    fun verifySiteKeyAlgorithm() {
        val masterKey = MasterKey("Bender", "BiteMyShinyMetalAss!")
        val siteKey = SiteKey(masterKey, "benderbrau.robot", 1, SiteKey.Scope.Authentication)

        assertEquals(
            "6710F3341EFE6F12374E83407466812DAD670683B7618818E71AB6EA70AA21E7",
            sha256(siteKey.key).toHexString()
        )
    }
}
