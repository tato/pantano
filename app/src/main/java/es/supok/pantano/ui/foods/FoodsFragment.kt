package es.supok.pantano.ui.foods

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.supok.pantano.R
import es.supok.pantano.data.db.AppDatabase
import es.supok.pantano.data.model.CalorieUnit
import es.supok.pantano.data.model.Food
import es.supok.pantano.ui.AppViewModelFactory
import java.time.OffsetDateTime

class FoodsFragment : Fragment() {

    private val db by lazy { AppDatabase.getInstance(requireActivity()) }
    private val model: FoodsViewModel by viewModels { AppViewModelFactory(db) }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_foods, container, false)
        val recycler = root.findViewById<RecyclerView>(R.id.latest_foods_list)
        val foodsAdapter = FoodsAdapter(ArrayList(), db) { dialog ->
            dialog.show(requireActivity().supportFragmentManager, "EditFoodFragment")
        }
        model.latest.observe(requireActivity(), {
            foodsAdapter.foods = it
            foodsAdapter.notifyDataSetChanged()
        })
        recycler.adapter = foodsAdapter
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val fab = root.findViewById<FloatingActionButton>(R.id.add_food_button)
        fab.setOnClickListener{
            val date = OffsetDateTime.now()
            val f = Food(null, "Food", null, 1000.0, CalorieUnit.KCAL, date)
            val dialog = EditFoodFragment(f, { db.foodDao().insert(it) }, { db.foodDao().delete(it) })
            dialog.show(requireActivity().supportFragmentManager, "EditFoodFragment")
        }
        return root
    }

    class FoodsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.food_name)
        val calories: TextView = view.findViewById(R.id.food_calories)
        val calorieUnit: TextView = view.findViewById(R.id.food_calorie_unit)
        val created: TextView = view.findViewById(R.id.food_created)
    }

    class FoodsAdapter(
        var foods: List<Food>,
        private val db: AppDatabase,
        private val showDialog: (EditFoodFragment) -> Unit,
    ) : RecyclerView.Adapter<FoodsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
            return FoodsViewHolder(view)
        }

        override fun onBindViewHolder(holder: FoodsViewHolder, position: Int) {
            holder.name.text = foods[position].name
            holder.calories.text = foods[position].calories.toString()
            holder.calorieUnit.text = foods[position].calorieUnit.toString()
            holder.created.text = DateUtils.getRelativeTimeSpanString(
                foods[position].created.toInstant().toEpochMilli(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
            holder.itemView.setOnClickListener{
                val fragment = EditFoodFragment(foods[position], { db.foodDao().update(it) }, { db.foodDao().delete(it)})
                showDialog(fragment)
            }
        }

        override fun getItemCount(): Int = foods.size
    }

    class EditFoodFragment(private val food: Food, private val save: (Food) -> Unit, private val delete: (Food) -> Unit) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val dialogView = requireActivity()
                    .layoutInflater
                    .inflate(R.layout.dialog_edit_food, null)
                val foodNameView = dialogView.findViewById<EditText>(R.id.food_name_edit)
                val foodCaloriesView = dialogView.findViewById<EditText>(R.id.food_calories_edit)
                val foodCalorieUnitView = dialogView.findViewById<Spinner>(R.id.food_calorie_unit_edit)

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    CalorieUnit.values()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                foodCalorieUnitView.adapter = adapter

                foodNameView.setText(food.name, TextView.BufferType.EDITABLE)
                foodCaloriesView.setText(food.calories.toString(), TextView.BufferType.EDITABLE)
                foodCalorieUnitView.setSelection(adapter.getPosition(food.calorieUnit))

                var builder = AlertDialog.Builder(it)
                    .setView(dialogView)
                    .setPositiveButton("Save") { _, _ ->
                        food.name = foodNameView.text.toString()
                        food.calories = foodCaloriesView.text.toString().toDouble()
                        food.calorieUnit = foodCalorieUnitView.selectedItem as CalorieUnit
                        save(food)
                    }
                if (food.id != null) {
                    builder = builder.setNegativeButton("Delete") { _, _ ->
                        delete(food)
                    }
                }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}