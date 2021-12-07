import kotlin.math.absoluteValue

fun main() {


    fun part1(input: List<Int>): Int {
        println(input)
        val median = input.sorted().let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }
        println(median)
        return input.fold(0) { acc, pos ->
             acc + (pos - median).absoluteValue
        }
    }

    fun getFuelConsumption(from: Int, to: Int): Int {
        val delta = (from - to).absoluteValue
        return (1 + delta) * delta / 2
    }

    fun part2(input: List<Int>): Int {
        val min = input.minOrNull() ?: 0
        val max = input.maxOrNull() ?: 0
        var currentMinFuel = Int.MAX_VALUE
        for (i in min..max) {
            val candidate = input.fold(0) { acc, pos -> acc + getFuelConsumption(pos, i) }
            currentMinFuel = minOf(currentMinFuel, candidate)
        }
        println(currentMinFuel)
        return currentMinFuel
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readFirstLine("Day07_test").split(",").map(String::toInt)
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readFirstLine("Day07_input").split(",").map(String::toInt)
    println(part1(input))
    println(part2(input))
}
