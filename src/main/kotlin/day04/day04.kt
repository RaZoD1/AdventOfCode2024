package day04

import Vec2
import Grid
import getInput
import parseGrid
import runLevels

val DIRECTIONS = listOf<Vec2>(
    Vec2.UP,
    Vec2.DOWN,
    Vec2.LEFT,
    Vec2.RIGHT,
    Vec2.UP_LEFT,
    Vec2.UP_RIGHT,
    Vec2.DOWN_LEFT,
    Vec2.DOWN_RIGHT
)

fun <T> List<List<T>>.inGrid(pos: Vec2): Boolean {
    if (pos.row in this.indices) {
        if (pos.col in this[pos.row].indices)
            return true
    }
    return false
}

fun <T> List<List<T>>.at(pos: Vec2): T {
    if (!this.inGrid(pos)) error("pos $pos outside of grid")

    return this[pos.row][pos.col]!!
}

fun solveLevel1(grid: Grid): Long {
    var xmasCount = 0

    for (rowIdx in grid.indices) {
        for (colIdx in grid[rowIdx].indices) {
            val pos = Vec2(col = colIdx, row = rowIdx)

            if (grid[rowIdx][colIdx] != 'X') continue

            directions@ for (dir in DIRECTIONS) {
                for (i in 1..3) {
                    val letterPos = pos + (dir * i)
                    if (!grid.inGrid(letterPos)) continue@directions

                    if (grid.at(letterPos) != ("XMAS"[i])) continue@directions
                }
                xmasCount++

            }
        }
    }

    println("Amount of XMAS: $xmasCount")
    return xmasCount.toLong()
}


val DIAG_DIRECTIONS = listOf(Vec2.UP_LEFT, Vec2.UP_RIGHT, Vec2.DOWN_LEFT, Vec2.DOWN_RIGHT)

fun msToggle(c: Char): Char = if (c == 'M') 'S' else 'M'

fun solveLevel2(grid: Grid): Long {
    var xmasCount = 0

    for (rowIdx in grid.indices) {
        cols@ for (colIdx in grid[rowIdx].indices) {
            val pos = Vec2(col = colIdx, row = rowIdx)

            if (grid[rowIdx][colIdx] != 'A') continue

            for (dir in DIAG_DIRECTIONS) {
                if (!grid.inGrid(pos + dir)) continue@cols
            }
            val upLeftChar = grid.at(pos + Vec2.UP_LEFT)
            if (upLeftChar == 'M' || upLeftChar == 'S') {
                if (msToggle(upLeftChar) != grid.at(pos + Vec2.DOWN_RIGHT)) continue@cols
            } else continue@cols
            val upRightChar = grid.at(pos + Vec2.UP_RIGHT)
            if (upRightChar == 'M' || upRightChar == 'S') {
                if (msToggle(upRightChar) != grid.at(pos + Vec2.DOWN_LEFT)) continue@cols
            } else continue@cols

            xmasCount++
        }
    }

    println("Amount of X-MAS: $xmasCount")
    return xmasCount.toLong()
}

fun main() {
    val text = getInput(4)
    val grid = parseGrid(text)

    runLevels(4, { solveLevel1(grid) }, { solveLevel2(grid) })

}