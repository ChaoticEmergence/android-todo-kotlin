package com.example.tood_lsit_2_the_revenge.model

import java.time.LocalDateTime
import java.util.UUID

class Task (
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val category: Category,
    val priority: Priority,
    val isDone: Boolean,
    val dateCreation: Long
)