# American Football Scoreboard — JavaFX MVC Application

A desktop application built with Java and JavaFX that provides a real-time American football scoreboard with a dedicated control panel and a separate live display window.

---

## Project Overview

This application simulates a two-window scoreboard system used for tracking an American football game. One window — the **Control Panel** — is operated by the scorekeeper. The other window — the **Scoreboard Display** — shows the live score and is intended for the audience.

The scorekeeper can enter team names, award points using standard football scoring increments, undo mistakes, and reset the game at any time. Every action is immediately reflected on the display window.

---

## MVC Design

This project strictly follows the **Model–View–Controller (MVC)** architectural pattern.

**Model (`model/Scoreboard.java`)**
The model is a pure Java class with zero JavaFX dependencies. It owns all game state: team names, scores, last action description, and a stack-based history for undo support. It enforces all business rules (e.g., names must be set before scoring, points must be positive) by throwing `IllegalArgumentException` or `IllegalStateException` when rules are violated. The controller and view never modify state directly — they go through the model.

**View (`view/Control.fxml`, `view/Scoreboard.fxml`, `view/style.css`)**
The view layer is defined entirely in FXML (built with SceneBuilder) and styled with a shared CSS file. It has no logic — it only describes layout and binds UI element IDs to controller fields via `fx:id`. This makes the UI easy to redesign without touching any Java.

**Controller (`controller/ControlController.java`, `controller/DisplayController.java`)**
The application opens two independent stages on launch: There is a Control Panel window for the scorekeeper and a Scoreboard Display window for the audience to see the current score of the game. Each stage has its own FXML view file along with its own dedicated controller file.
Two controllers bridge the model and the view. `ControlController` handles all user input events (button clicks, text field reads) and calls the appropriate model methods. `DisplayController` is responsible for reading state out of the model and pushing it to the display labels. Neither controller contains game logic — they only coordinate between the model and the UI.

---

## Features

- **Team name input** — Enter custom home and away team names via text fields; names are validated before being accepted.
- **Scoring buttons** — Award `+6` (touchdown), `+3` (field goal), `+2` (safety/conversion), or `+1` (PAT) points with a single click.
- **Team selection** — Radio buttons let the scorekeeper choose which team receives the points.
- **Undo last action** — Reverts the most recent scoring event and restores the previous score.
- **Clear/restart game** — Resets both scores and action history to zero while preserving team names.
- **Live score display** — A separate stage updates instantly after every action.
- **Last action display** — Shows a human-readable description of the most recent event.
- **Input validation and alerts** — JavaFX `Alert` dialogs appear for blank names, missing team selection, or undo with no history.

---

## How to Build and Run

### Prerequisites

- Java 17 or later
- JavaFX SDK 17+ downloaded separately (e.g., from [https://openjfx.io](https://openjfx.io))
- JavaFX is **not** bundled with the JDK, so you must supply `--module-path` and `--add-modules`.

### Project Structure

```
CSCE-314-Capstone/
├── controller/
│   ├── ControlController.java
│   └── DisplayController.java
├── model/
│   └── Scoreboard.java
├── view/
│   ├── Control.fxml
│   ├── Scoreboard.fxml
│   └── style.css
└── Main.java
```

### Compile

Replace `/path/to/javafx-sdk/lib` with the actual path to your JavaFX SDK's `lib` folder.

**Windows**
```bat
javac --module-path "C:\javafx-sdk\lib" ^
      --add-modules javafx.controls,javafx.fxml ^
      -d out ^
      Main.java controller\*.java model\*.java
```

**macOS / Linux**
```bash
javac --module-path /path/to/javafx-sdk/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d out \
      Main.java controller/*.java model/*.java
```

### Copy View Resources

The FXML and CSS files must be on the classpath under `view/`:

**Windows**
```bat
xcopy view out\view /E /I
```

**macOS / Linux**
```bash
cp -r view out/view
```

### Run

**Windows**
```bat
java --module-path "C:\javafx-sdk\lib" ^
     --add-modules javafx.controls,javafx.fxml ^
     -cp out ^
     Main
```

**macOS / Linux**
```bash
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp out \
     Main
```

---

## Model API Summary

All game logic lives in `model.Scoreboard`. The following public methods make up its API:

| Method | Description |
|---|---|
| `setTeamNames(String home, String away)` | Sets both team names. Throws `IllegalArgumentException` if either name is null or blank. |
| `addPointsToHome(int points)` | Adds points to the home team. Throws `IllegalStateException` if names are not set; throws `IllegalArgumentException` if points ≤ 0. Records the action in history. |
| `addPointsToAway(int points)` | Same as above for the away team. |
| `undoLast()` | Removes the most recent scoring action and reverses its effect. Throws `IllegalStateException` if history is empty. |
| `restart()` | Resets both scores and clears history. Team names are preserved. |
| `getHomeName()` | Returns the home team's name. |
| `getAwayName()` | Returns the away team's name. |
| `getHomeScore()` | Returns the current home score as an `int`. |
| `getAwayScore()` | Returns the current away score as an `int`. |
| `getLastActionDescription()` | Returns a human-readable description of the last event. |
| `hasTeamName()` | Returns `true` if both team names have been set (non-empty). |
| `hasHistory()` | Returns `true` if at least one scoring action exists in the history stack. |

---

## How to Run Unit Tests

The test class `ScoreboardTests.java` uses a plain `main` method — no JUnit or external libraries required.

**Compile** (from the project root, alongside the model):
```bash
javac -d out model/Scoreboard.java model/ScoreboardTests.java
```

**Run:**
```bash
java -cp out model/ScoreboardTests
```

Each test prints either `PASS: <test name>` or `FAIL: <reason>` to the console. A summary line at the end reports total passed and failed.

---

## Known Limitations

- **No persistence** — Game state is held entirely in memory. Closing the application discards all data.
- **No timer or clock** — The scoreboard does not track game time or quarters.
- **No network support** — Both windows must run on the same machine; there is no remote display capability.
- **No negative score protection** — Undoing a score that was added before `restart()` is not possible, but a sequence of undos can theoretically bring a score below zero if history is manipulated in unexpected ways.
- **Single game session** — There is no save/load functionality and no support for resuming a previous game.
- **Basic UI** — No animations, no sound effects, and no per-quarter score breakdown.

## References

- AI assistance provided by Claude (Anthropic) for debugging and development guidance.