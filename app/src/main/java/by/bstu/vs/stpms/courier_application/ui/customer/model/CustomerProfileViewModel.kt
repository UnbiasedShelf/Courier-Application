package by.bstu.vs.stpms.courier_application.ui.customer.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bstu.vs.stpms.courier_application.model.database.contract.RoleType
import by.bstu.vs.stpms.courier_application.model.database.entity.Role
import by.bstu.vs.stpms.courier_application.model.database.entity.Stats
import by.bstu.vs.stpms.courier_application.model.database.entity.User
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType
import by.bstu.vs.stpms.courier_application.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.stream.Collectors

class CustomerProfileViewModel: ViewModel() {

    var user: User? by mutableStateOf(null)
    var isRefreshing by mutableStateOf(false)

    //fixme crash without internet connection
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.logout()
        }
    }

    fun refresh() {
        isRefreshing = true
        user = null

        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            val result = UserRepository.getCurrentUser()
            if (result.isSuccessful) user = result.data

            isRefreshing = false
        }
    }

    fun initialLoad() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = UserRepository.getCurrentUser()
            if (result.isSuccessful) user = result.data
        }
    }

    fun changeType(newType: CourierType) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = UserRepository.changeType(newType)
            if (result.isSuccessful) user = User().apply {
                this.firstName = user?.firstName
                this.secondName = user?.secondName
                this.email = user?.email
                this.phone = user?.phone
                this.password = user?.password
                this.courierType = newType
            }
            //todo else
        }
    }

}