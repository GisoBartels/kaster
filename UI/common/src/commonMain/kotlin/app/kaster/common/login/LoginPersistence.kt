package app.kaster.common.login

import kotlinx.coroutines.flow.MutableStateFlow

interface LoginPersistence {

    val credentials: MutableStateFlow<Credentials?>

    data class Credentials(val username: String, val password: String)
}