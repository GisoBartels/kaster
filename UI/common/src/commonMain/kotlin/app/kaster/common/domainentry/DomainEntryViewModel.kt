package app.kaster.common.domainentry

import app.kaster.common.domainentry.DomainEntryInput.Domain
import app.kaster.common.domainentry.DomainEntryInput.Save
import app.kaster.common.domainlist.DomainListPersistence
import app.kaster.common.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class DomainEntryViewModel(private val originalDomain: String?, private val persistence: DomainListPersistence) {
    private val domain = MutableStateFlow(originalDomain ?: "")

    val viewState = domain.map { DomainEntryViewState(domain.value) }

    fun onInput(input: DomainEntryInput) {
        when (input) {
            is Domain -> domain.value = input.value
            Save -> saveAndClose()
        }
    }

    private fun saveAndClose() {
        save()
        Navigator.goBack()
    }

    private fun save() {
        require(domain.value.isNotEmpty()) { "Saving empty domains not allowed" }
        if (domain.value != originalDomain)
            persistence.domainList.update {
                if (originalDomain == null)
                    it.add(domain.value)
                else
                    it.remove(originalDomain).add(domain.value)
            }
    }
}