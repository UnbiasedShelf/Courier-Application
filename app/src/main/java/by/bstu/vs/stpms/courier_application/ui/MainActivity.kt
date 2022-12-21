package by.bstu.vs.stpms.courier_application.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import by.bstu.vs.stpms.courier_application.model.util.ConnectionListener
import by.bstu.vs.stpms.courier_application.ui.composables.AppTheme
import by.bstu.vs.stpms.courier_application.ui.courier.ActiveDetailsScreen
import by.bstu.vs.stpms.courier_application.ui.courier.AvailableDetailsScreen
import by.bstu.vs.stpms.courier_application.ui.courier.CourierScreen
import by.bstu.vs.stpms.courier_application.ui.customer.CreateOrderScreen
import by.bstu.vs.stpms.courier_application.ui.customer.CreatedOrderDetailsScreen
import by.bstu.vs.stpms.courier_application.ui.customer.CustomerScreen
import by.bstu.vs.stpms.courier_application.ui.login.LoginScreen
import by.bstu.vs.stpms.courier_application.ui.login.RegistrationScreen
import by.bstu.vs.stpms.courier_application.ui.login.SplashScreen
import by.bstu.vs.stpms.courier_application.ui.util.Route
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = Route.SPLASH,
                    enterTransition = {
                        if (disableAnimation())
                            EnterTransition.None
                        else
                            slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                    },
                    exitTransition = {
                        if (disableAnimation())
                            ExitTransition.None
                        else
                            slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )
                    },
                    popEnterTransition = {
                        if (disableAnimation())
                            EnterTransition.None
                        else
                            slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                    },
                    popExitTransition = {
                        if (disableAnimation())
                            ExitTransition.None
                        else
                            slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )
                    }
                ) {
                    composable(Route.SPLASH) {
                        SplashScreen(navController = navController)
                    }
                    composable(Route.LOGIN) {
                        LoginScreen(navController = navController)
                    }
                    composable(Route.REGISTRATION) {
                        RegistrationScreen(navController = navController)
                    }
                    composable(Route.CUSTOMER) {
                        CustomerScreen(navController)
                    }
                    composable(Route.COURIER) {
                        CourierScreen(navController)
                    }

                    composable(Route.CREATE_NEW) {
                        CreateOrderScreen(navController)
                    }
                    composable(
                        "${Route.CREATE_DETAILS}/{order}/{callNumber}",
                        arguments = listOf(
                            navArgument("order") { type = NavType.LongType },
                            navArgument("callNumber") { type = NavType.BoolType }
                        )
                    ) { entry ->
                        val callNumber = entry.arguments
                            ?.getBoolean("callNumber", false) ?: false

                        CreatedOrderDetailsScreen(
                            orderId = entry.getOrderId(),
                            callNumber = callNumber,
                            navHostController = navController
                        )
                    }
                    composable(
                        "${Route.AVAILABLE_DETAILS}/{order}",
                        arguments = listOf(navArgument("order") { type = NavType.LongType })
                    ) { entry ->
                        AvailableDetailsScreen(entry.getOrderId(), navController)
                    }
                    composable(
                        "${Route.ACTIVE_DETAILS}/{order}",
                        arguments = listOf(navArgument("order") { type = NavType.LongType })
                    ) { entry ->
                        ActiveDetailsScreen(entry.getOrderId(), navController)
                    }
                }
            }
        }
        ConnectionListener.init(this)
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun AnimatedContentScope<NavBackStackEntry>.disableAnimation() =
        ((
                initialState.destination.route == Route.CUSTOMER
                        || initialState.destination.route == Route.COURIER
                )
                && (
                targetState.destination.route == Route.CUSTOMER
                        || targetState.destination.route == Route.COURIER
                        || targetState.destination.route == Route.LOGIN
                ))

    private fun NavBackStackEntry.getOrderId() = arguments?.getLong("order") ?: -1
}