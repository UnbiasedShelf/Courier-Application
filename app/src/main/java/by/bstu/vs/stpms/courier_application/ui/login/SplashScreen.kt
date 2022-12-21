package by.bstu.vs.stpms.courier_application.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType
import by.bstu.vs.stpms.courier_application.model.util.event.Status
import by.bstu.vs.stpms.courier_application.ui.login.model.AuthViewModel
import by.bstu.vs.stpms.courier_application.ui.util.Route

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavHostController
) {
    LaunchedEffect(true) {
        authViewModel.tryAutoLogin()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_express_delivery),
            contentDescription = "Logo",
            modifier = Modifier.size(150.dp)
        )
    }
    LaunchedEffect(authViewModel.autoLoginUser?.status) {
        authViewModel.autoLoginUser?.let {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { user ->
                        navController.navigate(
                            if (user.courierType == CourierType.NotCourier)
                                Route.CUSTOMER
                            else
                                Route.COURIER
                        ) {
                            popUpTo(Route.SPLASH) {
                                inclusive = true
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    navController.navigate(Route.LOGIN) {
                        popUpTo(Route.SPLASH) {
                            inclusive = true
                        }
                    }
                }
                else -> { Log.d("SplashScreen", "Waiting...") }
            }
        }
    }
}