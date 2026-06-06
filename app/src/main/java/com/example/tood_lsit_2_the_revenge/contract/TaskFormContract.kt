package com.example.tood_lsit_2_the_revenge.contract

import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task

class TaskFormContract {
    interface View {
        fun displayTask(task: Task)
        fun navigateToList()
        fun showTitleError()
    }

    interface Presenter {
        fun onSaveTask(taskId: String?, title: String, description: String, category: Category, priority: Priority)
        fun onCancelClicked()
        fun onViewDestroyed()
    }
}