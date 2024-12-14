import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlin.math.*

fun getInput(day: Int, sample: Boolean = false, alternateFileName: String? = null): String {
    val dayString = if (day < 10) "0$day" else "$day"
    val filename = if (sample) "sample" else alternateFileName ?: "input"
    val path = Path("./inputFiles/day$dayString/$filename")

    val buffer: Source = SystemFileSystem.source(path).buffered()

    return buffer.readString()
}

fun toRadians(degrees: Double): Double = degrees * PI / 180.0


data class Vec2(val col: Int, val row: Int) {
    companion object {
        val LEFT = Vec2(-1, 0)
        val RIGHT = Vec2(1, 0)
        val UP = Vec2(0, -1)
        val DOWN = Vec2(0, 1)
        val UP_LEFT = Vec2(-1, -1)
        val UP_RIGHT = Vec2(1, -1)
        val DOWN_LEFT = Vec2(-1, 1)
        val DOWN_RIGHT = Vec2(1, 1)
    }

    val x = col
    val y = row

    operator fun plus(v: Vec2): Vec2 {
        return Vec2(this.col + v.col, this.row + v.row)
    }

    operator fun minus(v: Vec2): Vec2 {
        return Vec2(this.col - v.col, this.row - v.row)
    }

    operator fun times(scale: Int): Vec2 {
        return Vec2(this.col * scale, this.row * scale)
    }

    fun rotate(degrees: Double): Vec2 {

        val radians = toRadians(degrees)
        return Vec2(
            row = (sin(radians) * col + cos(radians) * row).roundToInt(),
            col = (cos(radians) * col - sin(radians) * row).roundToInt()
        )
    }

    fun turnRight(): Vec2 {
        return this.rotate(90.0)
    }

    fun turnLeft(): Vec2 {
        return this.rotate(-90.0)
    }

    fun turnAround(): Vec2 {
        return this.rotate(180.0)
    }
}


data class LVec2(val col: Long, val row: Long) {
    companion object {
        val LEFT = LVec2(-1, 0)
        val RIGHT = LVec2(1, 0)
        val UP = LVec2(0, -1)
        val DOWN = LVec2(0, 1)
        val UP_LEFT = LVec2(-1, -1)
        val UP_RIGHT = LVec2(1, -1)
        val DOWN_LEFT = LVec2(-1, 1)
        val DOWN_RIGHT = LVec2(1, 1)
    }

    val x = col
    val y = row

    operator fun plus(v: LVec2): LVec2 {
        return LVec2(this.col + v.col, this.row + v.row)
    }

    operator fun minus(v: LVec2): LVec2 {
        return LVec2(this.col - v.col, this.row - v.row)
    }

    operator fun times(scale: Long): LVec2 {
        return LVec2(this.col * scale, this.row * scale)
    }

    fun rotate(degrees: Double): LVec2 {

        val radians = toRadians(degrees)
        return LVec2(
            row = (sin(radians) * col + cos(radians) * row).roundToLong(),
            col = (cos(radians) * col - sin(radians) * row).roundToLong()
        )
    }

    fun turnRight(): LVec2 {
        return this.rotate(90.0)
    }

    fun turnLeft(): LVec2 {
        return this.rotate(-90.0)
    }

    fun turnAround(): LVec2 {
        return this.rotate(180.0)
    }
}

fun calculateLCM(a: Long, b: Long): Long {
    if (a == 0L || b == 0L) {
        throw IllegalArgumentException("LCM is not defined for zero.")
    }

    return (a / gcd(a, b)) * b
}

// Helper function to calculate GCD using the Euclidean algorithm
fun gcd(a: Long, b: Long): Long {
    var x = a
    var y = b

    while (y != 0L) {
        val temp = y
        y = x % y
        x = temp
    }

    return x
}

fun plotVec2s(positions: Iterable<Vec2>) {
    val cols = positions.maxOf { it.col } + 1
    val rows = positions.maxOf { it.row } + 1
    val grid = List(rows) { MutableList(cols) { '.' } }
    for (position in positions) {
        grid[position.row][position.col] = 'X'
    }
    println(grid.joinToString("\n") { it.joinToString("") })

}