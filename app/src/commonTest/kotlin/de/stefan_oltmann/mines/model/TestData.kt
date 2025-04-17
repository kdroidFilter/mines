package de.stefan_oltmann.mines.model

val smallTestMinefield = Minefield(
    config = GameConfig(
        cellSize = 10,
        mapWidth = 10,
        mapHeight = 10,
        difficulty = GameDifficulty.HARD
    ),
    seed = 4711
)

val mediumTestMinefield = Minefield(
    config = GameConfig(
        cellSize = 10,
        mapWidth = 25,
        mapHeight = 25,
        difficulty = GameDifficulty.HARD
    ),
    seed = 4242
)

val largeTestMinefield = Minefield(
    config = GameConfig(
        cellSize = 10,
        mapWidth = 50,
        mapHeight = 50,
        difficulty = GameDifficulty.HARD
    ),
    seed = 123456789
)
