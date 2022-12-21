package by.bstu.vs.stpms.courier_application.ui.courier

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import by.bstu.vs.stpms.courier_application.ui.util.CourierTab
import by.bstu.vs.stpms.courier_application.ui.util.Route
import by.bstu.vs.stpms.courier_application.ui.util.navigateWithOrder
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CourierScreen(parentController: NavHostController) {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val items = listOf(CourierTab.Available, CourierTab.Active, CourierTab.Profile)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = items.firstOrNull { it.route == currentDestination?.route }?.title?.let {
                            stringResource(id = it)
                        } ?: ""
                    )
                },
            )
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.background
            ) {
                val activity = (LocalContext.current as? Activity)
                BackHandler(enabled = currentDestination?.route == items.first().route) {
                    activity?.finish()
                }
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(screen.title)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AnimatedNavHost(
            navController = navController,
            startDestination = CourierTab.Available.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(CourierTab.Available.route) {
                AvailableOrdersScreen(
                    onItemClick = {
                        parentController.navigateWithOrder(Route.AVAILABLE_DETAILS, it)
                    }
                )
            }
            composable(CourierTab.Active.route) {
                ActiveOrdersScreen(
                    onItemClick = {
                        parentController.navigateWithOrder(Route.ACTIVE_DETAILS, it)
                    }
                )
            }
            composable(CourierTab.Profile.route) {
                CourierProfileScreen(
                    toLogin = {
                        parentController.navigate(Route.LOGIN) {
                            popUpTo(0)
                        }
                    },
                    toCustomer = {
                        parentController.navigate(Route.CUSTOMER) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}