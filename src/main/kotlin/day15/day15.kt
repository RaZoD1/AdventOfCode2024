package day15

import Grid
import Vec2
import day04.at
import day04.inGrid
import day12.forEachCell
import findCharInGrid
import getInput
import parseGrid
import runLevels
import toMutableGrid
import kotlin.math.E

const val WALL = '#'
const val CRATE = 'O'
const val ROBOT = '@'
const val EMPTY = '.'

fun checkMove(grid: Grid, move: Vec2, robotPos: Vec2 = findCharInGrid(grid, ROBOT)): Boolean {
    var curPos = robotPos + move
    while (true) {
        if (!grid.inGrid(curPos)) error("Move is outside of grid")
        if (grid.at(curPos) == WALL) {
            return false
        }
        if (grid.at(curPos) == EMPTY) {
            return true
        }
        curPos += move
    }
}

fun gpsSum(grid: Grid): Long {
    var sum = 0L
    grid.forEachCell {
        if (grid.at(it) == CRATE || grid.at(it) == CRATE_LEFT) {
            sum += it.row * 100 + it.col
        }
    }
    return sum
}

fun solveLevel1(grid: Grid, movements: List<Vec2>): Long {
    val grid = grid.toMutableGrid()

    var robotPosition: Vec2 = findCharInGrid(grid, '@')
    for (move in movements) {
        if (checkMove(grid, move, robotPosition)) {
            var curPos = robotPosition
            var temp: Char = EMPTY
            while (true) {
                val c = grid.at(curPos)
                grid[curPos.row][curPos.col] = temp
                temp = c
                if (temp == EMPTY) break
                curPos += move
            }

            robotPosition += move
        }
    }

    // println(grid.joinToString("\n") { it.joinToString("") })

    return gpsSum(grid)
}

const val CRATE_LEFT = '['
const val CRATE_RIGHT = ']'

fun expandWarehouse(grid: Grid): Grid {
    return grid.map { chars ->
        chars.flatMap { c ->
            when (c) {
                WALL -> listOf<Char>(WALL, WALL)
                CRATE -> listOf<Char>(CRATE_LEFT, CRATE_RIGHT)
                ROBOT -> listOf<Char>(ROBOT, EMPTY)
                EMPTY -> listOf<Char>(EMPTY, EMPTY)
                else -> error("Unknown char $c")
            }
        }
    }
}

fun checkMove2(grid: Grid, move: Vec2, robotPos: Vec2 = findCharInGrid(grid, ROBOT)): Boolean {
    var positions = mutableSetOf<Vec2>(robotPos + move)
    while (positions.isNotEmpty()) {
        positions += positions
            .filter { grid.at(it) in listOf(CRATE_LEFT, CRATE_RIGHT) }
            .map { pos ->
                when (grid.at(pos)) {
                    CRATE_LEFT -> pos + Vec2.RIGHT
                    CRATE_RIGHT -> pos + Vec2.LEFT
                    else -> error("Not a crate")
                }
            }
        if (positions.any { !grid.inGrid(it) }) error("Move is outside of grid")
        if (positions.any { grid.at(it) == WALL }) {
            return false
        }
        positions = positions.filter { pos -> grid.at(pos) != EMPTY }.toMutableSet()
        positions = positions.map { pos -> pos + move }.filter { pos -> pos !in positions }.toMutableSet()
    }
    return true
}

fun solveLevel2(grid: Grid, movements: List<Vec2>): Long {
    val grid = expandWarehouse(grid).toMutableGrid()
    //println(grid.joinToString("\n") { it.joinToString("") })

    var robotPosition: Vec2 = findCharInGrid(grid, '@')

    for (move in movements) {
        if (checkMove2(grid, move, robotPosition)) {

            var toPlace = mutableListOf<Pair<Vec2, Char>>(robotPosition to EMPTY)
            val moved = mutableSetOf<Vec2>()
            while (toPlace.isNotEmpty()) {
                val (pos, char) = toPlace.removeFirst()
                if(char == EMPTY && (grid.at(pos) == EMPTY || grid.at(pos) == WALL)) continue
                if(pos in moved) continue
                if(move.row == 0){
                    if(grid.at(pos) != EMPTY) toPlace.add(pos + move to grid.at(pos))
                    grid[pos.row][pos.col] = char
                    moved.add(pos)
                } else {
                    toPlace.addAll(when(grid.at(pos)) {
                        CRATE_LEFT -> listOf((pos + Vec2.RIGHT) to EMPTY, (pos + move) to CRATE_LEFT)
                        CRATE_RIGHT -> listOf((pos + Vec2.LEFT) to EMPTY, (pos + move) to CRATE_RIGHT)
                        EMPTY -> emptyList()
                        ROBOT -> listOf((pos + move) to ROBOT)
                        else -> error("idk")
                    })
                    grid[pos.row][pos.col] = char
                    moved.add(pos)
                }
            }

            robotPosition += move
        }
    }
    // println(grid.joinToString("\n") { it.joinToString("") })
    return gpsSum(grid)
}

fun main() {
    val text = getInput(15, sample = false)
    val grid = text.split("\n\n").first().let { parseGrid(it) }
    val movements = text.split("\n\n").last().replace("\n", "").map { c ->
        when (c) {
            '<' -> Vec2.LEFT
            '>' -> Vec2.RIGHT
            'v' -> Vec2.DOWN
            '^' -> Vec2.UP
            else -> error("$c is not a direction char (<>v^)")
        }
    }


    runLevels(15, { solveLevel1(grid, movements) }, { solveLevel2(grid, movements) })
}
