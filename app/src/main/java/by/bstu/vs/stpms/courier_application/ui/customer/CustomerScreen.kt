package by.bstu.vs.stpms.courier_application.ui.customer

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import by.bstu.vs.stpms.courier_application.ui.util.CustomerTab
import by.bstu.vs.stpms.courier_application.ui.util.Route
import by.bstu.vs.stpms.courier_application.ui.util.navigateWithOrderAndCallNumber
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomerScreen(parentController: NavHostController) {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val items = listOf(CustomerTab.Created, CustomerTab.History, CustomerTab.Profile)
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
            startDestination = CustomerTab.Created.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(CustomerTab.Created.route) {
                OrderedListScreen(
                    onFabClick = {
                        parentController.navigate(Route.CREATE_NEW)
                    },
                    onItemClick = {
                        parentController.navigateWithOrderAndCallNumber(
                            route = Route.CREATE_DETAILS,
                            order = it,
                            callNumber = true
                        )
                    }
                )
            }
            composable(CustomerTab.History.route) {
                HistoryScreen(
                    onItemClick = {
                        parentController.navigateWithOrderAndCallNumber(
                            route = Route.CREATE_DETAILS,
                            order = it,
                            callNumber = false
                        )
                    }
                )
            }
            composable(CustomerTab.Profile.route) {
                CustomerProfileScreen(
                    toLogin = {
                        parentController.navigate(Route.LOGIN) {
                            popUpTo(0)
                        }
                    },
                    toCourier = {
                        parentController.navigate(Route.COURIER) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}