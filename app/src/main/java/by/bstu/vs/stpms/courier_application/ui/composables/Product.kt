package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.Ordered
import by.bstu.vs.stpms.courier_application.ui.util.Util

@Composable
fun ProductList(ordereds: Collection<Ordered>) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Column {
            Row(Modifier.padding(8.dp)) {
                Text(
                    text = stringResource(id = R.string.amount),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(id = R.string.info),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = stringResource(id = R.string.price),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Divider()

            ordereds.forEachIndexed { index, ordered ->
                ProductItem(
                    ordered = ordered,
                    isDark = index % 2 == 0
                )
            }
        }
    }
}

@Composable
fun ProductItem(ordered: Ordered, isDark: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = if (isDark) {
                    if (MaterialTheme.colors.isLight) Color.LightGray.copy(alpha = ContentAlpha.medium)
                    else Color.DarkGray
                } else Color.Transparent
            )
            .padding(8.dp)
    ) {
        Text(
            text = ordered.amount.toString(),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = ordered.product.name,
            modifier = Modifier.weight(2f)
        )
        Text(
            text = Util.formatDouble(ordered.product.price),
            modifier = Modifier.weight(1f)
        )
    }
}