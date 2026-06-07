package com.example.tood_lsit_2_the_revenge.contract

import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task

class TaskListContract {
    interface View {
        fun displayTasks(tasks: List<Task>)
        fun navigateToTask(taskId: String?)
    }
    interface Presenter {
        fun onAddTaskClicked()
        fun onTaskClicked(taskId: String)
        fun onDeleteTask(taskId: String)
        fun onTaskDoneClicked(taskId: String)
        fun onFilterByCategory(category: Category?)
        fun onFilterByPriority(priority: Priority?)
        fun onFilterByCompletionStatus(isDone: Boolean?)
        fun onViewDestroyed()
        fun onViewCreated()
        fun onResume()
    }
}