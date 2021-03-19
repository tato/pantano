package es.supok.pantano.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import es.supok.pantano.data.model.Entry
import java.time.LocalDate

@Dao
interface EntryDao {
    @Query("SELECT * FROM entries WHERE day = :date")
    fun getByDate(date: LocalDate): LiveData<Entry>
}