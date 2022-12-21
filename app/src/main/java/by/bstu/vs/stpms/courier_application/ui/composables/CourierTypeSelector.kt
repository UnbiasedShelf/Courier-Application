package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType

@Composable
fun CourierTypeSelector(
    selectedType: CourierType,
    onTypeChanged: (CourierType) -> Unit
) {
    Column(Modifier.padding(vertical = 12.dp)) {
        Text(
            text = stringResource(R.string.movement_way),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 12.dp)
        )
        val onClick: (CourierType) -> Unit = { onTypeChanged(it) }

        RadioButtonRow(
            title = stringResource(R.string.type_walk),
            selectedType = selectedType,
            type = CourierType.Walk,
            onClick = onClick
        )
        RadioButtonRow(
            title = stringResource(R.string.type_bicycle),
            selectedType = selectedType,
            type = CourierType.Bicycle,
            onClick = onClick
        )
        RadioButtonRow(
            title = stringResource(R.string.type_car),
            selectedType = selectedType,
            type = CourierType.Car,
            onClick = onClick
        )
    }
}

@Composable
private fun RadioButtonRow(
    title: String,
    selectedType: CourierType,
    type: CourierType,
    onClick: (CourierType) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(type) }
    ) {
        RadioButton(
            selected = selectedType == type,
            onClick = null,
            modifier = Modifier.padding(12.dp)
        )
        Text(text = title)
    }
}