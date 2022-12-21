package by.bstu.vs.stpms.courier_application.ui.customer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.OrderState
import by.bstu.vs.stpms.courier_application.ui.composables.OrderItem
import by.bstu.vs.stpms.courier_application.ui.composables.OrderLabel
import by.bstu.vs.stpms.courier_application.ui.customer.model.OrderedListViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OrderedListScreen(
    onFabClick: () -> Unit,
    onItemClick: (Order) -> Unit,
    viewModel: OrderedListViewModel = viewModel(),
) {
    LaunchedEffect(true) { viewModel.getCreatedOrders() }
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) {
        viewModel.orders?.let {
            SwipeRefresh(
                state = rememberSwipeRefreshState(viewModel.isRefreshing),
                onRefresh = { viewModel.getCreatedOrders() }
            ) {
                if (it.isSuccessful) {
                    if (!it.data.isNullOrEmpty()) {
                        LazyColumn(Modifier.fillMaxHeight()) {
                            val delivering = it.data.filter { it.state == OrderState.Delivering }
                            if (delivering.isNotEmpty()) {
                                item {
                                    OrderLabel(text = stringResource(id = R.string.delivering))
                                }
                                items(delivering) { order ->
                                    OrderItem(
                                        order = order,
                                        onClick = {
                                            onItemClick(order)
                                        },
                                        isCourier = false
                                    )
                                }
                            }

                            val accepted = it.data.filter { it.state == OrderState.Ordered && it.courierId != null }
                            if (accepted.isNotEmpty()) {
                                item {
                                    OrderLabel(text = stringResource(id = R.string.accepted))
                                }
                                items(accepted) { order ->
                                    OrderItem(
                                        order = order,
                                        onClick = {
                                            onItemClick(order)
                                        },
                                        isCourier = false
                                    )
                                }
                            }

                            val ordered = it.data.filter { it.state == OrderState.Ordered && it.courierId == null }
                            if (ordered.isNotEmpty()) {
                                item {
                                    OrderLabel(text = stringResource(id = R.string.ordered))
                                }
                                items(ordered) { order ->
                                    OrderItem(
                                        order = order,
                                        onClick = {
                                            onItemClick(order)
                                        },
                                        isCourier = false
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    } else {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = stringResource(R.string.no_created_orders_found),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                } else {
                    Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState()))
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        scaffoldState.snackbarHostState.showSnackbar("Error: " + it.t?.message)
                    }
                }
            }
        }
    }
}