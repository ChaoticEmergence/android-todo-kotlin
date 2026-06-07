package com.example.tood_lsit_2_the_revenge.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tood_lsit_2_the_revenge.R
import com.example.tood_lsit_2_the_revenge.contract.TaskFormContract
import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task
import com.example.tood_lsit_2_the_revenge.presenter.TaskFormPresenter
import com.example.tood_lsit_2_the_revenge.repository.TaskRepository

class TaskFormActivity : AppCompatActivity(), TaskFormContract.View {

    private lateinit var presenter: TaskFormContract.Presenter
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerPriority: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var taskId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        val repository = TaskRepository(this)
        presenter = TaskFormPresenter(this, repository)

        taskId = intent.getStringExtra(TaskListActivity.TASK_ID)

        setupViews()
        setupSpinners()
        setupButtons()

        presenter.onViewCreated(taskId)
        if (taskId != null) {
            findViewById<TextView>(R.id.tvFormTitle).text = getString(R.string.title_edit_task)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    private fun setupViews() {
        etTitle = findViewById(R.id.etTaskTitle)
        etDescription = findViewById(R.id.etTaskDescription)
        spinnerCategory = findViewById(R.id.spinnerFormCategory)
        spinnerPriority = findViewById(R.id.spinnerFormPriority)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun setupSpinners() {
        val categoryNames = Category.entries.map { it.name }
        spinnerCategory.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryNames)

        val priorityNames = Priority.entries.map { it.name }
        spinnerPriority.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorityNames)
    }

    private fun setupButtons() {
        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()
            val category = Category.entries[spinnerCategory.selectedItemPosition]
            val priority = Priority.entries[spinnerPriority.selectedItemPosition]
            presenter.onSaveTask(taskId, title, description, category, priority)
        }

        btnCancel.setOnClickListener {
            presenter.onCancelClicked()
        }
    }



    override fun displayTask(task: Task) {
        etTitle.setText(task.title)
        etDescription.setText(task.description)
        spinnerCategory.setSelection(Category.entries.indexOf(task.category))
        spinnerPriority.setSelection(Priority.entries.indexOf(task.priority))
    }

    override fun navigateToList() {
        finish()
    }
    override fun showTitleError() {
        etTitle.error = getString(R.string.error_title_empty)
    }
}