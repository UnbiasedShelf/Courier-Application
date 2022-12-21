package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.network.dto.googleapi.Bounds
import by.bstu.vs.stpms.courier_application.model.network.dto.googleapi.Route
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun DeliveryMap(
    order: Order,
    route: Route,
    hideInfo: Boolean = false,
    cameraPosition: CameraPosition
) {

    val points by remember(route) { derivedStateOf { PolyUtil.decode(route.polyline.points) } }
    val bounds: Bounds? by remember(route) { derivedStateOf { route.bounds } }

    if (points.isNotEmpty() && bounds != null) {
        val cameraPositionState = rememberCameraPositionState {
            position = cameraPosition
        }
        var showOriginDialog by remember(route) { mutableStateOf(false) }
        var showDestinationDialog by remember(route) { mutableStateOf(false) }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            Polyline(
                points = points,
                color = MaterialTheme.colors.primary
            )
            Marker(
                position = points.first(),
                title = stringResource(R.string.from),
                snippet = order.sender.address,
                onInfoWindowClick = { showOriginDialog = true }
            )
            Marker(
                position = points.last(),
                title = stringResource(R.string.to),
                snippet = order.recipient.address,
                onInfoWindowClick = { showDestinationDialog = true }
            )
        }

        if (showOriginDialog) {
            AlertDialog(
                onDismissRequest = { showOriginDialog = false },
                confirmButton = {
                    Button(onClick = { showOriginDialog = false }) {
                        Text(text = stringResource(R.string.ok))
                    }
                },
                text = {
                    DestinationCard(
                        destination = order.sender,
                        hideInfo = hideInfo
                    )
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.from),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
        if (showDestinationDialog) {
            AlertDialog(
                onDismissRequest = { showDestinationDialog = false },
                confirmButton = {
                    Button(onClick = { showDestinationDialog = false }) {
                        Text(text = stringResource(R.string.ok))
                    }
                },
                text = {
                    DestinationCard(
                        destination = order.recipient,
                        hideInfo = hideInfo
                    )
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.to),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

    }
}