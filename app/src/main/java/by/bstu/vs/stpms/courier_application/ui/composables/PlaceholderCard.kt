package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun <T> PlaceholderCard(
    item: T?,
    modifier: Modifier = Modifier,
    heightIn: Dp = 150.dp,
    content: @Composable () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = heightIn),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .placeholder(
                    visible = item == null,
                    highlight = PlaceholderHighlight.shimmer(
                        MaterialTheme.colors.onSurface.copy(
                            alpha = ContentAlpha.disabled
                        )
                    ),
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(8.dp)
        ) {
            content()
        }

    }
}