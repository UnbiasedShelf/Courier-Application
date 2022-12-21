package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabelText(label: String, text: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            style = TextStyle(MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled))
        )
        Text(
            text = text,
            fontSize = 18.sp
        )
    }
}