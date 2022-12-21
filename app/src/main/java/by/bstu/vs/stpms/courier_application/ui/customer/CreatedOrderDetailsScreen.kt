package by.bstu.vs.stpms.courier_application.ui.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.ui.composables.BackButton
import by.bstu.vs.stpms.courier_application.ui.composables.OrderCard
import by.bstu.vs.stpms.courier_application.ui.composables.ProductList
import by.bstu.vs.stpms.courier_application.ui.customer.model.CreatedOrderDetailsViewModel
import kotlinx.coroutines.launch

@Composable
fun CreatedOrderDetailsScreen(
    orderId: Long,
    callNumber: Boolean,
    navHostController: NavHostController,
    viewModel: CreatedOrderDetailsViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scope.launch {
            viewModel.getOrder(orderId)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.order_num, orderId)) },
                navigationIcon = { BackButton(navController = navHostController) }
            )
        }
    ) {
        if (viewModel.order?.isSuccessful == true && viewModel.order?.data != null) {
            val order = viewModel.order?.data!!
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OrderCard(
                    order = order,
                    isCourier = false,
                    callNumber = callNumber
                )

                ProductList(order.ordered)
            }
        }
        else {
            //TODO error?
        }
    }
}