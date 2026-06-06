package com.example.tood_lsit_2_the_revenge.view
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tood_lsit_2_the_revenge.R
import com.example.tood_lsit_2_the_revenge.contract.TaskListContract
import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task
import com.example.tood_lsit_2_the_revenge.presenter.TaskListPresenter
import com.example.tood_lsit_2_the_revenge.repository.TaskRepository
import com.example.tood_lsit_2_the_revenge.view.adapter.TaskAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListActivity : AppCompatActivity(), TaskListContract.View {

    private lateinit var presenter: TaskListContract.Presenter
    private lateinit var adapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerStatus: Spinner
    private lateinit var spinnerPriority: Spinner

    companion object {
        const val TASK_ID = "task_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        val repository = TaskRepository(this)
        presenter = TaskListPresenter(this, repository)

        setupRecyclerView()
        setupSwipeToDelete()
        setupFab()
        setupFilters()
        presenter.onViewCreated()
    }
    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewTasks)
        adapter = TaskAdapter(
            tasks = emptyList(),
            onTaskClicked = { taskId -> presenter.onTaskClicked(taskId) },
            onTaskDoneClicked = { taskId -> presenter.onTaskDoneClicked(taskId) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
    private fun setupSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val task = adapter.getTaskAtPosition(position)
                presenter.onDeleteTask(task.id)
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)
    }
    private fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fabAddTask)
        fab.setOnClickListener {
            Toast.makeText(this, "Ya man ça marche!", Toast.LENGTH_SHORT).show()
        }
    }

    // Cette méthode a requis l'aide de l'IA pour implémenter le principe de filtre
    // et j'ai adapté le reste
    private fun setupFilters() {

        // Filtrer par Category
        spinnerCategory = findViewById(R.id.spinnerCategory)
        val categoryOptions = listOf("Toutes") + Category.entries.map { it.name }
        spinnerCategory.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryOptions)
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selected = if (position == 0) null else Category.entries[position - 1]
                presenter.onFilterByCategory(selected)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Filtrer par complétion via isDone (Boolean)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        val statusOptions = listOf("Toutes", "Complétées", "À faire")
        spinnerStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusOptions)
        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val isDone = when (position) {
                    1 -> true
                    2 -> false
                    else -> null
                }
                presenter.onFilterByCompletionStatus(isDone)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Filtrer par Priorité
        spinnerPriority = findViewById(R.id.spinnerPriority)
        val priorityOptions = listOf("Toutes") + Priority.entries.map { it.name }
        spinnerPriority.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorityOptions)
        spinnerPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selected = if (position == 0) null else Priority.entries[position - 1]
                presenter.onFilterByPriority(selected)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun displayTasks(tasks: List<Task>) {
        adapter.updateTasks(tasks)
    }

    // À implémenter
    override fun navigateToTask(taskId: String?) {}

}