package de.stefan_oltmann.mines.model

val smallTestMinefield = Minefield.create(
    config = GameConfig(
        cellSize = 10,
        mapWidth = 10,
        mapHeight = 10,
        difficulty = GameDifficulty.HARD
    ),
    seed = 4711
)

val mediumTestMinefield = Minefield.create(
    config = GameConfig(
        cellSize = 10,
        mapWidth = 25,
        mapHeight = 25,
        difficulty = GameDifficulty.HARD
    ),
    seed = 4242
)

val largeTestMinefield = Minefield.create(
    config = GameConfig(
        cellSize = 10,
        mapWidth = 50,
        mapHeight = 50,
        difficulty = GameDifficulty.HARD
    ),
    seed = 123456789
)
