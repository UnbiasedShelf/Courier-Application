package by.bstu.vs.stpms.courier_application.ui.customer

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
import by.bstu.vs.stpms.courier_application.ui.composables.OrderItem
import by.bstu.vs.stpms.courier_application.ui.customer.model.HistoryViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HistoryScreen(
    onItemClick: (Order) -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    LaunchedEffect(true) { viewModel.getHistory() }
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState) {
        viewModel.historyList?.let {
            SwipeRefresh(
                state = rememberSwipeRefreshState(viewModel.isRefreshing),
                onRefresh = { viewModel.getHistory() }
            ) {
                if (it.isSuccessful) {
                    if (!it.data.isNullOrEmpty()) {
                        LazyColumn(Modifier.fillMaxHeight()) {
                            items(it.data) { order ->
                                OrderItem(
                                    order = order,
                                    isHistory = true,
                                    isCourier = false,
                                    onClick = {
                                        onItemClick(order)
                                    }
                                )
                            }
                        }
                    } else {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = stringResource(R.string.empty_history),
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
