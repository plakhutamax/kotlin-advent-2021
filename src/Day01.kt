fun main() {
    fun count(input: List<Int>): Int {
        if (input.isEmpty()) return 0

        var count = input.size - 1
        var prevDepth: Int = input[0]
        for (i in 1 until input.size) {
            val current = input[i]
            if (current <= prevDepth) count--
            prevDepth = current
        }
        return count
    }

    fun part1(input: List<String>): Int {
        return count(input.map(String::toInt))
    }

    fun part2(input: List<String>): Int {
        return count(
            input.map(String::toInt)
                .windowed(size = 3, step = 1) {
                    it.sum()
                })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("day1-puzzle1-input")
    println(part1(input))
    println(part2(input))
}
