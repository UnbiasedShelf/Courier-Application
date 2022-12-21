package by.bstu.vs.stpms.courier_application.ui.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType
import by.bstu.vs.stpms.courier_application.ui.composables.CourierTypeSelector
import by.bstu.vs.stpms.courier_application.ui.composables.UserCard
import by.bstu.vs.stpms.courier_application.ui.customer.model.CustomerProfileViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun CustomerProfileScreen(
    toLogin: () -> Unit,
    toCourier: () -> Unit,
    viewModel: CustomerProfileViewModel = viewModel()
) {
    SideEffect {
        if (viewModel.user == null)
            viewModel.initialLoad()
    }
    Scaffold {
        SwipeRefresh(
            state = rememberSwipeRefreshState(viewModel.isRefreshing),
            onRefresh = { viewModel.refresh() }
        ) {
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                UserCard(
                    user = viewModel.user,
                    isVerified = false,
                    isCourier = false,
                    heightIn = 120.dp,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Button(
                    onClick = {
                        viewModel.logout()
                        toLogin()
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        fontSize = 16.sp
                    )
                }

                if (viewModel.user?.courierType == null || viewModel.user?.courierType == CourierType.NotCourier) {
                    var showDialog by remember { mutableStateOf(false) }

                    TextButton(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .defaultMinSize(minHeight = 48.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.want_become_courier))
                    }

                    var selectedType by remember { mutableStateOf(CourierType.NotCourier) }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            confirmButton = {
                                Button(onClick = {
                                    viewModel.changeType(selectedType)
                                    showDialog = false
                                }) {
                                    Text(text = stringResource(R.string.save))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text(text = stringResource(R.string.cancel))
                                }
                            },
                            text = {
                                CourierTypeSelector(
                                    selectedType = selectedType,
                                    onTypeChanged = {
                                        selectedType = it
                                    }
                                )
                            }
                        )
                    }
                } else {
                    Button(
                        onClick = { toCourier() },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .defaultMinSize(minHeight = 48.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.switch_to_courier),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}