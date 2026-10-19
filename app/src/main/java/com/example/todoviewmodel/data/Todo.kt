package com.example.todoviewmodel.data

data class Todo(
  val id: Long,
  val title: String,
  val isDone: Boolean = false,
)
