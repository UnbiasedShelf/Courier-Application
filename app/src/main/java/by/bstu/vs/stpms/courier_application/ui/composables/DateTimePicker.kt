package by.bstu.vs.stpms.courier_application.ui.composables


import android.app.TimePickerDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.ui.util.Util
import java.sql.Timestamp
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun DateTimePicker(
    labelText: String,
    timestamp: Timestamp?,
    onTimeStampChange: (Timestamp?) -> Unit,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    var showDateDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    ValidatedTextField(
        value = Util.formatDate(context, timestamp, true),
        onValueChange = { },
        labelText = labelText,
        trailingIcon = {
            Icon(Icons.Filled.Edit, contentDescription = "Edit")
        },
        onTrailingIconClick = { showDateDialog = true },
        isReadOnly = true,
        isError = isError,
        errorMessage = errorMessage
    )
    if (showDateDialog) {

        DatePicker(onDateSelected = { date ->
            pickTime(context) { hour, minute ->
                val zone = TimeZone.getDefault().toZoneId()
                val zonedDateTime = ZonedDateTime.of(date, LocalTime.of(hour, minute), zone)
                onTimeStampChange(Timestamp(Timestamp.from(zonedDateTime.toInstant()).time))
            }
        }) {
            showDateDialog = false
        }
    }
}

@Composable
fun DatePicker(onDateSelected: (LocalDate) -> Unit, onDismissRequest: () -> Unit) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_date), //todo param?
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selectedDate.value.format(DateTimeFormatter.ofPattern("MMM d, YYYY")),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(onDateSelected = {
                selectedDate.value = it
            })

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.button
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(selectedDate.value)
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        style = MaterialTheme.typography.button
                    )
                }

            }
        }
    }
}

@Composable
fun CustomCalendarView(onDateSelected: (LocalDate) -> Unit) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CalenderViewCustom))
        },
        update = { view ->
            val zone = TimeZone.getDefault().toZoneId()
            view.minDate = LocalDateTime.now(zone).atZone(zone).toInstant().toEpochMilli()
            view.maxDate = LocalDateTime.now(zone).plusDays(6).atZone(zone).toInstant().toEpochMilli()
            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    LocalDate
                        .now()
                        .withMonth(month + 1)
                        .withYear(year)
                        .withDayOfMonth(dayOfMonth)
                )
            }
        }
    )
}

private fun pickTime(
    context: Context,
    callback: (Int, Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    val currentHour = calendar[Calendar.HOUR_OF_DAY]
    val currentMinute = calendar[Calendar.MINUTE]

    val timePickerDialog = TimePickerDialog(
        context,
        R.style.CalenderViewCustom,
        {_, hour : Int, minute: Int ->
            callback(hour, minute)
        }, currentHour, currentMinute, false
    )
    timePickerDialog.show()
}