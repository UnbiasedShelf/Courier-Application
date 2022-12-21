package by.bstu.vs.stpms.courier_application.ui.courier

import android.location.Geocoder
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.ui.composables.*
import by.bstu.vs.stpms.courier_application.ui.courier.model.CourierOrderDetailsViewModel
import by.bstu.vs.stpms.courier_application.ui.util.Util
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AvailableDetailsScreen(
    orderId: Long,
    navHostController: NavHostController,
    viewModel: CourierOrderDetailsViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(viewModel.order) {
        if (viewModel.order == null) {
            viewModel.getOrder(orderId)
        } else {
            scope.launch(Dispatchers.IO) {
                viewModel.loadMap(Geocoder(context))
            }
        }
    }
    val scaffoldState = rememberScaffoldState()
    var isMap by rememberSaveable { mutableStateOf(false) }

    val successMessage = stringResource(R.string.order_successfully_accepted)
    val errorMessage = stringResource(R.string.error)
    var isLoading by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isMap,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(onClick = {
                    if (!isLoading) {
                        scope.launch {
                            isLoading = true
                            val response = viewModel.acceptOrder()
                            val message = if (response?.isSuccessful == true) {
                                navHostController.popBackStack()
                                successMessage
                            } else {
                                "$errorMessage ${response?.t?.localizedMessage}"
                            }
                            isLoading = false
                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }) {
                    if (isLoading) {
                        FabProgress()
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_check_24),
                            contentDescription = "Accept order"
                        )
                    }
                }
            }
        },
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.order_num, orderId)) },
                navigationIcon = { BackButton(navController = navHostController) },
                actions = {
                    val mapErrorMessage = stringResource(R.string.map_error)
                    IconButton(
                        onClick = {
                            when {
                                viewModel.route?.isSuccessful == false -> Toast.makeText(
                                    context,
                                    mapErrorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                isMap || (!isMap && viewModel.isOnline(context) && viewModel.checkNotNull()) -> isMap =
                                    !isMap
                                else -> {}
                            }
                        },
                        enabled = viewModel.route != null
                    ) {
                        if (viewModel.route != null) {
                            Icon(
                                imageVector = if (isMap) Icons.Filled.List else Icons.Filled.Map,
                                contentDescription = "Switch Screen",
                            )
                        } else {
                            CircularProgressIndicator(
                                color = MaterialTheme.colors.onPrimary.copy(ContentAlpha.disabled),
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            )
        }
    ) {
        var isRefreshing by remember { mutableStateOf(false) }
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                scope.launch(Dispatchers.IO) {
                    viewModel.getOrder(orderId)
                    viewModel.loadMap(Geocoder(context))
                    isRefreshing = false
                }
            }
        ) {
            viewModel.order?.data?.let {
                if (isMap) {
                    if (viewModel.checkNotNull() && viewModel.route?.isSuccessful == true) {
                        DeliveryMap(
                            order = it,
                            route = viewModel.route!!.data!!,
                            hideInfo = true,
                            cameraPosition = viewModel.cameraPosition!!
                        )
                    } else {
                        CircularProgressIndicator()
                    }
                } else {
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        elevation = 4.dp,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(8.dp)) {
                            CourierItem(order = it)
                            if (!it.info.isNullOrEmpty()) {
                                LabelText(
                                    label = stringResource(id = R.string.info),
                                    text = it.info
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}