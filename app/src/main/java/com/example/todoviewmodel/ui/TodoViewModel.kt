package com.example.todoviewmodel.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoviewmodel.data.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TodoViewModel(
  private val repository: TodoRepository = TodoRepository(),
) : ViewModel() {

  val uiState: StateFlow<TodoUiState> =
    repository.todos
      .map { todos -> TodoUiState(todos = todos) }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TodoUiState(),
      )

  fun addTodo(title: String) {
    if (title.isBlank()) return
    repository.addTodo(title.trim())
  }

  fun toggleTodo(id: Long) {
    repository.toggleTodo(id)
  }

  fun deleteTodo(id: Long) {
    repository.deleteTodo(id)
  }
}
