package day09

import getResourceAsText
import kotlin.time.measureTime


data class StorageEntry(val id: Int, val pos: Int, val len: Int)

fun main() {
    measureTime{
        val text = getResourceAsText("/day09/dominik") ?: error("Input not found")

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
        printStorage(storage)

        println("Checksum: $checksum")
    }.also {
        println("Time taken (part 2): ${it.inWholeMilliseconds}ms")
    }
}

fun printStorage(storage: List<StorageEntry>){
    var i = 0
    while(i < (storage.size - 1)){
        print("${storage[i].id},".repeat(storage[i].len))
        val firstEnd = storage[i].pos + storage[i].len
        val secondStart = storage[i+1].pos
        val space = secondStart - firstEnd
        if(space > 0){
            print(" ,".repeat(space))
        }
        i++
    }
    print("${storage[i].id},".repeat(storage[i].len))

    println()
}