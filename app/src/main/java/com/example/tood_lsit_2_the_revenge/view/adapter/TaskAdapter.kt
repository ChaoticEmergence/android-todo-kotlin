package com.example.tood_lsit_2_the_revenge.view.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tood_lsit_2_the_revenge.R
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClicked: (String) -> Unit,
    private val onTaskDoneClicked: (String) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm")
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val tvCategory: TextView = itemView.findViewById(R.id.tvTaskCategory)
        val cbDone: CheckBox = itemView.findViewById(R.id.cbTaskDone)
        val viewPriorityIndicator: View = itemView.findViewById(R.id.viewPriorityIndicator)
        val tvDate: TextView = itemView.findViewById(R.id.tvTaskDate)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.tvTitle.text = task.title
        holder.tvDescription.text = task.description
        holder.tvDescription.visibility = if (task.description.isBlank()) View.GONE else View.VISIBLE
        holder.tvCategory.text = task.category.name

        val localDate = Instant.ofEpochMilli(task.dateCreation)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
        holder.tvDate.text = localDate

        if (task.isDone) {
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = task.isDone
        holder.cbDone.setOnCheckedChangeListener { _, _ ->
            onTaskDoneClicked(task.id)
        }

        val colorRes = when (task.priority) {
            Priority.HIGH   -> R.color.priority_high
            Priority.MEDIUM -> R.color.priority_medium
            Priority.LOW    -> R.color.priority_low
        }
        holder.viewPriorityIndicator.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, colorRes)
        )

        holder.itemView.setOnClickListener {
            onTaskClicked(task.id)
        }
    }
    override fun getItemCount(): Int = tasks.size
    fun getTaskAtPosition(position: Int): Task = tasks[position]
    fun updateTasks(newTasks: List<Task>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = tasks.size
            override fun getNewListSize(): Int = newTasks.size

            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                tasks[oldPos].id == newTasks[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                tasks[oldPos] == newTasks[newPos]
        })
        tasks = newTasks
        diffResult.dispatchUpdatesTo(this)
    }
}