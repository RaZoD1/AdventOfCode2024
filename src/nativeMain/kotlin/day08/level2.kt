package day08

import Vec2
import day04.at
import day04.inGrid
import getResourceAsText


fun calculateAntinodePositions2(grid: Grid, antennas: List<Vec2>): Set<Vec2> {

    val antinodes = mutableSetOf<Vec2>()

    for(i in antennas.indices){
        val a1 = antennas[i]
        for(j in (i+1)..<antennas.size){
            val a2 = antennas[j]

            val delta = a2 - a1

            var nextAntinode = a1
            while (grid.inGrid(nextAntinode)){
                antinodes.add(nextAntinode)
                nextAntinode -= delta
            }
            nextAntinode = a2
            while (grid.inGrid(nextAntinode)){
                antinodes.add(nextAntinode)
                nextAntinode += delta
            }
        }
    }
    return antinodes
}

fun main() {
    val text = getResourceAsText("/day08/input") ?: error("Input not found")

    val grid = text.split("\n").filter { it.isNotEmpty() }.map { it.toList() }

    val antennaMap = grid.mapIndexed { rowIdx, row -> row.mapIndexed { colIdx, char ->
        val pos = Vec2(colIdx, rowIdx)
        pos
    } }.flatten().filter { grid.at(it) != '.' }.groupBy { grid.at(it) }


    val antinodeCount = antennaMap.flatMap { (antennaChar, positions) -> calculateAntinodePositions2(grid, positions) }.toSet().count()

    println("Amount of antinodes: $antinodeCount")
}