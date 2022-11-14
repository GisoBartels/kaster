package app.kaster.common.domainentry

import kotlinx.coroutines.flow.flowOf

class DomainEntryViewModel(domain: String?) {

    val viewState = flowOf(DomainEntryViewState(domain ?: ""))

}