# Mines Project Development Guidelines

This document provides essential information for developers working on the Mines project.

## Build/Configuration Instructions

### Project Overview

Mines is a KMP Minesweeper clone with Compose Multiplatform for UI targeting Android, JVM (Desktop), and WebAssembly JS (Browser).

### Prerequisites

- JDK 11 (for Android compatibility)
- Android SDK (for Android builds)
- Latest Gradle version

### Building the Project

For all platforms: `./gradlew build`

Platform-specific builds:

- Android: `./gradlew assembleDebug` or `./gradlew assembleRelease`
- Desktop: `./gradlew packageDistributionForCurrentOS`
- Web: `./gradlew wasmJsBrowserDistribution`

### Running the Application

- Android: Use IntelliJ IDEA with Android plugin or `./gradlew installDebug`
- Desktop: `./gradlew run`
- Web: `./gradlew wasmJsBrowserRun`

## Testing Information

### Test Structure

The project follows Kotlin Multiplatform conventions:

- `commonTest`: Platform-independent tests
- `jvmTest`: JVM-specific tests
- `androidTest`: Android-specific tests
- `wasmJsTest`: WebAssembly JS-specific tests

Note: Desktop (JVM) is the primary development platform.

### Running Tests

All tests: `./gradlew allTests`

Platform-specific tests:

- JVM: `./gradlew jvmTest`
- Android: `./gradlew androidTest`
- WebAssembly JS: `./gradlew wasmJsTest`

Specific test: `./gradlew test --tests "de.stefan_oltmann.mines.model.GameDifficultyTest"`

Important: Place all logic/non-UI tests in `commonTest` and test with `./gradlew jvmTest`. No need to run `androidTest` or `wasmJsTest` if `jvmTest` passes.

### Adding New Tests

1. Create a test file in the appropriate directory:
    - Common: `app/src/commonTest/kotlin/de/stefan_oltmann/mines/...`
    - JVM: `app/src/jvmTest/kotlin/de/stefan_oltmann/mines/...`
    - Android: `app/src/androidTest/kotlin/de/stefan_oltmann/mines/...`
    - WebAssembly JS: `app/src/wasmJsTest/kotlin/de/stefan_oltmann/mines/...`

2. Use the Kotlin Test framework:
   ```kotlin
   class MyTest {
       @Test
       fun testSomething() {
           assertEquals(expected, actual)
       }
   }
   ```

3. Configure dependencies in `app/build.gradle.kts`:
   ```kotlin
   commonTest.dependencies {
       implementation(libs.kotlin.test)
   }
   jvmTest.dependencies {
       implementation(libs.kotlin.test.junit)
   }
   ```

### Compose UI Testing

Use Compose Desktop UI testing framework (not Android UI testing).

Setup:

1. Add dependencies:
   ```kotlin
   jvmTest.dependencies {
       implementation(compose.desktop.uiTestJUnit4)
       implementation(compose.desktop.currentOs)
   }
   ```

2. Create UI tests in `jvmTest`:
   ```kotlin
   class MyComposeUiTest {
       @get:Rule
       val composeTestRule = createComposeRule()

       @Test
       fun testMyComposable() {
           composeTestRule.setContent {
               MyComposable()
           }
           /* Verify UI elements */
           composeTestRule.onNodeWithText("Expected Text").assertIsDisplayed()
           /* Perform actions */
           composeTestRule.onNodeWithContentDescription("Button").performClick()
           /* Verify state changes */
           composeTestRule.onNodeWithText("Updated Text").assertIsDisplayed()
       }
   }
   ```

For details: [Compose Desktop UI Testing documentation](https://www.jetbrains.com.cn/en-us/help/kotlin-multiplatform-dev/compose-desktop-ui-testing.html)

### Example Test

```kotlin
class GameDifficultyTest {
    @Test
    fun testCalcMineCount() {

        /* Test EASY difficulty (10%) */
        assertEquals(1, GameDifficulty.EASY.calcMineCount(3, 3))
        assertEquals(4, GameDifficulty.EASY.calcMineCount(10, 4))

        /* Test MEDIUM difficulty (15%) */
        assertEquals(1, GameDifficulty.MEDIUM.calcMineCount(3, 3))
        assertEquals(6, GameDifficulty.MEDIUM.calcMineCount(10, 4))

        /* Test HARD difficulty (20%) */
        assertEquals(1, GameDifficulty.HARD.calcMineCount(3, 3))
        assertEquals(8, GameDifficulty.HARD.calcMineCount(10, 4))
    }
}
```

## Additional Development Information

### Project Structure

- `app/src/commonMain`: Shared code for all platforms
- `app/src/androidMain`: Android-specific code
- `app/src/jvmMain`: Desktop-specific code
- `app/src/wasmJsMain`: WebAssembly JS-specific code

### Key Components

- `GameConfig`: Configuration (cell size, map dimensions, difficulty)
- `GameDifficulty`: Difficulty levels (EASY, MEDIUM, HARD)
- `GameState`: Game state management

### Versioning

Uses `androidGitVersion` plugin with version numbers derived from Git tags.

### Code Style

Follow Clean Code practices and official Kotlin conventions plus these rules:

1. **Indentation and Spacing**
    - 4 spaces indentation, 120 character line limit
    - No braces for single-line if statements
    - Use blank lines to separate logical blocks
    - No consecutive blank lines
    - Add blank line before if/for/while statements
    - Don't separate consecutive assert statements
    - Add blank line between function signature and body for multi-line functions
    - Example:
      ```kotlin
      fun calculateValue(param1: Int, param2: Int): Int {

          val result = param1 + param2

          return result
      }
      ```

2. **Naming Conventions**
    - Constants: UPPER_SNAKE_CASE with `const val`
    - Classes: PascalCase
    - Functions/variables: camelCase
    - Packages: lowercase.with.dots

3. **Imports**: Alphabetical order, no wildcards

4. **Function Formatting**: Align parameters in multi-line declarations, use trailing commas

5. **Type Declarations**: Explicit types for public APIs

6. **Braces**: Opening at line end, closing on own line, use braces for multi-line bodies only

7. **String Templates**: Prefer `"Value: $value"` over concatenation

9. **Documentation**: KDoc format for all public APIs

10. **Forbidden Practices**
    - No `print`/`println` (use logger)
    - Don't remove TODO/FIXME/STOPSHIP comments unless resolved
    - No magic numbers

11. **Comment Style**
    - Use only block comments (`/* */`), never line comments (`//`)
    - Align stars in multi-line comments
    - Each file needs license header as block comment

### Dependencies Management

Managed in `gradle/libs.versions.toml` using Gradle's version catalog.

### UI Framework

Compose Multiplatform for UI. No platform-specific UI libraries.

### Final Checks

- Ensure code compiles and tests pass
- Remove unused code and imports
