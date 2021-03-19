package es.supok.pantano.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import es.supok.pantano.data.model.Entry
import es.supok.pantano.data.model.Food
import es.supok.pantano.data.model.Ingestion

@Database(entities = [Food::class, Ingestion::class, Entry::class], version = 2)
@TypeConverters(DbConverters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            val i = instance
            if (i != null) {
                return i
            }
            return synchronized(this) {
                val y = instance
                if (y != null) {
                    y
                } else {

                    val created = Room
                        .databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                "pantano.db",
                        )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigrationFrom(1)
                        .build()
                    instance = created
                    created
                }
            }
        }
    }
    abstract fun foodDao(): FoodDao
    abstract fun entryDao(): EntryDao
}
