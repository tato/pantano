package es.supok.pantano.ui.foods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.supok.pantano.data.db.AppDatabase
import es.supok.pantano.data.model.Food

class FoodsViewModel(db: AppDatabase) : ViewModel() {

    val latest: LiveData<List<Food>> = db.foodDao().getLatest()

}