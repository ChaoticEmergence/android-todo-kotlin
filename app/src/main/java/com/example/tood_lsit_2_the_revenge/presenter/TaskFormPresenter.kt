package com.example.tood_lsit_2_the_revenge.presenter

import com.example.tood_lsit_2_the_revenge.contract.TaskFormContract
import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task
import com.example.tood_lsit_2_the_revenge.repository.TaskRepository

class TaskFormPresenter(
    private var view: TaskFormContract.View?,
    private val repository: TaskRepository
) : TaskFormContract.Presenter {

    override fun onViewCreated(taskId: String?) {
        if (taskId != null) {
            val task = repository.getById(taskId) ?: return
            view?.displayTask(task)
        } else {
            val emptyTask = Task(
                title = "",
                description = "",
                category = Category.PERSONAL,
                priority = Priority.MEDIUM,
                isDone = false,
                dateCreation = System.currentTimeMillis()
            )
            view?.displayTask(emptyTask)
        }
    }
    override fun onCancelClicked() {
        view?.navigateToList()
    }

    override fun onViewDestroyed() {
        view = null
    }

    override fun onSaveTask(
        taskId: String?,
        title: String,
        description: String,
        category: Category,
        priority: Priority
    ) {
        if (title.isBlank()) {
            view?.showTitleError()
            return
        }

        if (taskId != null) {
            val existing = repository.getById(taskId) ?: return
            val updated = Task(
                id           = existing.id,
                title        = title,
                description  = description,
                category     = category,
                priority     = priority,
                isDone       = existing.isDone,
                dateCreation = existing.dateCreation
            )
            repository.update(updated)
        } else {
            val newTask = Task(
                title        = title,
                description  = description,
                category     = category,
                priority     = priority,
                isDone       = false,
                dateCreation = System.currentTimeMillis()
            )
            repository.add(newTask)
        }

        view?.navigateToList()
    }
}