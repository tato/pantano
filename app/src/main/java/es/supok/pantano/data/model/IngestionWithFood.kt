package es.supok.pantano.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class IngestionWithFood(

    @Embedded val ingestion: Ingestion,
    @Relation(
            parentColumn = "foodId",
            entityColumn = "id"
    )
    val food: Food
)
