# Codelab: TODO con ViewModel y State

Entrega mediante README (opción A). Repositorio de entrega: [macusmarroquin93/TODOViewModel](https://github.com/macusmarroquin93/TODOViewModel). Proyecto base: [franzueto/TODOViewModel](https://github.com/franzueto/TODOViewModel). Guía seguida: [Improve the TODO app with ViewModel and State](codelab/index.html).

## Trabajo realizado

Se refactorizó la aplicación de tareas de Kotlin y Jetpack Compose siguiendo los pasos 4 al 11 de la guía. La aplicación permite agregar tareas, marcarlas como completadas, eliminarlas y consultar cuántas quedan pendientes.

- `data/Todo.kt`: modelo inmutable de una tarea.
- `data/TodoRepository.kt`: almacena la lista en memoria y la expone mediante un `StateFlow` de solo lectura.
- `ui/TodoUiState.kt`: estado de pantalla con lista, contador de pendientes e indicador de lista vacía.
- `ui/TodoViewModel.kt`: valida títulos, elimina espacios y procesa los eventos; convierte los datos en estado de UI usando `stateIn` y `viewModelScope`.
- `ui/TodoScreen.kt`: recoge el estado con `collectAsStateWithLifecycle` y conecta los eventos a `TodoContent`. Incluye previews vacía y con tareas.

Los conceptos practicados son separación de responsabilidades, flujo unidireccional de datos, listas inmutables, `StateFlow`, ciclo de vida, estado derivado y pruebas locales con coroutines.

## Problema y solución

Antes, la lista estaba dentro del composable con `remember`: al recrear la Activity se perdían las tareas. Ahora el repositorio pertenece al ViewModel, que conserva su instancia durante cambios de configuración. El texto que todavía no se agrega se conserva con `rememberSaveable`.

Las tareas siguen en memoria: no se garantiza conservarlas al terminar la pantalla o morir el proceso. Este codelab no incluye una base de datos.

## Verificación

Requisitos: Android SDK 36, JDK 17 para el toolchain y un dispositivo Android API 24 o superior. Configurar la ruta del SDK en Android Studio o en `local.properties` (archivo local excluido de Git).

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug
.\gradlew.bat installDebug
```

Las cinco pruebas del codelab comprueban estado inicial, agregar una tarea, rechazar títulos en blanco, actualizar pendientes al completar y eliminar una tarea. `MainDispatcherRule` reemplaza el dispatcher principal; cada prueba mantiene una suscripción a `uiState` porque usa `WhileSubscribed`.

Validación realizada: `testDebugUnitTest assembleDebug` terminó con **BUILD SUCCESSFUL**; las **5 pruebas pasaron**, sin fallos ni errores, y se generó `app/build/outputs/apk/debug/app-debug.apk`. En este equipo se indicó a Gradle la ruta del JDK 17 local mediante `-Porg.gradle.java.installations.paths=...`; esta ruta depende de cada instalación.

### Evidencia y pasos pendientes para entregar

No se adjunta todavía una captura: el emulador x86_64 instalado no pudo iniciar por falta de aceleración compatible en el equipo ARM. No hay una ejecución visual verificada.

1. Abrir este proyecto en Android Studio y ejecutar en un teléfono o emulador compatible.
2. Agregar tres tareas, completar una y escribir texto sin enviarlo. Rotar y comprobar que se conservan la lista, la tarea completada y el texto. Eliminar una tarea y revisar el contador.
3. Guardar una captura real en `docs/capturas/todo-funcionando.png` e insertarla aquí con `![App funcionando](docs/capturas/todo-funcionando.png)`.
4. Revisar esta explicación y expresar lo aprendido con tus propias palabras y experiencia.
5. Comprobar que el repositorio sea público o tenga acceso para el docente. Verificar el enlace en incógnito y entregarlo en Canvas.

La implementación y este borrador de documentación se prepararon con asistencia de IA; la revisión personal y la ejecución del ejercicio siguen pendientes.

---

## README original del proyecto base

El contenido siguiente describe el punto de partida y conserva la guía original; la implementación actual es la descrita arriba.

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
