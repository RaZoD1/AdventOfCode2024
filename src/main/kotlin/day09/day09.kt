package day09

import getInput
import runLevels


data class StorageEntry(val id: Int, val pos: Int, val len: Int)

fun solveLevel1(text: String): Long {
    val storage = mutableListOf<Int?>()

    var id = 0
    var isSpace = false
    for (n in text.map { it.digitToInt() }) {
        for (i in 0..<n) {
            storage.add(if (isSpace) null else id)
        }
        if (!isSpace) id++
        isSpace = !isSpace
    }

    var left = 0
    var right = storage.size - 1
    while (storage[left] != null) left++
    while (storage[right] == null) right--
    while (left < right) {
        storage[left] = storage[right]
        storage[right] = null
        while (storage[left] != null) left++
        while (storage[right] == null) right--
    }

    val checksum = storage.withIndex().sumOf { (index, id) -> index * (id ?: 0).toLong() }

    //println(storage)
    println("Checksum: $checksum")
    return checksum
}

fun printStorage(storage: List<StorageEntry>) {
    var i = 0
    while (i < (storage.size - 1)) {
        print("${storage[i].id},".repeat(storage[i].len))
        val firstEnd = storage[i].pos + storage[i].len
        val secondStart = storage[i + 1].pos
        val space = secondStart - firstEnd
        if (space > 0) {
            print(" ,".repeat(space))
        }
        i++
    }
    print("${storage[i].id},".repeat(storage[i].len))

    println()
}

fun solveLevel2(text: String): Long {
    val storage = mutableListOf<StorageEntry>() // Id, Length

    var id = 0
    var isSpace = false
    var pos = 0
    for (len in text.map { it.digitToInt() }) {
        if (isSpace) {
            isSpace = false
            pos += len
            continue
        }
        storage.add(StorageEntry(id, pos, len))

        pos += len
        id++
        isSpace = true
    }

    println("Done parsing")


    var currentId = id - 1
    var right = storage.size - 1

    while (right >= 0) {
        //println("Running for rightIndex $right")
        //printStorage(storage)

        var i = 0
        val toMove = storage[right]
        var moved = false
        while (i < (storage.size - 1) && i < right) {
            val firstEnd = storage[i].pos + storage[i].len
            val secondStart = storage[i + 1].pos
            val space = secondStart - firstEnd
            if (space >= toMove.len) {
                // println("Found space at index $i ($firstEnd) with space $space for ${toMove.len} of id ${toMove.id}")
                storage.removeAt(right)
                storage.add(i + 1, toMove.copy(pos = firstEnd))
                moved = true
                break
            }
            i++
        }
        if (!moved) right--
        while (right >= 0 && storage[right].id > currentId) right--
    }

    val checksum = storage.sumOf { file -> (file.pos..<(file.pos + file.len)).sumOf { it * file.id.toLong() } }
    //printStorage(storage)

    println("Checksum: $checksum")
    return checksum
}

fun main() {
    val text = getInput(9)
    runLevels(9, { solveLevel1(text) }, { solveLevel2(text) })

}