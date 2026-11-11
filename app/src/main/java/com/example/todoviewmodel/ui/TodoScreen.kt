package com.example.todoviewmodel.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoviewmodel.R
import com.example.todoviewmodel.data.Todo
import com.example.todoviewmodel.theme.TODOViewModelTheme

/** Stateful screen: connects the ViewModel to the UI. */
@Composable
fun TodoScreen(
  modifier: Modifier = Modifier,
  viewModel: TodoViewModel = viewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  TodoContent(
    uiState = uiState,
    onAddTodo = viewModel::addTodo,
    onToggleTodo = viewModel::toggleTodo,
    onDeleteTodo = viewModel::deleteTodo,
    modifier = modifier,
  )
}

/** Stateless UI: draws [uiState] and reports user events through callbacks. */
@Composable
fun TodoContent(
  uiState: TodoUiState,
  onAddTodo: (String) -> Unit,
  onToggleTodo: (Long) -> Unit,
  onDeleteTodo: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {
  // The text being typed is UI element state, so it stays in the UI.
  // rememberSaveable keeps it across configuration changes.
  var newTodoText by rememberSaveable { mutableStateOf("") }

  fun submit() {
    onAddTodo(newTodoText)
    newTodoText = ""
  }

  Column(modifier = modifier.fillMaxSize()) {
    Text(text = "My tasks", style = MaterialTheme.typography.headlineMedium)
    Text(
      text = if (uiState.isEmpty) "Nothing to do yet" else "${uiState.remainingCount} of ${uiState.todos.size} remaining",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      OutlinedTextField(
        value = newTodoText,
        onValueChange = { newTodoText = it },
        label = { Text("New task") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { submit() }),
        modifier = Modifier.weight(1f),
      )
      Button(onClick = { submit() }, enabled = newTodoText.isNotBlank()) {
        Text("Add")
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (uiState.isEmpty) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
          text = "Add your first task above",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    } else {
      LazyColumn {
        items(uiState.todos, key = { it.id }) { todo ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Checkbox(
              checked = todo.isDone,
              onCheckedChange = { onToggleTodo(todo.id) },
            )
            Text(
              text = todo.title,
              style = MaterialTheme.typography.bodyLarge,
              textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
              color = if (todo.isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.weight(1f),
            )
            IconButton(onClick = { onDeleteTodo(todo.id) }) {
              Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "Delete ${todo.title}",
              )
            }
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun TodoContentEmptyPreview() {
  TODOViewModelTheme {
    TodoContent(uiState = TodoUiState(), onAddTodo = {}, onToggleTodo = {}, onDeleteTodo = {})
  }
}

@Preview(showBackground = true)
@Composable
fun TodoContentPreview() {
  val todos =
    listOf(
      Todo(id = 1, title = "Buy milk", isDone = true),
      Todo(id = 2, title = "Walk the dog"),
      Todo(id = 3, title = "Study ViewModel"),
    )
  TODOViewModelTheme {
    TodoContent(uiState = TodoUiState(todos = todos), onAddTodo = {}, onToggleTodo = {}, onDeleteTodo = {})
  }
}
