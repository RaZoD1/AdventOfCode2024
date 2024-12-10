package day09

import getResourceAsText


fun main() {
    val text = getResourceAsText("/day09/input") ?: error("Input not found")

    val storage = mutableListOf<Int?>()

    var id = 0
    var isSpace = false
    for(n in text.map { it.digitToInt() }){
        for(i in 0..<n){
            storage.add(if(isSpace) null else id)
        }
        if(!isSpace) id++
        isSpace = !isSpace
    }

    var left = 0
    var right = storage.size - 1
    while(storage[left] != null) left++
    while (storage[right] == null) right--
    while(left < right){
        storage[left] = storage[right]
        storage[right] = null
        while(storage[left] != null) left++
        while (storage[right] == null) right--
    }

    val checksum = storage.withIndex().sumOf { (index, id) -> index * (id ?: 0).toLong()}

    //println(storage)
    println("Checksum: $checksum")
}