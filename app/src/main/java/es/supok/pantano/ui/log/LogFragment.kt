package es.supok.pantano.ui.log

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import es.supok.pantano.R
import es.supok.pantano.data.db.AppDatabase
import es.supok.pantano.data.model.Ingestion
import es.supok.pantano.ui.AppViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class LogFragment : Fragment() {

    private val db by lazy { AppDatabase.getInstance(requireActivity()) }
    private val model: LogViewModel by viewModels { AppViewModelFactory(db) }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_log, container, false)
        val currentDateView = root.findViewById<EditText>(R.id.log_current_date)
        currentDateView.setOnClickListener {
            val dialog = DatePickerDialog(requireContext())
            dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                model.currentDate.value = LocalDate.of(year, month+1, dayOfMonth)
            }
            dialog.show()
        }

        model.currentDate.observe(requireActivity()) {
            currentDateView.setText(it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
        }
        return root
    }

    class IngestionViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.ingestion_food_name)
    }

    class IngestionAdapter(
        var ingestions: List<Ingestion>,
//        private val db: AppDatabase,
//        private val showDialog: (FoodsFragment.EditFoodFragment) -> Unit,
    ) : RecyclerView.Adapter<IngestionViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngestionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.ingestion_item, parent, false)
            return IngestionViewHolder(view)
        }

        override fun onBindViewHolder(holder: IngestionViewHolder, position: Int) {
            holder.name.text = ingestions[position].foodId.toString()
        }

        override fun getItemCount(): Int = ingestions.size
    }
}