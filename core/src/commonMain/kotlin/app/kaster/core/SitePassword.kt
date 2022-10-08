package app.kaster.core

import app.kaster.core.Kaster.PasswordType

object SitePassword {
    fun generate(siteKey: SiteKey, type: PasswordType): String {
        val template = passwordTemplates(type).modGet(siteKey.key[0])

        return buildString(template.characters.size) {
            template.characters.forEachIndexed { i, charClass ->
                append(charClass.characters.modGet(siteKey.key[i + 1]))
            }
        }
    }

    private fun <T> List<T>.modGet(index: Byte): T = get(index unsignedMod size)
    private fun String.modGet(index: Byte): Char = get(index unsignedMod length)

    private infix fun Byte.unsignedMod(divisor: Int): Int = toUByte().toInt() % divisor
}
