package es.supok.pantano.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "food")
data class Food (
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    var name: String,
    var image: String?,
    var calories: Double,
    var calorieUnit: CalorieUnit,
    val created: OffsetDateTime,
)