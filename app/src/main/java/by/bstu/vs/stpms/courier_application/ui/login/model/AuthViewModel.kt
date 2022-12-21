package by.bstu.vs.stpms.courier_application.ui.login.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bstu.vs.stpms.courier_application.model.database.entity.User
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import by.bstu.vs.stpms.courier_application.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel: ViewModel() {

    var autoLoginUser: Result<User>? by mutableStateOf(null)
    var user: User? by mutableStateOf(null)
    var login by mutableStateOf("")
    var password by mutableStateOf("")

    private val userService = UserRepository

    fun tryAutoLogin() {

        //todo use withContext if main thread is needed
        //fixme not working offline autologin
        viewModelScope.launch(Dispatchers.IO) {
            autoLoginUser = userService.tryAutoLogin()
        }
    }
    suspend fun login(): Result<User> = withContext(Dispatchers.IO) {
        userService.login(login, password)
    }
}