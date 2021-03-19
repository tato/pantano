package es.supok.pantano.ui.log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import es.supok.pantano.data.db.AppDatabase
import es.supok.pantano.data.model.Entry
import java.time.LocalDate

class LogViewModel(db: AppDatabase) : ViewModel() {
    val currentDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())
    val currentEntry: LiveData<Entry> = currentDate.switchMap {
        db.entryDao().getByDate(it)
    }
}