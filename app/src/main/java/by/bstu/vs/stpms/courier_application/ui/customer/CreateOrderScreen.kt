package by.bstu.vs.stpms.courier_application.ui.customer

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Ordered
import by.bstu.vs.stpms.courier_application.model.database.entity.Product
import by.bstu.vs.stpms.courier_application.ui.composables.*
import by.bstu.vs.stpms.courier_application.ui.customer.model.CreateOrderViewModel
import by.bstu.vs.stpms.courier_application.ui.util.MaskVisualTransformation
import by.bstu.vs.stpms.courier_application.ui.util.OrderValidator
import by.bstu.vs.stpms.courier_application.ui.util.Util
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Timestamp

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CreateOrderScreen(
    navHostController: NavHostController,
    viewModel: CreateOrderViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val state = rememberPagerState()
    val count = 5
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.create_order)) },
                navigationIcon = { BackButton(navController = navHostController) },
                actions = {
                    AnimatedVisibility(
                        visible = state.currentPage == count - 1,
                        enter = expandHorizontally(expandFrom = Alignment.Start),
                        exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
                    ) {
                        val successMessage = stringResource(R.string.order_created_successfully)
                        val invalidFormMessage = stringResource(R.string.invalid_creating_form)
                        val context = LocalContext.current

                        var isLoading by remember { mutableStateOf(false) }

                        IconButton(
                            enabled = !isLoading,
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    val message = if (OrderValidator.isFormValid(
                                            senderName = viewModel.senderName,
                                            senderPhone = viewModel.senderPhone,
                                            senderAddress = viewModel.senderAddress,
                                            recipientName = viewModel.recipientName,
                                            recipientPhone = viewModel.recipientPhone,
                                            recipientAddress = viewModel.recipientAddress,
                                            products = viewModel.products,
                                            prs = viewModel.pickUpRangeStart,
                                            pre = viewModel.pickUpRangeEnd,
                                            drs = viewModel.deliveryRangeStart,
                                            dre = viewModel.deliveryRangeEnd
                                        )
                                    ) {
                                        val result = viewModel.save()
                                        if (result.isSuccessful) {
                                            navHostController.popBackStack()
                                            successMessage
                                        } else {
                                            result.t?.message ?: ""
                                        }
                                    } else {
                                        invalidFormMessage
                                    }
                                    isLoading = false
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_check_24),
                                contentDescription = "Save"
                            )
                        }
                    }
                }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                count = count,
                state = state,
                modifier = Modifier.weight(1f)
            ) { page ->
                LaunchedEffect(viewModel.products.size) {
                    Log.d("tagg", viewModel.products.joinToString { it.product.name })
                }
                LazyColumn(Modifier.fillMaxSize()) {
                    when (page) {
                        0 -> {
                            items(viewModel.products) { ordered ->
                                ProductItem(
                                    title = stringResource(
                                        R.string.order_item,
                                        "#${viewModel.products.indexOf(ordered) + 1}"
                                    ),
                                    ordered = ordered,
                                    onAction = {
                                        viewModel.products.remove(ordered)
                                    }
                                )
                            }
                            item {
                                ProductItem(
                                    title = stringResource(id = R.string.new_order_item),
                                    onAction = { viewModel.products.add(it) },
                                    isChangeable = true
                                )
                            }
                        }
                        1 -> {
                            item {
                                PointCard(
                                    title = stringResource(R.string.sender),
                                    address = viewModel.senderAddress,
                                    onAddressChange = { viewModel.senderAddress = it },
                                    name = viewModel.senderName,
                                    onNameChange = { viewModel.senderName = it },
                                    phone = viewModel.senderPhone,
                                    onPhoneChange = { viewModel.senderPhone = it },
                                    rangeStart = viewModel.pickUpRangeStart,
                                    onRangeStartChange = { viewModel.pickUpRangeStart = it },
                                    isStartValid = OrderValidator.isPickupRangeStartValid(
                                        viewModel.pickUpRangeStart,
                                        viewModel.pickUpRangeEnd,
                                        viewModel.deliveryRangeStart,
                                        viewModel.deliveryRangeEnd,
                                    ),
                                    startErrorMessage = stringResource(R.string.prs_error),
                                    rangeEnd = viewModel.pickUpRangeEnd,
                                    onRangeEndChange = { viewModel.pickUpRangeEnd = it },
                                    isEndValid = OrderValidator.isPickupRangeEndValid(
                                        viewModel.pickUpRangeStart,
                                        viewModel.pickUpRangeEnd,
                                        viewModel.deliveryRangeStart,
                                        viewModel.deliveryRangeEnd,
                                    ),
                                    endErrorMessage = stringResource(R.string.pre_error),
                                )
                            }
                        }
                        2 -> {
                            item {
                                PointCard(
                                    title = stringResource(R.string.recipient),
                                    address = viewModel.recipientAddress,
                                    onAddressChange = { viewModel.recipientAddress = it },
                                    name = viewModel.recipientName,
                                    onNameChange = { viewModel.recipientName = it },
                                    phone = viewModel.recipientPhone,
                                    onPhoneChange = { viewModel.recipientPhone = it },
                                    rangeStart = viewModel.deliveryRangeStart,
                                    onRangeStartChange = { viewModel.deliveryRangeStart = it },
                                    isStartValid = OrderValidator.isDeliveryRangeStartValid(
                                        viewModel.pickUpRangeStart,
                                        viewModel.pickUpRangeEnd,
                                        viewModel.deliveryRangeStart,
                                        viewModel.deliveryRangeEnd,
                                    ),
                                    startErrorMessage = stringResource(R.string.drs_error),
                                    rangeEnd = viewModel.deliveryRangeEnd,
                                    onRangeEndChange = { viewModel.deliveryRangeEnd = it },
                                    isEndValid = OrderValidator.isDeliveryRangeEndValid(
                                        viewModel.pickUpRangeStart,
                                        viewModel.pickUpRangeEnd,
                                        viewModel.deliveryRangeStart,
                                        viewModel.deliveryRangeEnd,
                                    ),
                                    endErrorMessage = stringResource(R.string.dre_error),
                                )
                            }
                        }
                        3 -> {
                            item {
                                Card(
                                    shape = RoundedCornerShape(4.dp),
                                    elevation = 4.dp,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = stringResource(R.string.additional_info),
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        ValidatedTextField(
                                            value = viewModel.additionalInfo,
                                            onValueChange = { viewModel.additionalInfo = it },
                                            labelText = stringResource(R.string.additional_info),
                                            singleLine = false,
                                            maxLines = 5,
                                            enableFocus = false,
                                            modifier = Modifier.height(160.dp)
                                        )
                                    }
                                }
                            }
                        }
                        4 -> {
                            item {
                                Card(
                                    shape = RoundedCornerShape(4.dp),
                                    elevation = 4.dp,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = stringResource(R.string.summary),
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Divider()
                                        LabelText(
                                            label = stringResource(R.string.total_items),
                                            text = viewModel.totalItems.toString()
                                        )
                                        LabelText(
                                            label = stringResource(R.string.total_weight),
                                            text = "${Util.formatDouble(viewModel.totalWeight)} ${
                                                stringResource(
                                                    R.string.kg
                                                )
                                            }"
                                        )
                                        LabelText(
                                            label = stringResource(R.string.from),
                                            text = viewModel.senderAddress
                                        )
                                        LabelText(
                                            label = stringResource(R.string.to),
                                            text = viewModel.recipientAddress
                                        )
                                        LabelText(
                                            label = stringResource(R.string.to_pay),
                                            text = "${Util.formatDouble(viewModel.calculateCost())} ${
                                                stringResource(
                                                    R.string.dollar
                                                )
                                            }"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Surface(elevation = 8.dp) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        enabled = state.currentPage != 0,
                        onClick = {
                            scope.launch {
                                state.animateScrollToPage(state.currentPage - 1)
                            }
                        }
                    ) {
                        Icon(Icons.Filled.ArrowLeft, "Back")
                    }
                    HorizontalPagerIndicator(pagerState = state)
                    IconButton(
                        enabled = state.currentPage != count - 1,
                        onClick = {
                            scope.launch {
                                state.animateScrollToPage(state.currentPage + 1)
                            }
                        }
                    ) {
                        Icon(Icons.Filled.ArrowRight, "Forward")
                    }
                }
            }
        }
    }
}

@Composable
private fun PointCard(
    title: String,
    address: String,
    onAddressChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    rangeStart: Timestamp?,
    onRangeStartChange: (Timestamp?) -> Unit,
    isStartValid: Boolean,
    startErrorMessage: String,
    rangeEnd: Timestamp?,
    onRangeEndChange: (Timestamp?) -> Unit,
    isEndValid: Boolean,
    endErrorMessage: String,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            ValidatedTextField(
                value = address,
                onValueChange = onAddressChange,
                labelText = stringResource(R.string.address),
                isError = !OrderValidator.isAddressValid(address),
                errorMessage = stringResource(R.string.address_length)
            )
            ValidatedTextField(
                value = name,
                onValueChange = onNameChange,
                labelText = stringResource(R.string.name),
                isError = !OrderValidator.isNameValid(name),
                errorMessage = stringResource(R.string.name_length_message)
            )
            ValidatedTextField(
                value = phone,
                onValueChange = {
                    if (it.length < 10 && it.all { char -> char.isDigit() }) {
                        onPhoneChange(it)
                    }
                },
                labelText = stringResource(R.string.phone_number),
                isError = !OrderValidator.isPhoneValid(phone),
                keyboardType = KeyboardType.Phone,
                errorMessage = stringResource(R.string.phone_message),
                visualTransformation = MaskVisualTransformation("+375 (##) ###-##-##")
            )
            DateTimePicker(
                timestamp = rangeStart,
                onTimeStampChange = onRangeStartChange,
                labelText = stringResource(R.string.from_time),
                isError = !isStartValid,
                errorMessage = startErrorMessage
            )
            DateTimePicker(
                timestamp = rangeEnd,
                onTimeStampChange = onRangeEndChange,
                labelText = stringResource(R.string.to_time),
                isError = !isEndValid,
                errorMessage = endErrorMessage
            )
        }
    }
}

@Composable
private fun ProductItem(
    title: String,
    isChangeable: Boolean = false,
    ordered: Ordered? = null,
    onAction: (Ordered) -> Unit
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        var reset by remember { mutableStateOf(false) }
        var amount by remember(reset, ordered) { mutableStateOf(ordered?.amount ?: 1) }
        var displayedAmount by remember(amount) { mutableStateOf(amount.toString()) }
        var name by remember(reset, ordered) { mutableStateOf(ordered?.product?.name ?: "") }
        var price by remember(reset, ordered) { mutableStateOf(ordered?.product?.price ?: 0.0) }
        var weight by remember(reset, ordered) { mutableStateOf(ordered?.product?.weight ?: 0.0) }

        val scope = rememberCoroutineScope()
        val onClick = {
            val product = Ordered().apply {
                this.amount = amount
                this.product = Product().apply {
                    this.name = name
                    this.price = price
                    this.weight = weight
                }
            }
            onAction(product)
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                if (!isChangeable) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = onClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_decline_24),
                            contentDescription = "Remove",
                            tint = Color.Red
                        )
                    }
                }
            }
            ValidatedTextField(
                value = name,
                onValueChange = { name = it },
                labelText = stringResource(R.string.name),
                isError = !OrderValidator.isNameValid(name),
                errorMessage = stringResource(R.string.product_name_length_message),
                isReadOnly = !isChangeable
            )

            var job: Job? by remember { mutableStateOf(null) }

            Row(Modifier.fillMaxWidth()) {
                ValidatedRowTextField(
                    value = displayedAmount,
                    onValueChange = {
                        displayedAmount = it
                        job?.cancel()
                        job = scope.launch {
                            delay(700)
                            amount = try {
                                if (it.isEmpty()) 1
                                else {
                                    val integer = it.toInt()
                                    if (integer in 1..99999) integer
                                    else amount
                                }
                            } catch (e: Exception) {
                                amount
                            }
                            displayedAmount = amount.toString()
                        }
                    },
                    labelText = stringResource(R.string.amount),
                    keyboardType = KeyboardType.Number,
                    isReadOnly = !isChangeable,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                ValidatedRowTextField(
                    value = Util.formatDouble(price),
                    onValueChange = {
                        price = try {
                            if (it.isEmpty()) 0.0
                            else Util.toDouble(it)
                        } catch (e: Exception) {
                            price
                        }
                    },
                    labelText = stringResource(R.string.price),
                    keyboardType = KeyboardType.Number,
                    isReadOnly = !isChangeable,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                ValidatedRowTextField(
                    value = Util.formatDouble(weight),
                    onValueChange = {
                        weight = try {
                            if (it.isEmpty()) 0.0
                            else Util.toDouble(it)
                        } catch (e: Exception) {
                            weight
                        }
                    },
                    labelText = stringResource(R.string.weight),
                    keyboardType = KeyboardType.Number,
                    isReadOnly = !isChangeable,
                    modifier = Modifier.weight(1f)
                )
            }
            if (isChangeable) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        reset = !reset
                    }) {
                        Text(text = stringResource(R.string.reset))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onClick()
                            reset = !reset
                        },
                        enabled = OrderValidator.isProductValid(name)
                    ) {
                        Text(text = stringResource(R.string.add))
                    }
                }
            }
        }
    }
}