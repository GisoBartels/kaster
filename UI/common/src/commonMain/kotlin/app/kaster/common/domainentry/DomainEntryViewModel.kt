package app.kaster.common.domainentry

import app.kaster.common.domainentry.DomainEntryInput.*
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.kaster.common.login.LoginPersistence
import app.kaster.common.navigation.Navigator
import app.kaster.core.Kaster
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
    private val domainEntryPersistence: DomainEntryPersistence,
    loginPersistence: LoginPersistence,
    private val passwordGenerationContext: CoroutineContext = Dispatchers.Default
) {
    private val username = loginPersistence.loadUsername()
    private val masterPassword = loginPersistence.loadMasterPassword()
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
        DomainEntryViewState(domainEntry, password)
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
            Dismiss -> Navigator.goBack()
        }
    }

    private fun updateCounterIfValid(newValue: Int) {
        if (newValue > 0) {
            workingCopy.update { it.copy(counter = newValue) }
        }
    }

    private fun DomainEntry.generatePassword() =
        GeneratedPassword.Result(Kaster.generatePassword(username, masterPassword, domain, counter, type, scope))

    private fun saveAndClose() {
        save()
        Navigator.goBack()
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