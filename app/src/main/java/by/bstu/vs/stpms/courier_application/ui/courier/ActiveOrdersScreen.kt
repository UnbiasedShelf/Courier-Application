package by.bstu.vs.stpms.courier_application.ui.courier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.OrderState
import by.bstu.vs.stpms.courier_application.ui.composables.OrderItem
import by.bstu.vs.stpms.courier_application.ui.composables.OrderLabel
import by.bstu.vs.stpms.courier_application.ui.courier.model.ActiveOrderViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ActiveOrdersScreen(
    onItemClick: (Order) -> Unit,
    viewModel: ActiveOrderViewModel = viewModel()
) {
    LaunchedEffect(true) { viewModel.getActiveOrders() }
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        viewModel.orders?.let {
            SwipeRefresh(
                state = rememberSwipeRefreshState(viewModel.isRefreshing),
                onRefresh = { viewModel.getActiveOrders() }
            ) {
                if (it.isSuccessful) {
                    if (!it.data.isNullOrEmpty()) {
                        OrderList(orders = it.data, onItemClick = onItemClick)
                    } else {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = stringResource(R.string.no_available_orders),
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

@Composable
private fun OrderList(
    onItemClick: (Order) -> Unit,
    orders: List<Order>
) {
    LazyColumn(Modifier.fillMaxHeight()) {
        val delivering = orders.filter { it.state == OrderState.Delivering }
        if (delivering.isNotEmpty()) {
            item {
                OrderLabel(text = stringResource(id = R.string.delivering))
            }
            items(delivering) { order ->
                OrderItem(
                    order = order,
                    onClick = {
                        onItemClick(order)
                    }
                )
            }
        }

        val ordered = orders.filter { it.state == OrderState.Ordered }
        if (ordered.isNotEmpty()) {
            item {
                OrderLabel(text = stringResource(id = R.string.ordered))
            }
            items(ordered) { order ->
                OrderItem(
                    order = order,
                    onClick = {
                        onItemClick(order)
                    }
                )
            }
        }
    }
}