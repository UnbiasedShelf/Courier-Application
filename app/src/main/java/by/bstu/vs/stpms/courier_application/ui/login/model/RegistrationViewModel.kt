package by.bstu.vs.stpms.courier_application.ui.login.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType
import by.bstu.vs.stpms.courier_application.model.network.dto.UserDto
import by.bstu.vs.stpms.courier_application.model.repository.UserRepository
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import by.bstu.vs.stpms.courier_application.ui.util.formatPhone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel : ViewModel() {

    var firstName by mutableStateOf("")
    var secondName by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var courierType by mutableStateOf(CourierType.NotCourier)

    private val userService = UserRepository

    suspend fun signUp() = withContext(Dispatchers.IO) {
        val userDto = UserDto(
            firstName,
            secondName,
            email,
            phone.formatPhone(),
            password,
            confirmPassword,
            courierType.name
        )

        userService.signUp(userDto)
    }
}