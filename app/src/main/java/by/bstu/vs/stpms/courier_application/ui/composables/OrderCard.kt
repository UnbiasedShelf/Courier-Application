package by.bstu.vs.stpms.courier_application.ui.composables

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Destination
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.getResId
import by.bstu.vs.stpms.courier_application.ui.util.Util
import kotlinx.coroutines.launch
import kotlin.math.exp

@Composable
fun OrderCard(
    order: Order,
    isCourier: Boolean = true,
    callNumber: Boolean = false,
    updateState: (suspend () -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (isCourier) {
                if (!order.info.isNullOrEmpty()) {
                    LabelText(
                        label = stringResource(id = R.string.additional_info),
                        text = order.info
                    )
                }
                LabelText(
                    label = stringResource(id = R.string.total_mass),
                    text = "${Util.formatDouble(order.totalWeight)} ${stringResource(id = R.string.kg)}"
                )
                LabelText(
                    label = stringResource(id = R.string.total_price),
                    text = "${Util.formatDouble(order.totalPrice)} ${stringResource(id = R.string.dollar)}"
                )
                updateState?.let {
                    val scope = rememberCoroutineScope()
                    var isStateUpdating by remember { mutableStateOf(false) }
                    Button(
                        enabled = !isStateUpdating,
                        onClick = {
                            if (!isStateUpdating) {
                                scope.launch {
                                    isStateUpdating = true
                                    it()
                                    isStateUpdating = false
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(top = 9.dp)
                            .defaultMinSize(minHeight = 48.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isStateUpdating) {
                                ButtonProgress()
                            }
                            Text(
                                text = stringResource(R.string.update_state_to, stringResource(id = order.state.next.getResId())),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                LabelText(
                    label = stringResource(id = R.string.total_mass),
                    text = "${Util.formatDouble(order.totalWeight)} ${stringResource(id = R.string.kg)}"
                )
                LabelText(
                    label = stringResource(id = R.string.to_pay),
                    text = "${Util.formatDouble(order.calculateCost())} ${stringResource(id = R.string.dollar)}"
                )
                if (order.courier != null) {
                    LabelText(
                        label = stringResource(id = R.string.name),
                        text = "${order.courier.firstName} ${order.courier.secondName}"
                    )
                    LabelText(
                        label = stringResource(id = R.string.phone_number),
                        text = order.courier.phone
                    )
                    if (callNumber) {
                        val context = LocalContext.current
                        Button(
                            onClick = { callNumber(order.courier.phone, context) },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .defaultMinSize(minHeight = 48.dp)
                                .fillMaxWidth()

                        ) {
                            Text(
                                text = stringResource(id = R.string.call),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            PointInfo(
                label = stringResource(id = R.string.sender),
                destination = order.sender,
                callNumber = callNumber && isCourier
            )
            PointInfo(
                label = stringResource(id = R.string.recipient),
                destination = order.recipient,
                callNumber = callNumber && isCourier
            )
        }
    }
}

@Composable
private fun ColumnScope.PointInfo(
    label: String,
    destination: Destination,
    callNumber: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                contentDescription = "Chevron"
            )
        }
    }
    Divider()
    AnimatedVisibility(visible = expanded) {
        Column {
            LabelText(label = stringResource(R.string.address), text = destination.address)
            LabelText(label = stringResource(R.string.name), text = destination.name)
            LabelText(label = stringResource(R.string.phone_number), text = destination.phone)
            if (callNumber) {
                val context = LocalContext.current
                Button(
                    onClick = { callNumber(destination.phone, context) },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth()

                ) {
                    Text(
                        text = stringResource(id = R.string.call),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

private fun callNumber(number: String, context: Context) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:${number}")
    context.startActivity(intent)
}