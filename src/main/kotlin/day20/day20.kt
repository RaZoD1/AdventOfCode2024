package day20

import Grid
import Vec2
import day04.at
import day04.inGrid
import findCharInGrid
import getInput
import parseGrid
import runLevels
import java.util.*
import kotlin.math.abs

val DIRECTIONS = listOf(Vec2.UP, Vec2.DOWN, Vec2.LEFT, Vec2.RIGHT)
fun getNeighbors(grid: Grid, pos: Vec2): List<Vec2> {
    return DIRECTIONS
        .map { it + pos }
        .filter { grid.inGrid(it) && grid.at(it) != '#' }
}

data class PathKey(val pos: Vec2, val cheatsLeft: Int, val cheatStarted: Boolean)

fun getNeighborsWithCheats(grid: Grid, pathKey: PathKey): List<PathKey> {
    val (pos, cheatsLeft, cheatStarted) = pathKey
    return DIRECTIONS
        .map { it + pos }
        .filter { grid.inGrid(it) }
        .map {
            val isCheat = grid.at(it) == '#' || grid.at(pos) == '#'


            if (cheatsLeft == 1 && cheatStarted) {
                return@map PathKey(it, 0, false)
            }

            if (cheatStarted || isCheat) {
                return@map PathKey(it, cheatsLeft - 1, true)
            }

            PathKey(it, cheatsLeft, false)
        }.filter {
            it.cheatsLeft >= 0
        }
}


fun minPicoSecs(grid: Grid): Long {
    val startPos = findCharInGrid(grid, 'S')
    val goalPos = findCharInGrid(grid, 'E')

    val distances = mutableMapOf<Vec2, Long>()
    val queue = PriorityQueue<Vec2>(compareBy { t -> distances.getOrElse(t) { Long.MAX_VALUE } })

    queue.add(startPos)
    distances[startPos] = 0

    while (queue.isNotEmpty()) {
        val cur = queue.remove()
        val curDistance = distances[cur] ?: error("Not in scores")


        for (neighbor in getNeighbors(grid, cur)) {
            if (neighbor !in distances) {
                distances[neighbor] = curDistance + 1
                queue.add(neighbor)
                continue
            }
            if (distances[neighbor]!! > curDistance + 1) {
                distances[neighbor] = curDistance + 1
                queue.add(neighbor)
            }
        }
    }

    return distances[goalPos]!!
}


fun allCheatPaths(
    grid: Grid,
    maxSteps: Long,
    pathKey: PathKey,
    goal: Vec2 = findCharInGrid(grid, 'E'),
    visited: MutableSet<Vec2> = mutableSetOf<Vec2>(pathKey.pos),
): Long {

    if (maxSteps < visited.size) return 0
    if (pathKey.pos == goal) return 1

    var paths = 0L

    for (neigh in getNeighborsWithCheats(grid, pathKey).filter { it.pos !in visited }) {
        visited.add(neigh.pos)

        paths += allCheatPaths(grid, maxSteps, neigh, goal, visited)
        visited.remove(neigh.pos);

    }

    return paths
}


fun solveLevel1(grid: Grid): Long {
    return optimizedCountCheatPaths(grid, 2, if(USE_SAMPLE) 0 else 100)
}


fun allDistancesFrom(grid: Grid, startPos: Vec2 = findCharInGrid(grid, 'S')): Map<Vec2, Long> {

    val distances = mutableMapOf<Vec2, Long>()
    val queue = PriorityQueue<Vec2>(compareBy { t -> distances.getOrElse(t) { Long.MAX_VALUE } })

    queue.add(startPos)
    distances[startPos] = 0

    while (queue.isNotEmpty()) {
        val cur = queue.remove()
        val curDistance = distances[cur] ?: error("Not in scores")


        for (neighbor in getNeighbors(grid, cur)) {
            if (neighbor !in distances) {
                distances[neighbor] = curDistance + 1
                queue.add(neighbor)
                continue
            }
            if (distances[neighbor]!! > curDistance + 1) {
                distances[neighbor] = curDistance + 1
                queue.add(neighbor)
            }
        }
    }

    return distances
}

fun Vec2.manhattan(v: Vec2): Long {
    val d = this - v
    return abs(d.x) + abs(d.y).toLong()
}

fun solveLevel2(grid: Grid): Long {
    return optimizedCountCheatPaths(grid, 20, if(USE_SAMPLE) 50 else 100)
}

fun optimizedCountCheatPaths(grid: Grid, cheatLength: Int, better: Int): Long {
    val bestNonCheat = minPicoSecs(grid)
    val maxCost = bestNonCheat - better

    val allDistances = allDistancesFrom(grid)


    return allDistances.entries.sumOf { (pos, distFromStart) ->
        return@sumOf allDistances.count { (pos2, dist2) ->
            val m = pos.manhattan(pos2)
            m <= cheatLength &&  bestNonCheat - (dist2 - distFromStart) + m <= maxCost

        }.toLong()
    }
}

const val USE_SAMPLE = false

fun main() {
    val text = getInput(20, USE_SAMPLE)

    val grid = parseGrid(text)

    runLevels(
        20,
        { solveLevel1(grid) },
        { solveLevel2(grid) },
        times = 1
    )
}
