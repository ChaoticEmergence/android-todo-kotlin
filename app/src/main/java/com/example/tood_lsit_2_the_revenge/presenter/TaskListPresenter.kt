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

    override fun onViewCreated() {
        val tasks = repository.getAll()
        view?.displayTasks(tasks)
    }
    override fun onResume() {
        val tasks = repository.getAll()
        view?.displayTasks(tasks)
    }
    override fun onFilterByCategory(category: Category?) {
        val tasks = if (category != null) {
            repository.filterByCategory(category)
        } else {
            repository.getAll()
        }
        view?.displayTasks(tasks)
    }

    override fun onFilterByPriority(priority: Priority?) {
        val tasks = if (priority != null) {
            repository.filterByPriority(priority)
        } else {
            repository.getAll()
        }
        view?.displayTasks(tasks)
    }

    override fun onFilterByCompletionStatus(isDone: Boolean?) {
        val tasks = if (isDone != null) {
            repository.filterByCompletionStatus(isDone)
        } else {
            repository.getAll()
        }
        view?.displayTasks(tasks)
    }

    override fun onViewDestroyed() {
        view = null
    }
    override fun onDeleteTask(taskId: String) {
        repository.delete(taskId)
        val tasks = repository.getAll()
        view?.displayTasks(tasks)
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
        val tasks = repository.getAll()
        view?.displayTasks(tasks)
    }


    override fun onAddTaskClicked() {
        view?.navigateToTask(null)
    }

    override fun onTaskClicked(taskId: String) {
        view?.navigateToTask(taskId)
    }

}