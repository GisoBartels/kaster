package app.passwordkaster.core

import app.passwordkaster.core.Kaster.PasswordType

internal fun passwordTemplates(type: PasswordType): List<PasswordTemplate> = when (type) {
    PasswordType.Name -> templates("cvccvcvcv")
    PasswordType.Phrase -> templates("cvcc cvc cvccvcv cvc", "cvc cvccvcvcv cvcv", "cv cvccv cvc cvcvccv")
    PasswordType.Maximum -> templates("anoxxxxxxxxxxxxxxxxx", "axxxxxxxxxxxxxxxxxno")
    PasswordType.Long -> templates(
        "CvcvnoCvcvCvcv",
        "CvcvCvcvnoCvcv",
        "CvcvCvcvCvcvno",
        "CvccnoCvcvCvcv",
        "CvccCvcvnoCvcv",
        "CvccCvcvCvcvno",
        "CvcvnoCvccCvcv",
        "CvcvCvccnoCvcv",
        "CvcvCvccCvcvno",
        "CvcvnoCvcvCvcc",
        "CvcvCvcvnoCvcc",
        "CvcvCvcvCvccno",
        "CvccnoCvccCvcv",
        "CvccCvccnoCvcv",
        "CvccCvccCvcvno",
        "CvcvnoCvccCvcc",
        "CvcvCvccnoCvcc",
        "CvcvCvccCvccno",
        "CvccnoCvcvCvcc",
        "CvccCvcvnoCvcc",
        "CvccCvcvCvccno",
    )

    PasswordType.Medium -> templates("CvcnoCvc", "CvcCvcno")
    PasswordType.Basic -> templates("aaanaaan", "aannaaan", "aaannaaa")
    PasswordType.Short -> templates("Cvcn")
    PasswordType.PIN -> templates("nnnn")
}

private fun templates(vararg templates: String): List<PasswordTemplate> =
    templates.map { PasswordTemplate(it) }
