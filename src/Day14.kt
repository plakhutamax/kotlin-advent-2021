import kotlin.math.ceil

fun main() {

    fun templateToCountMap(template: String): MutableMap<String, Long> {
        val res = mutableMapOf<String, Long>()
        template.windowed(2).forEach {
            res[it] = res.getOrPut(it) { 0L } + 1
        }
        return res
    }

    fun step(dict: Map<String, Char>, current: MutableMap<String, Long>) {
        val res = mutableMapOf<String, Long>()
        for (entry in current) {
            val (key, count) = entry
            val key1 = "${key[0]}${dict[key]}"
            val key2 = "${dict[key]}${key[1]}"
            if (res.contains(key1)) {
                res[key1] = res[key1]!! + count
            } else {
                res[key1] = count
            }
            if (res.contains(key2)) {
                res[key2] = res[key2]!! + count
            } else {
                res[key2] = count
            }
        }
        current.clear()
        current.putAll(res)
    }

    fun substructionAfter(steps: Int, template: String, dict: Map<String, Char>): Long {
        val tmpl = templateToCountMap(template)
        println(tmpl)
        for (i in 0 until steps) {

            step(dict, tmpl)
            println("Step #${i + 1}")
            println(tmpl)
        }

        val charMap = mutableMapOf<Char, Long>()
        tmpl.forEach { (pair, count) ->
            charMap[pair[0]] = charMap.getOrPut(pair[0], { 0L }) + count
            charMap[pair[1]] = charMap.getOrPut(pair[1], { 0L }) + count
        }

        println(charMap)
        val sorted = charMap.values.sorted()

        return ceil(sorted.last().toDouble() / 2).toLong() - ceil(sorted.first().toDouble() / 2).toLong()
    }

    fun part1(input: Pair<String, Map<String, Char>>): Long {
        val (template, dict) = input
        return substructionAfter(10, template, dict)
    }

    fun part2(input: Pair<String, Map<String, Char>>): Long {
        val (template, dict) = input
        return substructionAfter(40, template, dict)
    }

    fun parseInput(input: List<String>): Pair<String, Map<String, Char>> {
        val iterator = input.iterator()
        val template = iterator.next()
        iterator.next()
        val instructions = mutableMapOf<String, Char>()
        for (instruction in iterator) {
            val (key, ins) = instruction.split(" -> ")
            instructions[key] = ins.toCharArray().first()
        }
        return Pair(template, instructions)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day14_test")
    check(part1(parseInput(readInput("Day14_test"))) == 1588L)
    check(part2(parseInput(readInput("Day14_test"))) == 2188189693529)

//    val input = readInput("Day14_input")
    println(part1(parseInput(readInput("Day14_input"))))
    println(part2(parseInput(readInput("Day14_input"))))
}
