import java.lang.IllegalArgumentException

enum class Command {
    Forward, Down, Up;

    companion object {
        fun fromString(string: String): Command {
            return when (string) {
                "forward" -> Forward
                "up" -> Up
                "down" -> Down
                else -> throw IllegalArgumentException("Unknown command")
            }
        }
    }
}

fun main() {

    data class Instruction(
        val command: Command,
        val value: Int
    )

    fun parseInstructions(input: List<String>): List<Instruction> {
        return input.map {
            val splitted = it.split(" ")
            Instruction(
                Command.fromString(splitted[0]),
                splitted[1].toInt()
            )
        }
    }

    fun part1(input: List<String>): Int {
        var depth = 0
        var horizontal = 0
        parseInstructions(input).forEach {
            when(it.command) {
                Command.Forward -> horizontal += it.value
                Command.Down -> depth += it.value
                Command.Up -> depth -= it.value
            }
        }
        return depth * horizontal
    }

    fun part2(input: List<String>): Int {
        var depth = 0
        var horizontal = 0
        var aim = 0
        parseInstructions(input).forEach {
            when(it.command) {
                Command.Forward -> {
                    horizontal += it.value
                    depth += aim * it.value
                }
                Command.Down -> aim += it.value
                Command.Up -> aim -= it.value
            }
        }
        return depth * horizontal
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02_input")
    println(part1(input))
    println(part2(input))
}
