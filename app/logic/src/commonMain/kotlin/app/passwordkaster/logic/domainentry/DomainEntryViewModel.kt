package app.passwordkaster.logic.domainentry

import app.passwordkaster.logic.domainentry.DomainEntryInput.*
import app.passwordkaster.logic.domainentry.DomainEntryViewState.GeneratedPassword
import app.passwordkaster.logic.login.LoginInteractor
import app.passwordkaster.logic.login.LoginInteractor.LoginState
import app.passwordkaster.core.Kaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class DomainEntryViewModel(
    private val originalDomain: String?,
    private val onCloseEntry: () -> Unit,
    private val loginInteractor: LoginInteractor,
    private val domainEntryPersistence: DomainEntryPersistence,
    private val passwordGenerationContext: CoroutineContext = Dispatchers.Default
) {
    private val originalEntry =
        originalDomain?.let { domainEntryPersistence.entries.value.find { it.domain == originalDomain } }
    private val workingCopy = MutableStateFlow(originalEntry ?: DomainEntry(originalDomain ?: ""))

    @OptIn(ExperimentalCoroutinesApi::class)
    private val password: Flow<GeneratedPassword> = workingCopy
        .transformLatest { domainEntry ->
            if (domainEntry.domain.isEmpty()) {
                emit(GeneratedPassword.NotEnoughData)
            } else {
                emit(GeneratedPassword.Generating)
                delay(250) // debounce during multiple inputs
                emit(withContext(passwordGenerationContext) { domainEntry.generatePassword() })
            }
        }

    val viewState = combine(workingCopy, password) { domainEntry, password ->
        DomainEntryViewState(domainEntry, password, saveEnabled = domainEntry.domain.isNotBlank())
    }

    fun onInput(input: DomainEntryInput) {
        when (input) {
            is Domain -> workingCopy.update { it.copy(domain = input.value) }
            is Scope -> workingCopy.update { it.copy(scope = input.value) }
            is Counter -> updateCounterIfValid(input.value)
            IncreaseCounter -> updateCounterIfValid(workingCopy.value.counter + 1)
            DecreaseCounter -> updateCounterIfValid(workingCopy.value.counter - 1)
            is Type -> workingCopy.update { it.copy(type = input.value) }
            Save -> saveAndClose()
            Dismiss -> onCloseEntry()
            Delete -> deleteAndClose()
        }
    }

    private fun updateCounterIfValid(newValue: Int) {
        if (newValue > 0) {
            workingCopy.update { it.copy(counter = newValue) }
        }
    }

    private fun DomainEntry.generatePassword(): GeneratedPassword.Result {
        val (username, masterPassword) = (loginInteractor.loginState.value as LoginState.LoggedIn).credentials
        return GeneratedPassword.Result(Kaster.generatePassword(username, masterPassword, domain, counter, type, scope))
    }

    private fun saveAndClose() {
        save()
        onCloseEntry()
    }

    private fun deleteAndClose() {
        originalDomain?.let { domainEntryPersistence.remove(it) }
        onCloseEntry()
    }

    private fun save() {
        workingCopy.value.let { changedEntry ->
            require(changedEntry.domain.isNotEmpty()) { "Saving empty domains not allowed" }
            if (changedEntry != originalEntry)
                domainEntryPersistence.entries.update {
                    it.removeAll { entry -> entry.domain == originalDomain }
                        .add(changedEntry)
                }
        }
    }
}