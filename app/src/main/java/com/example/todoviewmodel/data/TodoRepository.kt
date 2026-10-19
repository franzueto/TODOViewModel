package com.example.todoviewmodel.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Single source of truth for the list of TODOs.
 *
 * The data is kept in memory. Because only this class knows how the data is stored,
 * it can later be backed by a database (for example Room) without changing the UI.
 */
class TodoRepository {

  private val _todos = MutableStateFlow<List<Todo>>(emptyList())
  val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

  private var nextId = 0L

  fun addTodo(title: String) {
    _todos.update { todos -> todos + Todo(id = nextId++, title = title) }
  }

  fun toggleTodo(id: Long) {
    _todos.update { todos ->
      todos.map { todo -> if (todo.id == id) todo.copy(isDone = !todo.isDone) else todo }
    }
  }

  fun deleteTodo(id: Long) {
    _todos.update { todos -> todos.filterNot { it.id == id } }
  }
}
