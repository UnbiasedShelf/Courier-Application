package by.bstu.vs.stpms.courier_application.ui.util

import android.content.Context
import by.bstu.vs.stpms.courier_application.R
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Util {
    private val formatter = SimpleDateFormat("HH:mm dd.MM.yyyy")
    private val timeOnlyFormatter = SimpleDateFormat("HH:mm ")

    fun formatDate(context: Context, timestamp: Timestamp?, empty: Boolean = false): String {
        if (timestamp == null) {
            return if (!empty) "-" else ""
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp.time
        val now = Calendar.getInstance()
        val delta = calendar[Calendar.DAY_OF_YEAR] - now[Calendar.DAY_OF_YEAR]
        val dateTimeString = StringBuilder()
        if (delta > 7) {
            dateTimeString.append(formatter.format(calendar.time))
        } else {
            dateTimeString.append(timeOnlyFormatter.format(calendar.time))
            when (delta) {
                0 -> dateTimeString.append(context.getString(R.string.today))
                1 -> dateTimeString.append(context.getString(R.string.tomorrow))
                else ->{
                    val dayRes = arrayOf(
                        -1,
                        R.string.sun,
                        R.string.mon,
                        R.string.tue,
                        R.string.wed,
                        R.string.thu,
                        R.string.fri,
                        R.string.sat
                    )[calendar.get(Calendar.DAY_OF_WEEK)]
                    dateTimeString.append(context.getString(dayRes))
                }
            }
        }
        return dateTimeString.toString()
    }

    fun toDouble(value: String): Double {
        return (value.replace(",", ".").toDouble() * 100).roundToInt() / 100.0
    }

    fun formatDouble(double: Double) = double.format(2)



    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

}

fun String.formatPhone(): String {
    val code = substring(0..1)
    val first = substring(2..4)
    val second = substring(5..6)
    val third = substring(7..8)
    return "+375 ($code) $first-$second-$third"
}