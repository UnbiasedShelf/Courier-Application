package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.OrderState
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.OrderState.Canceled
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.OrderState.Delivered
import by.bstu.vs.stpms.courier_application.ui.util.Util

@Composable
fun OrderItem(
    order: Order,
    isHistory: Boolean = false,
    isCourier: Boolean = true,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(
                text = stringResource(id = R.string.order_num, order.id),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Divider()
            if (isCourier) {
                CourierItem(order = order)
            } else {
                CustomerItem(order = order, isHistory = isHistory)
            }
        }
    }
}

@Composable
fun ColumnScope.CourierItem(order: Order) {
    val context = LocalContext.current
    LabelText(label = stringResource(R.string.from), text = order.sender.address)
    LabelText(
        label = stringResource(R.string.pickup_in),
        text = "${Util.formatDate(context, order.sender.rangeStart)} - ${Util.formatDate(context, order.sender.rangeEnd)}"
    )
    LabelText(label = stringResource(R.string.to), text = order.recipient.address)
    LabelText(
        label = stringResource(R.string.deliver_in),
        text = "${Util.formatDate(context, order.recipient.rangeStart)} - ${Util.formatDate(context, order.recipient.rangeEnd)}"
    )
    LabelText(
        label = stringResource(id = R.string.total_mass),
        text = "${Util.formatDouble(order.totalWeight)} ${stringResource(id = R.string.kg)}"
    )
    LabelText(
        label = stringResource(id = R.string.total_price),
        text = "${Util.formatDouble(order.totalPrice)} ${stringResource(id = R.string.dollar)}"
    )
}

@Composable
fun ColumnScope.CustomerItem(
    order: Order,
    isHistory: Boolean = false
) {
    LabelText(label = stringResource(R.string.from), text = order.sender.address)
    LabelText(label = stringResource(R.string.to), text = order.recipient.address)
    if (order.courier != null) {
        LabelText(
            label = stringResource(R.string.courier),
            text = "${order.courier.firstName} ${order.courier.secondName}"
        )
    }

    if (isHistory) {
        Spacer(modifier = Modifier.height(6.dp))
        when (order.state) {
            Delivered -> {
                IconText(
                    text = stringResource(R.string.delivered)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_verified_16),
                        tint = Color.Green,
                        contentDescription = "status"
                    )
                }
            }
            Canceled -> {
                IconText(
                    text = stringResource(R.string.canceled)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_not_verified_16),
                        tint = Color.Red,
                        contentDescription = "status"
                    )
                }
            }
            else -> {
                IconText(
                    text = stringResource(R.string.not_accepted)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_remove_circle_24),
                        tint = Color(0xffff6600),
                        contentDescription = "status"
                    )
                }
            }
        }
    } else {
        LabelText(
            label = stringResource(R.string.to_pay),
            text = "${Util.formatDouble(order.calculateCost())} ${stringResource(R.string.dollar)}"
        )
    }
}