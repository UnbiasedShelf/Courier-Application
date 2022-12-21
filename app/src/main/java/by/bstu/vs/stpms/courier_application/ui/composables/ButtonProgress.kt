package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonProgress() {
    CircularProgressIndicator(
        strokeWidth = 2.dp,
        modifier = Modifier.size(20.dp).padding(end = 4.dp)
    )
}

@Composable
fun FabProgress() {
    CircularProgressIndicator(
        modifier = Modifier.size(30.dp),
        color = MaterialTheme.colors.onSecondary
    )
}