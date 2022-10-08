package app.kaster.core

import app.kaster.core.Kaster.PasswordType
import app.kaster.core.Kaster.Scope
import kotlin.test.Test
import kotlin.test.assertEquals

class PasswordGenerationTest {

    @Test
    fun testMax() = testPassword(
        type = PasswordType.Maximum,
        expectedPassword = "X@CPl2wOm0RgIWu#lC7/"
    )

    @Test
    fun testLong() = testPassword(
        type = PasswordType.Long,
        expectedPassword = "Kozm3[FikkPidu"
    )

    @Test
    fun testMedium() = testPassword(
        type = PasswordType.Medium,
        expectedPassword = "KozMob2?"
    )

    @Test
    fun testShort() = testPassword(
        type = PasswordType.Short,
        expectedPassword = "Koz3"
    )

    @Test
    fun testBasic() = testPassword(
        type = PasswordType.Basic,
        expectedPassword = "XCb3WQh7"
    )

    @Test
    fun testPin() = testPassword(
        type = PasswordType.PIN,
        expectedPassword = "8333"
    )

    @Test
    fun testName() = testPassword(
        type = PasswordType.Name,
        expectedPassword = "kozmobibi"
    )

    @Test
    fun testPhrase() = testPassword(
        type = PasswordType.Phrase,
        expectedPassword = "kozm bib kadduto pic"
    )

    @Test
    fun testCounter2() = testPassword(
        counter = 2,
        expectedPassword = "w1;yBPwvkliaDo^nKQfY"
    )

    @Test
    fun testCounter3() = testPassword(
        counter = 3,
        expectedPassword = "fFUVayDPbuEtsCT*B18="
    )

    @Test
    fun testIdentificationScope() = testPassword(
        type = PasswordType.Name,
        scope = Scope.Identification,
        expectedPassword = "rayjopera"
    )

    @Test
    fun testRecoveryScope() = testPassword(
        type = PasswordType.Phrase,
        scope = Scope.Recovery,
        expectedPassword = "gaj seqtabajo bewi"
    )

    @Test
    fun testOtherUsername() = testPassword(
        username = "Flexo",
        expectedPassword = "p9_YW9BScrAbLLo7Ivg9"
    )
    @Test
    fun testOtherMasterPassword() = testPassword(
        masterPassword = "I’m the brewery!",
        expectedPassword = "g9*RlE3CilUmDL\$#tyhX"
    )
    @Test
    fun testOtherDomain() = testPassword(
        domain = "slurm.corp",
        expectedPassword = "P2]w(IkqhjQf2L@lrRdw"
    )

    private fun testPassword(
        username: String = "Bender",
        masterPassword: String = "BiteMyShinyMetalAss!",
        domain: String = "benderbrau.robot",
        counter: Int = 1,
        type: PasswordType = PasswordType.Maximum,
        scope: Scope = Scope.Authentication,
        expectedPassword: String
    ) {
        val actualPassword = Kaster.generatePassword(username, masterPassword, domain, counter, type, scope)
        assertEquals(expectedPassword, actualPassword)
    }
}