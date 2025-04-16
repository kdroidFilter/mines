# Mines Project Development Guidelines

This document provides essential information for developers working on the Mines project.

## Build/Configuration Instructions

### Project Overview

Mines is a Kotlin Multiplatform project with Compose Multiplatform for UI.
It targets multiple platforms:

- Android
- JVM (Desktop)
- WebAssembly JS (Browser)

It's a Minesweeper clone.

### Prerequisites

- JDK 11, because of Android
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

- See `CODE_STYLE.md` and follow these rules.

### Dependencies Management

Dependencies are managed in `gradle/libs.versions.toml` using Gradle's version catalog feature.

### UI Framework

The project uses Compose Multiplatform for UI, which allows sharing UI code across all platforms.
Nothing can be used that's only working on a specific platform, like Android-only libraries or Swing/AWT.

### Resources

- Lottie animations are used for visual effects (confetti, explosion)
- Icons are sourced from Google Material Design
- The font used is licensed under the SIL Open Font License (OFL)
