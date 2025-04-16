# Mines Project Code Style Guidelines

This document outlines the code style rules to be followed when contributing to the Mines project.

These rules are mainly intended for AI agents to work properly.

## Code Style Rules

In general, follow the Clean Code practices.

The following rules are complementary to the official Kotlin coding conventions
and the detekt rules defined in the project's `detekt.yml` file.

1. **Indentation and Spacing**
    - Use 4 spaces for indentation (no tabs)
    - Maximum line length is 120 characters
    - Use blank lines to separate logical sections of code
    - Use a single blank line between package declaration, imports, and class definition
    - Use a single blank line between a function signature and its body for multi-line bodies.

2. **Naming Conventions**
    - Constants: UPPER_SNAKE_CASE with `const val`
    - Classes: PascalCase
    - Functions and variables: camelCase
    - Package names: lowercase with dots

3. **Imports**
    - Organize imports alphabetically
    - No wildcard imports

4. **Function and Parameter Formatting**
    - For functions with multiple parameters that span multiple lines, align parameters
    - Use trailing commas for parameters in multi-line function calls

5. **Type Declarations**
    - Explicitly declare types (e.g., `Int`, `Long`) rather than relying on type inference for public APIs

6. **Braces**
    - Opening braces are placed at the end of the line
    - Closing braces are placed on their own line
    - Always use braces for control structures, even for single-line statements

7. **String Templates**
    - Use string templates (`"Value: $value"`) instead of string concatenation

8. **Composable Functions**
    - Annotate with `@Composable`
    - Follow the capitalization convention for Composable functions (PascalCase for UI elements)

9. **Documentation**
    - Use KDoc format for documentation comments
    - Document public APIs

10. **Forbidden Practices**
    - Avoid using `print` or `println` for logging (use a proper logger instead)
    - Avoid creating TODO, FIXME, and STOPSHIP markers in comments; but don't remove them until the issue has been resolved
    - Avoid magic numbers; use named constants instead

## Comment Style

- Use block comments (`/* */`) exclusively, do not use line comments (`//`)
- For multi-line block comments, align stars at the beginning of each line
- Include a space after the star in multi-line block comments
- For single-line comments, use `/* Comment text */` format
- Each file should include the standard license header as a block comment

Example of proper multi-line block comment:

```kotlin
/*
 * This is a multi-line
 * block comment with aligned stars
 * and proper spacing.
 */
```

Example of proper single-line block comment:

```kotlin
/* This is a single-line block comment */
```
