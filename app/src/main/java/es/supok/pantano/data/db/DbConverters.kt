package es.supok.pantano.data.db

import androidx.room.TypeConverter
import es.supok.pantano.data.model.CalorieUnit
import es.supok.pantano.data.model.IngestionTime
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DbConvertersConstants {
    val FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
}

class DbConverters {
    @TypeConverter
    fun fromDateString(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun fromOffsetDateTimeString(value: String?): OffsetDateTime? =
            value?.let { OffsetDateTime.parse(it, DbConvertersConstants.FORMATTER) }

    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime?): String? =
            value?.format(DbConvertersConstants.FORMATTER)

    @TypeConverter
    fun toIngestionTime(value: String) = enumValueOf<IngestionTime>(value)

    @TypeConverter
    fun fromIngestionTime(value: IngestionTime) = value.name

    @TypeConverter
    fun toCalorieUnit(value: String) = enumValueOf<CalorieUnit>(value)

    @TypeConverter
    fun fromCalorieUnit(value: CalorieUnit): String = value.name
}