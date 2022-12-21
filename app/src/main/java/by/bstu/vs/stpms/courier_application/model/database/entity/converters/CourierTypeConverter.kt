package by.bstu.vs.stpms.courier_application.model.database.entity.converters

import androidx.room.TypeConverter
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType

class CourierTypeConverter {
    @TypeConverter
    fun fromEnum(courierType: CourierType?): String? {
        return courierType?.name
    }

    @TypeConverter
    fun toEnum(courierTypeString: String): CourierType? {
        return courierTypeString?.let { CourierType.valueOf(it) }
    }
}