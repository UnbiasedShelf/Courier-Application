package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrderLabel(text: String) {
    Column(Modifier.padding(8.dp)) {
        Text(text = text)
        Divider()
    }
}