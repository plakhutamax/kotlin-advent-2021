import java.util.*
import kotlin.math.ceil

fun main() {

    data class Point(val y: Int, val x: Int, var risk: Int)

    fun Array<IntArray>.getAdjacentFor(i: Int, j: Int): List<Point> {
        val height = size
        val width = this[0].size
        val res = mutableListOf<Point>()
        if (i > 0) res.add(Point(i - 1, j, this[i - 1][j]))
        if (j > 0) res.add(Point(i, j - 1, this[i][j - 1]))
        if (j < width - 1) res.add(Point(i, j + 1, this[i][j + 1]))
        if (i < height - 1) res.add(Point(i + 1, j, this[i + 1][j]))
        return res
    }

    val toVisit: MutableList<Point> = mutableListOf()
    val pq = PriorityQueue<Point> { a, b -> a.risk - b.risk }
    fun findPath(input: Array<IntArray>, riskMap: Array<IntArray>): Int {
        while (pq.isNotEmpty()) {
//            println(pq)
            val (y, x, riskValue) = pq.poll()

            input.getAdjacentFor(y, x).forEach {
                if (riskMap[it.y][it.x] > riskValue + input[it.y][it.x]) {
                    riskMap[it.y][it.x] = riskValue + input[it.y][it.x]
//                    println("Set value ${riskMap[it.y][it.x]}")
                    it.risk = riskMap[it.y][it.x]
                    pq.add(it)
                }
            }
        }
        return riskMap.last().last()
    }

    fun part1(input: Array<IntArray>): Int {
        val risk = Array(input.size) { IntArray(input.first().size) { Int.MAX_VALUE } }
        risk[0][0] = 0
        pq.add(Point(0, 0, 0))
        return findPath(input, risk)

    }

    fun part2(input: Array<IntArray>): Int {
        val risk = Array(input.size) { IntArray(input.first().size) { Int.MAX_VALUE } }
        risk[0][0] = 0
        pq.add(Point(0, 0, 0))
        return findPath(input, risk).also { println(it) }
    }

    fun parseInput(input: List<String>): Array<IntArray> {
        val height = input.size
        val width = input[0].length

        val res = Array(height) { IntArray(width) }
        for (i in 0 until height) {
            for (j in 0 until width) {
                res[i][j] = Integer.parseInt(input[i][j].toString())
            }
        }
        return res
    }

    fun Array<IntArray>.scale(): Array<IntArray> {
        val res = Array(size * 5) { IntArray (first().size * 5) }
        for (y in this.indices) {
            for (x in first().indices) {
                res[y][x] = this[y][x]
            }
        }
        val blockHeight = size
        val blockWidth = first().size
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                for (y in indices) {
                    for (x in first().indices) {
                        var v = this[y][x] + i + j
                        if (v >= 10) v -= 9
                        res[y + blockHeight * i][x + blockWidth * j] = v
                    }
                }
            }
        }

        for (y in this.indices) {
            for (x in first().indices) {
                res[y][x] = this[y][x]
            }
        }

        for (i in res.indices) {
            if (i % 10 == 0) println()
            for (j in res[i].indices) {
                if (j % 10 == 0) print(" ")
                print(res[i][j])
            }
            println()
        }
        return res
    }

// test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day14_test")
    check(part1(parseInput(readInput("Day15_test"))) == 40)
    check(part2(parseInput(readInput("Day15_test")).scale()) == 315)

//    val input = readInput("Day14_input")
    println(part1(parseInput(readInput("Day15_input"))))
    println(part2(parseInput(readInput("Day15_input")).scale()))
}
