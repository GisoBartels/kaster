package app.kaster.common

import app.kaster.common.KasterInput.CounterDecrease
import app.kaster.common.KasterInput.CounterIncrease
import app.kaster.common.KasterInput.Domain
import app.kaster.common.KasterInput.Password
import app.kaster.common.KasterInput.Scope
import app.kaster.common.KasterInput.Type
import app.kaster.common.KasterInput.Username
import app.kaster.core.Kaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class KasterViewModel {
    private val mutex = Mutex()

    private val inputState = MutableStateFlow(KasterViewState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val sitePassword: Flow<String> = inputState
        .transformLatest {
            mutex.withLock {
                emit("")
                if (it.password.isNotEmpty()) {
                    delay(200)
                    emit(it.generatePassword())
                }
            }
        }
        .flowOn(Dispatchers.Default)

    val viewState: Flow<KasterViewState> = inputState.combine(sitePassword) { inputState, password ->
        inputState.copy(result = password)
    }

    private fun KasterViewState.generatePassword(): String = Kaster.generatePassword(
        username,
        password,
        domain,
        counter,
        type,
        scope
    )

    fun onInput(input: KasterInput) {
        inputState.value = when (input) {
            is Username -> inputState.value.copy(username = input.value)
            is Password -> inputState.value.copy(password = input.value)
            is Domain -> inputState.value.copy(domain = input.value)
            is Scope -> inputState.value.copy(scope = input.scope)
            CounterIncrease -> inputState.value.copy(counter = inputState.value.counter + 1)
            CounterDecrease -> inputState.value.copy(counter = inputState.value.counter - 1)
            is Type -> inputState.value.copy(type = input.value)
        }
    }
}
