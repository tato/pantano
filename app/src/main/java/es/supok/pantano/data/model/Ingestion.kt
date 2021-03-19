package es.supok.pantano.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "ingestion")
data class Ingestion (
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val entryId: Long,
    val foodId: Long,
    val created: OffsetDateTime,
    val quantity: Int,
    val ingestionTime: IngestionTime,
)