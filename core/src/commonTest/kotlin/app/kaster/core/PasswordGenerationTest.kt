package app.kaster.core

import kotlin.test.Test
import kotlin.test.assertEquals

class PasswordGenerationTest {

    @Test
    fun verifyGenerationAlgorithms() {
        testData.forEach { (input, expectedPassword) ->
            val actualPassword =
                Kaster.generatePassword(input.username, input.secret, input.domain, input.counter, input.type)
            assertEquals(expectedPassword, actualPassword)
        }
    }

    data class TestInput(
        val username: String,
        val secret: String,
        val domain: String,
        val counter: Int,
        val type: PasswordType
    )

    private val testData = listOf(
        TestInput(
            username = "Bender",
            secret = "BiteMyShinyMetalAss!",
            domain = "benderbrau.robot",
            counter = 1,
            type = PasswordType.Maximum
        ) to "X@CPl2wOm0RgIWu#lC7/",
        TestInput(
            username = "Bender",
            secret = "BiteMyShinyMetalAss!",
            domain = "benderbrau.robot",
            counter = 1,
            type = PasswordType.Long
        ) to "Kozm3[FikkPidu",
        TestInput(
            username = "Bender",
            secret = "BiteMyShinyMetalAss!",
            domain = "benderbrau.robot",
            counter = 1,
            type = PasswordType.Medium
        ) to "KozMob2?",
        TestInput(
            username = "Bender",
            secret = "BiteMyShinyMetalAss!",
            domain = "benderbrau.robot",
            counter = 1,
            type = PasswordType.Short
        ) to "Koz3",
        TestInput(
            username = "Bender",
            secret = "BiteMyShinyMetalAss!",
            domain = "benderbrau.robot",
            counter = 1,
            type = PasswordType.Basic
        ) to "XCb3WQh7",
        TestInput(
            username = "Bender",
            secret = "BiteMyShinyMetalAss!",
            domain = "benderbrau.robot",
            counter = 1,
            type = PasswordType.PIN
        ) to "8333",
        TestInput(
            username = "Bender",
            secret = "BiteMyShinyMetalAss!",
            domain = "benderbrau.robot",
            counter = 1,
            type = PasswordType.Name
        ) to "kozmobibi",
        TestInput(
            username = "Bender",
            secret = "BiteMyShinyMetalAss!",
            domain = "benderbrau.robot",
            counter = 1,
            type = PasswordType.Phrase
        ) to "kozm bib kadduto pic",
    )
}