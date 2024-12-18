package day18


import Grid
import Vec2
import day04.at
import day04.inGrid
import getInput
import runLevels
import toMutableGrid
import java.util.PriorityQueue

val DIRECTIONS = listOf(Vec2.UP, Vec2.DOWN, Vec2.LEFT, Vec2.RIGHT)
fun getNeighbors(grid: Grid, pos: Vec2): List<Vec2> {
    return DIRECTIONS.map {it + pos}.filter { grid.inGrid(it) && grid.at(it) == '.' }
}

fun minDistance(grid: Grid): Long? {
    val startPos = Vec2(0,0)
    val goalPos = SIZE - Vec2(1,1)

    val distances = mutableMapOf<Vec2, Long>()
    val queue = PriorityQueue<Vec2>(compareBy { t -> distances.getOrElse(t) { Long.MAX_VALUE } })

    queue.add(startPos)
    distances[startPos] = 0L

    while(queue.isNotEmpty()){
        val cur = queue.remove()
        val curDistance = distances[cur] ?: error("Not in scores")


        for (neighbor in getNeighbors(grid, cur)) {
            if(neighbor !in distances){
                distances[neighbor] = curDistance + 1
                queue.add(neighbor)
                continue
            }
            if(distances[neighbor]!! > curDistance + 1){
                distances[neighbor] = curDistance + 1
                queue.add(neighbor)
            }
        }
    }

    return distances[goalPos]
}

fun solveLevel1(bytes: List<Vec2>): Long {
    val grid = List(SIZE.row) { index -> List(SIZE.col){'.'} }.toMutableGrid()

    for (pos in bytes.take(if(SAMPLE) 12 else 1024)) {
        grid[pos.row][pos.col] = '#'
    }

    //println(grid.joinToString("\n") { it.joinToString("")})


    return minDistance(grid)!!
}

fun solveLevel2(bytes: List<Vec2>): Long {
    val grid = List(SIZE.row) { index -> List(SIZE.col){'.'} }.toMutableGrid()


    for (pos in bytes) {
        grid[pos.row][pos.col] = '#'
        if(minDistance(grid) == null){
            println("Part 2: ${pos.col},${pos.row}")
            return 0
        }
    }
    return -1
}



val SAMPLE = false
val SIZE = if (SAMPLE) Vec2(7, 7) else Vec2(71,71)
fun main() {
    val text = getInput(18, SAMPLE)

    val bytes = text.split('\n').filter { it.isNotEmpty() }.map { it.split(",").let {
        Vec2(it.first().toInt(), it.last().toInt())
    }}

    runLevels(
        18,
        { solveLevel1(bytes) },
        {solveLevel2(bytes)},
        times = 10
    )
}
