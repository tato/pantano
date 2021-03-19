package es.supok.pantano.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import es.supok.pantano.data.model.Food

@Dao
interface FoodDao {
    @Insert
    fun insert(food: Food): Long

    @Update
    fun update(vararg food: Food)

    @Delete
    fun delete(vararg food: Food)

    @Query("SELECT * FROM food ORDER BY created DESC")
    fun getLatest(): LiveData<List<Food>>
}