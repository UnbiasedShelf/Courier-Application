package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Destination
import by.bstu.vs.stpms.courier_application.ui.util.Util

@Composable
fun DestinationCard(destination: Destination, hideInfo: Boolean = false) {
    Column {
        IconText(text = destination.address) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_location_on_16),
                contentDescription = "Address"
            )
        }
        val context = LocalContext.current
        IconText(
            text = "${Util.formatDate(context, destination.rangeStart)} -> " +
                    Util.formatDate(context, destination.rangeEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_access_time_16),
                contentDescription = "Delivery time"
            )
        }
        if (!hideInfo) {
            IconText(text = destination.name) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_person_16),
                    contentDescription = "Person's name"
                )
            }

            IconText(text = destination.phone) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_phone_16),
                    contentDescription = "Customer's phone"
                )
            }
        }
    }
}