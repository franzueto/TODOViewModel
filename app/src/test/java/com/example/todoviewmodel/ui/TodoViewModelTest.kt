package com.example.todoviewmodel.ui

import com.example.todoviewmodel.data.TodoRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TodoViewModelTest {

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: TodoViewModel

  // Create the ViewModel in @Before so it is built after MainDispatcherRule has replaced Dispatchers.Main.
  @Before
  fun setUp() {
    viewModel = TodoViewModel(TodoRepository())
  }

  // uiState uses WhileSubscribed, so it only updates while someone is collecting it.
  private fun TestScope.collectUiState() {
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }
  }

  @Test
  fun initialState_isEmpty() = runTest {
    collectUiState()

    assertTrue(viewModel.uiState.value.isEmpty)
  }

  @Test
  fun addTodo_addsTodoToTheList() = runTest {
    collectUiState()

    viewModel.addTodo("Buy milk")

    val todos = viewModel.uiState.value.todos
    assertEquals(1, todos.size)
    assertEquals("Buy milk", todos.first().title)
  }

  @Test
  fun addTodo_withBlankTitle_isIgnored() = runTest {
    collectUiState()

    viewModel.addTodo("   ")

    assertTrue(viewModel.uiState.value.isEmpty)
  }

  @Test
  fun toggleTodo_updatesRemainingCount() = runTest {
    collectUiState()
    viewModel.addTodo("Buy milk")
    viewModel.addTodo("Walk the dog")

    viewModel.toggleTodo(viewModel.uiState.value.todos.first().id)

    assertEquals(1, viewModel.uiState.value.remainingCount)
  }

  @Test
  fun deleteTodo_removesTodoFromTheList() = runTest {
    collectUiState()
    viewModel.addTodo("Buy milk")

    viewModel.deleteTodo(viewModel.uiState.value.todos.first().id)

    assertTrue(viewModel.uiState.value.isEmpty)
  }
}
