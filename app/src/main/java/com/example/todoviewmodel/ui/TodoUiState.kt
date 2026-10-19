package com.example.todoviewmodel.ui

import com.example.todoviewmodel.data.Todo

/** Everything the TODO screen needs to draw itself. */
data class TodoUiState(
  val todos: List<Todo> = emptyList(),
) {
  val remainingCount: Int
    get() = todos.count { !it.isDone }

  val isEmpty: Boolean
    get() = todos.isEmpty()
}
