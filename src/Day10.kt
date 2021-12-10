fun main() {

    val opening = arrayOf('(', '{', '[', '<')
    val closing = arrayOf(')', '}', ']', '>')
    val scoreArray = arrayOf(3, 1197, 57, 25137)

    fun part1(input: List<String>): Int {
        var score = 0
        input.forEach { line ->
            val queue = ArrayDeque<Char>()
            for (c in line) {
                when (c) {
                    in opening -> queue.add(c)
                    else -> {
                        val lastOpenBracketIndex = opening.lastIndexOf(queue.last())
                        if (c == closing[lastOpenBracketIndex]) {
                            queue.removeLast()
                        } else {
                            score += scoreArray[closing.indexOf(c)]
                            break
                        }
                    }
                }
            }
        }
        return score
    }

    val symbolScore = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    fun part2(input: List<String>): Long {
        val incompleteLines = mutableListOf<String>()
        loop@ for (line in input) {
            val queue = ArrayDeque<Char>()
            for (c in line) {
                when (c) {
                    in opening -> queue.add(c)
                    else -> {
                        val lastOpenBracketIndex = opening.lastIndexOf(queue.last())
                        if (c == closing[lastOpenBracketIndex]) {
                            queue.removeLast()
                        } else {
                            continue@loop
                        }
                    }
                }
            }
            incompleteLines.add(line)
        }

        val completionSymbols = mutableListOf<List<Char>>()
        loop@ for (line in incompleteLines) {
            val queue = ArrayDeque<Char>()
            val completedSymbols = mutableListOf<Char>()
            for (c in line) {
                when (c) {
                    in opening -> queue.add(c)
                    else -> {
                        var lastOpenBracketIndex = opening.lastIndexOf(queue.last())
                        while (c != closing[lastOpenBracketIndex]) {
                            val el = queue.removeLast()
                            val index = opening.indexOf(el)
                            completedSymbols.add(closing[index])
                            lastOpenBracketIndex = opening.lastIndexOf(queue.last())
                        }
                        queue.removeLast()
                    }
                }
            }
            queue.reversed().forEach {
                completedSymbols.add(closing[opening.indexOf(it)])
            }
            completionSymbols.add(completedSymbols)
        }

        val scores = completionSymbols.map {
            it.fold(0L) { acc, c -> acc * 5 + symbolScore[c]!!}
        }.sorted()

        println(scores)
        return scores[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10_input")
    println(part1(input))
    println(part2(input))
}
