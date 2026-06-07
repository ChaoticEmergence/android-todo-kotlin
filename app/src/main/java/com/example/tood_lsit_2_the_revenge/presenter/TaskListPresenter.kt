package com.example.tood_lsit_2_the_revenge.presenter

import com.example.tood_lsit_2_the_revenge.contract.TaskListContract
import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task
import com.example.tood_lsit_2_the_revenge.repository.TaskRepository

class TaskListPresenter(
    private var view: TaskListContract.View?,
    private val repository: TaskRepository
) : TaskListContract.Presenter {
    private var currentCategory: Category? = null
    private var currentPriority: Priority? = null
    private var currentIsDone: Boolean? = null

    override fun onViewCreated() {
        applyFilters()
    }

    override fun onResume() {
        applyFilters()
    }

    override fun onFilterByCategory(category: Category?) {
        currentCategory = category
        applyFilters()
    }

    override fun onFilterByPriority(priority: Priority?) {
        currentPriority = priority
        applyFilters()
    }

    override fun onFilterByCompletionStatus(isDone: Boolean?) {
        currentIsDone = isDone
        applyFilters()
    }

    override fun onViewDestroyed() {
        view = null
    }

    override fun onDeleteTask(taskId: String) {
        repository.delete(taskId)
        applyFilters()
    }

    override fun onTaskDoneClicked(taskId: String) {
        val task = repository.getById(taskId) ?: return
        val updated = Task(
            id = task.id,
            title = task.title,
            description = task.description,
            category = task.category,
            priority = task.priority,
            isDone = !task.isDone,
            dateCreation = task.dateCreation
        )
        repository.update(updated)
        applyFilters()
    }

    override fun onAddTaskClicked() {
        view?.navigateToTask(null)
    }

    override fun onTaskClicked(taskId: String) {
        view?.navigateToTask(taskId)
    }

    // Filtres additifs + applique les filtres dès que l'on clique sur un checkbox
    // en regénérant la liste à chaque fois
    private fun applyFilters() {
        val tasks = repository.filter(currentCategory, currentPriority, currentIsDone)
        view?.displayTasks(tasks)
    }
}