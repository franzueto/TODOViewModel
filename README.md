# TODO App

A simple Android TODO app built with Kotlin and Jetpack Compose.

- Add a task
- Mark a task as done (or not done)
- Delete a task
- See how many tasks are still remaining

Tasks are kept in a local, in-memory list.

## Requirements

- Android Studio (latest stable version)
- Android SDK 36
- A device or emulator running Android 7.0 (API 24) or newer

## Get the code

```bash
git clone https://github.com/franzueto/TODOViewModel.git
```

## Run the app

Open the project in Android Studio and click **Run**, or from the command line:

```bash
./gradlew installDebug
```

## Project structure

```
app/src/main/java/com/example/todoviewmodel/
├── MainActivity.kt        # Hosts the Compose UI
├── theme/                 # Material 3 theme
└── ui/
    └── TodoScreen.kt      # The TODO screen: UI, state and logic
```

## Improving the app

All of the app's state and logic lives inside the `TodoScreen` composable, using `remember`. This keeps the code short, but it has some problems:

- **Tasks are lost on configuration changes.** Rotate the device, switch to dark mode or resize the window and the list is empty again, because `remember` does not survive the `Activity` being recreated.
- **UI and logic are mixed together.** Generating ids, validating input and updating the list all happen inside the UI code.
- **It is hard to test.** The logic can only run inside a composable.
- **It is hard to preview.** The preview can only show an empty list.

The app can be improved by splitting it into layers that follow unidirectional data flow:

```
 ┌──────────────┐  events   ┌───────────────┐  events   ┌────────────────┐
 │  TodoScreen  │ ────────▶ │ TodoViewModel │ ────────▶ │ TodoRepository │
 │  (Compose)   │ ◀──────── │               │ ◀──────── │                │
 └──────────────┘  UI state └───────────────┘   data    └────────────────┘
```

| Piece | Responsibility |
| --- | --- |
| `Todo` | The data model. |
| `TodoRepository` | Single source of truth for the list of tasks. Exposes them as a `StateFlow`. |
| `TodoUiState` | An immutable snapshot of everything the screen needs to draw, including derived values such as the remaining count. |
| `TodoViewModel` | Survives configuration changes, turns repository data into `TodoUiState` and handles user events. |
| `TodoScreen` / `TodoContent` | A stateful composable that connects to the ViewModel and a stateless composable that only draws the state and reports events. |

### Step-by-step codelab

The [`codelab/`](codelab/index.html) folder contains a step-by-step guide that walks through the refactor. Open `codelab/index.html` in any web browser. It works offline and needs no build step.

### Follow the codelab with an AI agent

You can also work through the codelab with an AI coding agent that can read this project, such as Claude Code or the agent in Android Studio. The agent acts as a tutor: it explains each change, makes the change with you one small piece at a time, and checks that you understood before moving on.

1. Clone the repository and stay on the `main` branch.
2. Open the project folder in your AI agent.
3. Paste the prompt below.

```text
You are my tutor for this Android project. I want to learn how to improve this
TODO app with a ViewModel, a UI state class and a repository. This is a learning
activity, so teach me instead of doing the work for me.

Read codelab/index.html first, then guide me through it step by step, in order.

For every step:
1. Before touching any code, explain in simple words what we are going to change
   and why, and which problem of the current app it solves.
2. Make one small change at a time. Before each change, ask me whether I want to
   type it myself or want you to make it.
3. After each change, show me the code and explain what every new or changed line
   does. Point out the Kotlin and Jetpack Compose concepts involved.
4. Ask me a short question to check that I understood. If my answer is wrong or
   incomplete, explain it in a different way and ask again.
5. When the step can be verified (Gradle sync, building, running the app, rotating
   the device, running the tests), tell me how to check it and wait for me to
   confirm that it works.
6. Summarize what I learned in the step and wait until I say I'm ready before
   starting the next one.

Rules:
- Work on the main branch. Do not look at or copy from the viewmodel-solution
  branch unless I ask you to compare my work with it.
- Do not skip steps, combine steps, or change files that the current step does
  not mention.
- Keep the code the same as in the codelab. Do not add dependency injection or
  libraries that the codelab does not use.
- If I make a mistake, give me hints to find it myself before showing the answer.
- If I ask a question that is off topic, answer it briefly and then return to the
  current step.

Start with a short overview of the app and of what we will build, then begin with
step 1.
```

## Branches

| Branch | Contents |
| --- | --- |
| `main` | The starting point: everything in a single composable. |
| `viewmodel-solution` | The finished refactor with a ViewModel, a UI state class, a repository and unit tests. |

To compare the two, run the command below or open the [comparison on GitHub](https://github.com/franzueto/TODOViewModel/compare/main...viewmodel-solution).

```bash
git diff main origin/viewmodel-solution -- app
```
