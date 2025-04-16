# Mines Project Development Guidelines

This document provides essential information for developers working on the Mines project.

## Build/Configuration Instructions

### Project Overview

Mines is a KMP Minesweeper clone with Compose Multiplatform for UI.
It follows the typical game rules.

It targets multiple platforms:
- Android
- JVM (Desktop)
- WebAssembly JS (Browser)

### Prerequisites

- JDK 11 (because of Android)
- Android SDK (for Android builds)
- Latest Gradle version

### Building the Project

To build the entire project for all platforms:

```bash
./gradlew build
```

For platform-specific builds:

- Android: `./gradlew assembleDebug` or `./gradlew assembleRelease`
- Desktop: `./gradlew packageDistributionForCurrentOS`
- Web: `./gradlew wasmJsBrowserDistribution`

### Running the Application

- Android: Use IntelliJ IDEA with Android plugin or `./gradlew installDebug`
- Desktop: `./gradlew run`
- Web: `./gradlew wasmJsBrowserRun`

## Testing Information

### Test Structure

The project follows the Kotlin Multiplatform convention for tests:

- `commonTest`: Tests that run on all platforms
- `jvmTest`: JVM-specific tests
- `androidTest`: Android-specific tests
- `wasmJsTest`: WebAssembly JS-specific tests

Note that Desktop (JVM) is the primary development platform for this project.

### Running Tests

To run all tests:

```bash
./gradlew allTests
```

To run tests for a specific platform:

- JVM: `./gradlew jvmTest`
- Android: `./gradlew androidTest`
- WebAssembly JS: `./gradlew wasmJsTest`

To run a specific test:

```bash
./gradlew test --tests "de.stefan_oltmann.mines.model.GameDifficultyTest"
```

All logic / non-UI tests should be placed into `commonTest` and be tested by running `./gradlew jvmTest`.

There is no need to run `androidTest` or `wasmJsTest`. Assume that they will work if `jvmTest` does.

### Adding New Tests

1. Create a test file in the appropriate test directory:
    - Common tests: `app/src/commonTest/kotlin/de/stefan_oltmann/mines/...`
    - JVM tests: `app/src/jvmTest/kotlin/de/stefan_oltmann/mines/...`
    - Android tests: `app/src/androidTest/kotlin/de/stefan_oltmann/mines/...`
    - WebAssembly JS tests: `app/src/wasmJsTest/kotlin/de/stefan_oltmann/mines/...`

2. Use the Kotlin Test framework:
   ```kotlin
   class MyTest {
       @Test
       fun testSomething() {
           assertEquals(expected, actual)
       }
   }
   ```

3. Configure test dependencies in `app/build.gradle.kts`:
   ```kotlin
   commonTest.dependencies {
       implementation(libs.kotlin.test)
   }

   jvmTest.dependencies {
       implementation(libs.kotlin.test.junit)
   }
   ```

### Compose UI Testing

For UI testing in this project, use Compose Desktop UI testing framework - **NOT** Android UI testing. Desktop is the primary development platform, and we want to avoid the overhead of running tests in the Android emulator.

To set up Compose Desktop UI testing:

1. Add the necessary dependencies in `app/build.gradle.kts`:
   ```kotlin
   jvmTest.dependencies {
       implementation(compose.desktop.uiTestJUnit4)
       implementation(compose.desktop.currentOs)
   }
   ```

2. Create UI tests in the `jvmTest` source set:
   ```kotlin
   class MyComposeUiTest {

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

3. Set up the test rule:
   ```kotlin
   class MyComposeUiTest {

       @get:Rule
       val composeTestRule = createComposeRule()

       /* Test methods */
   }
   ```

For more details, refer to the [Compose Desktop UI Testing documentation](https://www.jetbrains.com.cn/en-us/help/kotlin-multiplatform-dev/compose-desktop-ui-testing.html).

### Example Test

Here's a simple test for the `GameDifficulty.calcMineCount` method:

```kotlin
import kotlin.test.Test
import kotlin.test.assertEquals

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

- `GameConfig`: Configuration for the game (cell size, map dimensions, difficulty)
- `GameDifficulty`: Enum defining difficulty levels (EASY, MEDIUM, HARD)
- `GameState`: Manages the state of the game

### Versioning

The project uses the `androidGitVersion` plugin for versioning.
Version numbers are derived from Git tags.

### Code Style

In general, follow the Clean Code practices.

The following rules are complementary to the official Kotlin coding conventions  
and the detekt rules defined in the project's `detekt.yml` file.

1. **Indentation and Spacing**
    - Use 4 spaces for indentation (no tabs)
    - Maximum line length is 120 characters
    - Use blank lines to separate logical sections of code
    - Use a single blank line between package declaration, imports, and class definition
    - Use a single blank line between a function signature and its body (for multi-line bodies)
        - This rule applies to ALL functions with multi-line bodies, including methods in test files
        - Examples:
            - Correct:
              ```
              fun calculateValue(param1: Int, param2: Int): Int {

                  val result = param1 + param2

                  return result
              }
              ```
            - Incorrect:
              ```
              fun calculateValue(param1: Int, param2: Int): Int {
                  val result = param1 + param2
                  return result
              }
              ```

2. **Naming Conventions**
    - Constants: UPPER_SNAKE_CASE with `const val`
    - Classes: PascalCase
    - Functions and variables: camelCase
    - Package names: lowercase with dots

3. **Imports**
    - Organize imports alphabetically
    - Do not use wildcard imports

4. **Function and Parameter Formatting**
    - Align parameters in multi-line function declarations
    - Use trailing commas in multi-line function calls

5. **Type Declarations**
    - Explicitly declare types (e.g., `Int`, `Long`) for public APIs
    - Avoid relying on type inference for API boundaries

6. **Braces**
    - Opening braces go at the end of the line
    - Closing braces go on their own line
    - Always use braces, even for single-line control structures

7. **String Templates**
    - Prefer string templates (`"Value: $value"`) over string concatenation

8. **Composable Functions**
    - Annotate with `@Composable`
    - Use PascalCase for Composable function names

9. **Documentation**
    - Use KDoc format (`/** */`) for documentation comments
    - Document all public APIs

10. **Forbidden Practices**
    - Do not use `print` or `println` for logging; use a logger
    - Do not remove `TODO`, `FIXME`, or `STOPSHIP` comments unless the issue is resolved
    - Avoid magic numbers; use named constants

11. **Comment Style**
    - Use block comments (`/* */`) only; do not use line comments (`//`) anywhere in the codebase
        - This rule applies to ALL files, including test files and configuration files
    - In multi-line block comments:
        - Align stars at the start of each line
        - Include a space after each star
    - For single-line block comments, use: `/* Comment */`
    - Each file should start with a license header as a block comment
    - Examples:
        - Correct: `/* This is a comment */`
        - Incorrect: `// This is a comment`
        - Correct:
          ```
          /* 
           * This is a multi-line comment
           * with properly aligned stars
           */
          ```

### Dependencies Management

Dependencies are managed in `gradle/libs.versions.toml` using Gradle's version catalog feature.

### UI Framework

The project uses Compose Multiplatform for UI, which allows sharing UI code across all platforms.
Nothing can be used that's only working on a specific platform, like Android-only libraries or Swing/AWT.

### Resources

- Lottie animations are used for visual effects (confetti, explosion)
- Icons are sourced from Google Material Design
- The font used is licensed under the SIL Open Font License (OFL)

### Final checks

- All code should compile. Actually run the tests to make sure it does.
- Remove unused code and imports.
