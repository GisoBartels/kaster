package app.kaster.common.login

import kotlinx.coroutines.flow.MutableStateFlow

interface LoginPersistence {

    val credentials: MutableStateFlow<Credentials?>

    fun clear() {
        credentials.value = null
    }

    data class Credentials(val username: String, val password: String)
}