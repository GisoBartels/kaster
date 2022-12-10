package app.kaster.common.domainentry

import app.kaster.common.domainentry.DomainEntryInput.*
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.kaster.common.domainlist.DomainListPersistence
import app.kaster.common.login.LoginPersistence
import app.kaster.common.navigation.Navigator
import app.kaster.core.Kaster
import app.kaster.core.Kaster.PasswordType
import app.kaster.core.Kaster.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class DomainEntryViewModel(
    private val originalDomain: String?,
    private val domainListPersistence: DomainListPersistence,
    loginPersistence: LoginPersistence
) {
    private val username = loginPersistence.loadUsername()
    private val masterPassword = loginPersistence.loadMasterPassword()
    private val domain = MutableStateFlow(originalDomain ?: "")

    @OptIn(FlowPreview::class)
    private val password: Flow<GeneratedPassword> = domain
        .debounce(250)
        .map { domain -> generatePassword(domain) }
        .flowOn(Dispatchers.Default)
        .onStart { emit(GeneratedPassword.NotEnoughData) }

    val viewState = combine(domain, password) { domain, password ->
        DomainEntryViewState(domain, password)
    }

    fun onInput(input: DomainEntryInput) {
        when (input) {
            is Domain -> domain.value = input.value
            Save -> saveAndClose()
            Dismiss -> Navigator.goBack()
        }
    }

    private fun generatePassword(domain: String) =
        if (domain.isEmpty()) {
            GeneratedPassword.NotEnoughData
        } else {
            GeneratedPassword.Result(
                Kaster.generatePassword(
                    username = username,
                    masterPassword = masterPassword,
                    domain = domain,
                    counter = 1,
                    type = PasswordType.Maximum,
                    scope = Scope.Authentication
                )
            )
        }

    private fun saveAndClose() {
        save()
        Navigator.goBack()
    }

    private fun save() {
        require(domain.value.isNotEmpty()) { "Saving empty domains not allowed" }
        if (domain.value != originalDomain)
            domainListPersistence.domainList.update {
                if (originalDomain == null)
                    it.add(domain.value)
                else
                    it.remove(originalDomain).add(domain.value)
            }
    }
}