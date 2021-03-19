package es.supok.pantano.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.supok.pantano.data.db.AppDatabase
import es.supok.pantano.ui.foods.FoodsViewModel
import es.supok.pantano.ui.log.LogViewModel

class AppViewModelFactory(private val db: AppDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (modelClass) {
            FoodsViewModel::class.java -> FoodsViewModel(db) as T
            LogViewModel::class.java -> LogViewModel(db) as T
            else -> throw IllegalStateException("Unexpected type in ViewModel factory: $modelClass")
        }
    }
}