import kotlin.math.sign

fun main() {

    data class Point(val x: Int, val y: Int)

    data class Segment(val p1: Point, val p2: Point) {
        val isVertical = p1.x == p2.x
        val isHorizontal = p1.y == p2.y
    }

    fun part1(input: List<Segment>): Int {
        val segments = input.filter { it.isVertical || it.isHorizontal }
        val rightMostIndex = segments.maxOf { maxOf(it.p1.x, it.p2.x) }
        val topMostIndex = segments.maxOf { maxOf(it.p1.y, it.p2.y) }
        val intersectMatrix = Array(rightMostIndex + 1) { IntArray(topMostIndex + 1) }
        segments.forEach { segment ->
            when {
                segment.isVertical -> for (y in minOf(segment.p1.y, segment.p2.y)..maxOf(segment.p1.y, segment.p2.y)) {
                    intersectMatrix[segment.p1.x][y]++
                }
                segment.isHorizontal -> for (x in minOf(segment.p1.x, segment.p2.x)..maxOf(segment.p1.x, segment.p2.x)) {
                    intersectMatrix[x][segment.p1.y]++
                }
            }
        }
        var count = 0
        for (x in 0..rightMostIndex) {
            for (y in 0..topMostIndex) {
                if (intersectMatrix[x][y] > 1) count++
            }
        }
        return count
    }

    fun part2(input: List<Segment>): Int {
        val rightMostIndex = input.maxOf { maxOf(it.p1.x, it.p2.x) }
        val topMostIndex = input.maxOf { maxOf(it.p1.y, it.p2.y) }
        val intersectMatrix = Array(rightMostIndex + 1) { IntArray(topMostIndex + 1) }
        input.forEach { segment ->
            when {
                segment.isVertical -> for (y in minOf(segment.p1.y, segment.p2.y)..maxOf(segment.p1.y, segment.p2.y)) {
                    intersectMatrix[segment.p1.x][y]++
                }
                segment.isHorizontal -> for (x in minOf(segment.p1.x, segment.p2.x)..maxOf(segment.p1.x, segment.p2.x)) {
                    intersectMatrix[x][segment.p1.y]++
                }
                else -> {
                    var s = 0
                    val (start, end) = if (segment.p1.x < segment.p2.x) listOf(segment.p1, segment.p2) else listOf(segment.p2, segment.p1)
                    val dir = (end.y - start.y).sign
                    for (x in start.x..end.x) {
                        intersectMatrix[x][start.y + s * dir]++
                        s++
                    }
                }
            }
        }
        var count = 0
        for (x in 0..rightMostIndex) {
            for (y in 0..topMostIndex) {
                if (intersectMatrix[x][y] > 1) count++
            }
        }
        return count
    }

    fun String.toPoint(): Point {
        val p = split(",")
        return Point(p[0].toInt(), p[1].toInt())
    }

    fun String.toSegment(): Segment {
        val points = split(" -> ")
        return Segment(points[0].toPoint(), points[1].toPoint())
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val segments = testInput.map { it.toSegment() }
    check(part1(segments) == 5)
    check(part2(segments) == 12)

    var input = readInput("Day05_input")
    val segmentsInput = input.map { it.toSegment() }
    println(part1(segmentsInput))
    println(part2(segmentsInput))
}
