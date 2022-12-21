package by.bstu.vs.stpms.courier_application.ui.courier.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bstu.vs.stpms.courier_application.model.database.contract.RoleType
import by.bstu.vs.stpms.courier_application.model.database.entity.Role
import by.bstu.vs.stpms.courier_application.model.database.entity.Stats
import by.bstu.vs.stpms.courier_application.model.database.entity.User
import by.bstu.vs.stpms.courier_application.model.repository.OrderRepository
import by.bstu.vs.stpms.courier_application.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.Period
import java.util.*
import java.util.stream.Collectors

class CourierProfileViewModel : ViewModel() {

    var user: User? by mutableStateOf(null)
    var stats: Stats? by mutableStateOf(null)
    var deliveredMap: Map<String, Float>? by mutableStateOf(null)
    var isRefreshing by mutableStateOf(false)

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.logout()
        }
    }

    fun refresh() {
        isRefreshing = true
        user = null
        stats = null

        viewModelScope.launch(Dispatchers.IO) {
            val userLoad = async {
                delay(500)
                val result = UserRepository.getCurrentUser()
                if (result.isSuccessful) user = result.data

            }
            val statsLoad = async {
                val result = UserRepository.getUserStats()
                if (result.isSuccessful) stats = result.data
            }
            val deliveredLoad = async {
                getDelivered()
            }

            userLoad.await()
            statsLoad.await()
            deliveredLoad.await()

            isRefreshing = false
        }
    }

    fun initialLoad() {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                val result = UserRepository.getCurrentUser()
                if (result.isSuccessful) user = result.data
            }
            launch {
                val result = UserRepository.getUserStats()
                if (result.isSuccessful) stats = result.data
            }
            launch {
                getDelivered()
            }
        }
    }

    fun isVerified(roles: Set<Role>?): Boolean = roles?.let {
        val roleTypeSet = roles.stream().map { role -> RoleType.valueOf(role.name) }
            .collect(Collectors.toSet())

        roleTypeSet.contains(RoleType.ROLE_COURIER)
    } ?: false

    private suspend fun getDelivered() {
        val result = OrderRepository.getDeliveredOrders()
        if (result.isSuccessful) {
            val orders = result.data
            var now = Instant.now()
            now = now.minusSeconds(now.epochSecond % (60 * 60 * 24))
            val monthAgo = now.minus(Period.ofDays(30))
            val instantList = mutableListOf<Instant>()
            var tempInstant = monthAgo
            while (tempInstant.isBefore(now)) {
                instantList.add(Instant.from(tempInstant))
                tempInstant = tempInstant.plus(Period.ofDays(1))
            }
            val formatter = SimpleDateFormat("dd/MM", Locale.getDefault())
            deliveredMap = instantList.mapIndexed { index, day ->
                formatter.format(Timestamp.from(day)) to (orders?.count {
                    val deliveredInstant = it.deliveredAt.toInstant()
                    if (instantList.size > index + 1)
                        deliveredInstant.isAfter(instantList[index]) && deliveredInstant.isBefore(instantList[index + 1])
                    else
                        deliveredInstant.isAfter(instantList[index])
                } ?: 0).toFloat()
            }.toMap()
        }
    }

}