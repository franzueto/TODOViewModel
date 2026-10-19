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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoviewmodel.R
import com.example.todoviewmodel.theme.TODOViewModelTheme

data class Todo(
  val id: Long,
  val title: String,
  val isDone: Boolean = false,
)

@Composable
fun TodoScreen(modifier: Modifier = Modifier) {
  // All of the screen's data and logic lives inside this composable.
  val todos = remember { mutableStateListOf<Todo>() }
  var nextId by remember { mutableLongStateOf(0L) }
  var newTodoText by remember { mutableStateOf("") }

  val remainingCount = todos.count { !it.isDone }

  fun addTodo() {
    if (newTodoText.isBlank()) return
    todos.add(Todo(id = nextId, title = newTodoText.trim()))
    nextId++
    newTodoText = ""
  }

  Column(modifier = modifier.fillMaxSize()) {
    Text(text = "My tasks", style = MaterialTheme.typography.headlineMedium)
    Text(
      text = if (todos.isEmpty()) "Nothing to do yet" else "$remainingCount of ${todos.size} remaining",
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
        keyboardActions = KeyboardActions(onDone = { addTodo() }),
        modifier = Modifier.weight(1f),
      )
      Button(onClick = { addTodo() }, enabled = newTodoText.isNotBlank()) {
        Text("Add")
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (todos.isEmpty()) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
          text = "Add your first task above",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    } else {
      LazyColumn {
        items(todos, key = { it.id }) { todo ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Checkbox(
              checked = todo.isDone,
              onCheckedChange = { checked ->
                val index = todos.indexOfFirst { it.id == todo.id }
                todos[index] = todo.copy(isDone = checked)
              },
            )
            Text(
              text = todo.title,
              style = MaterialTheme.typography.bodyLarge,
              textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
              color = if (todo.isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.weight(1f),
            )
            IconButton(onClick = { todos.removeAll { it.id == todo.id } }) {
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
fun TodoScreenPreview() {
  TODOViewModelTheme { TodoScreen() }
}
