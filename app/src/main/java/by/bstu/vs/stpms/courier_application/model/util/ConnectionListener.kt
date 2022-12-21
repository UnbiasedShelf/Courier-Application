package by.bstu.vs.stpms.courier_application.model.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import by.bstu.vs.stpms.courier_application.model.network.NetworkService.isOnline
import by.bstu.vs.stpms.courier_application.model.repository.OrderRepository
import kotlinx.coroutines.*

object ConnectionListener {
    private const val TAG = "ConnectionListener"

    fun init(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (!isOnline(context)) onLost()

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onAvailable()
            }

            override fun onLost(network: Network) {
                onLost()
            }
        })
    }

    //TODO test what happens if server changes some stuff
    //todo check globalscope
    @OptIn(DelicateCoroutinesApi::class)
    fun onAvailable() {
        Log.d(TAG, "Available")
        GlobalScope.launch(Dispatchers.IO) {
            OrderRepository.sendDecline()
            OrderRepository.sendUpdateState()
        }
    }

    fun onLost() {
        Log.d(TAG, "Lost")
    }
}