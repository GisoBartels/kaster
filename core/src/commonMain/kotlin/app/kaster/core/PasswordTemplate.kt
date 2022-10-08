package app.kaster.core

internal data class PasswordTemplate(val characters: List<CharacterClass>) {
    constructor(characters: String) : this(characters.map { c -> CharacterClass.of(c) })
}

internal enum class CharacterClass(val code: Char, val characters: String) {
    VowelsUpperCase('V', "AEIOU"),
    ConsonantsUpperCase('C', "BCDFGHJKLMNPQRSTVWXYZ"),
    Vowels('v', "aeiou"),
    Consonants('c', "bcdfghjklmnpqrstvwxyz"),
    AlphabeticUpperCase('A', "AEIOUBCDFGHJKLMNPQRSTVWXYZ"),
    Alphabetic('a', "AEIOUaeiouBCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz"),
    Numeric('n', "0123456789"),
    Other('o', """@&%?,=[]_:-+*$#!'^~;()/."""),
    UnionSet('x', """AEIOUaeiouBCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz0123456789!@#$%^&*()"""),
    Space(' ', " ");

    companion object {
        fun of(code: Char): CharacterClass =
            CharacterClass.values().firstOrNull { it.code == code } ?: error("Unknown code: $code")
    }
}