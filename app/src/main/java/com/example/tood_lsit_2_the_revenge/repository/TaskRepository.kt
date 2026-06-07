package com.example.tood_lsit_2_the_revenge.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.tood_lsit_2_the_revenge.database.ToodLsitDbHelper
import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task

class TaskRepository(context: Context) {
    private val helper = ToodLsitDbHelper(context)

    fun add(task: Task) {
        val db = helper.writableDatabase
        db.insert("task", null, taskToDb(task))
    }

    fun update(task: Task) {
        val db = helper.writableDatabase
        db.update(
            "task",
            taskToDb(task),
            "id = ?",
            arrayOf(task.id)
        )
    }

    fun delete(id: String) {
        val db = helper.writableDatabase
        db.delete("task", "id = ?", arrayOf(id))
    }

    fun getById(id: String): Task? {
        val db = helper.readableDatabase
        val cursor = db.query(
            "task",
            null,
            "id = ?",
            arrayOf(id),
            null, null, null
        )
       cursor.use {
           return if (it.moveToFirst()) cursorToTask(it) else null
        }
    }

    fun getAll(): List<Task> = queryTasks(null, null)

    fun filterByCategory(category: Category): List<Task> =
        queryTasks("category = ?", arrayOf(category.name))

    fun filterByPriority(priority: Priority): List<Task> =
        queryTasks("priority = ?", arrayOf(priority.name))

    fun filterByCompletionStatus(isDone: Boolean): List<Task> =
        queryTasks("is_done = ?", arrayOf(if (isDone) "1" else "0"))

    // Ajout pour rendre les filtres additifs
    fun filter(category: Category?, priority: Priority?, isDone: Boolean?): List<Task> {
        val conditions = mutableListOf<String>()
        val args = mutableListOf<String>()

        if (category != null) {
            conditions.add("category = ?")
            args.add(category.name)
        }
        if (priority != null) {
            conditions.add("priority = ?")
            args.add(priority.name)
        }
        if (isDone != null) {
            conditions.add("is_done = ?")
            args.add(if (isDone) "1" else "0")
        }

        val selection = if (conditions.isEmpty()) null else conditions.joinToString(" AND ")
        val selectionArgs = if (args.isEmpty()) null else args.toTypedArray()

        return queryTasks(selection, selectionArgs)
    }
    // L'implémentation et la structure de cette méthode ont été générées en grande partie par l'IA
    // Je ne savais pas comment gérer des queries redondants mais je savais qu'il serait bien de le faire
    private fun queryTasks(selection: String?, selectionArgs: Array<String>?): List<Task> {
        val db = helper.readableDatabase
        val cursor = db.query(
            "task",
            null,
            selection,
            selectionArgs,
            null, null,
            "date_creation DESC"
        )
        val tasks = mutableListOf<Task>()
        cursor.use {
            while (it.moveToNext()) {
                tasks.add(cursorToTask(it))
            }
        }
        return tasks
    }

    private fun cursorToTask(cursor: Cursor): Task {
        return Task(
            id           = cursor.getString(cursor.getColumnIndexOrThrow("id")),
            title        = cursor.getString(cursor.getColumnIndexOrThrow("title")),
            description  = cursor.getString(cursor.getColumnIndexOrThrow("description")),
            category     = Category.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("category"))),
            priority     = Priority.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("priority"))),
            isDone       = cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) == 1,
            dateCreation = cursor.getString(cursor.getColumnIndexOrThrow("date_creation")).toLong()
        )
    }

    private fun taskToDb(task: Task): ContentValues {
        return ContentValues().apply {
            put("id",            task.id)
            put("title",         task.title)
            put("description",   task.description)
            put("category",      task.category.name)
            put("priority",      task.priority.name)
            put("is_done",       if (task.isDone) 1 else 0)
            put("date_creation", task.dateCreation.toString())
        }
    }
}