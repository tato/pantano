package es.supok.pantano.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class EntryWithIngestions(
    @Embedded val entry: Entry,
    @Relation(
            parentColumn = "id",
            entityColumn = "entryId"
    )
    val ingestions: List<Ingestion>
)
