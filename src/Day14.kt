fun main() {

    fun step(template: MutableList<Char>, dict: Map<String, Char>) {
        val iterator = template.listIterator()
        loop@ while (true) {
            val c1 = iterator.next()
            val c2 = if (iterator.hasNext()) iterator.next() else break
            iterator.previous()
            iterator.add(dict["$c1$c2"]!!)
        }
    }

    fun substructionAfter(steps: Int, template: MutableList<Char>, dict: Map<String, Char>): Long {
        for (i in 0 until steps) {
            println("Step #$i")
            step(template, dict)
        }

        val charMap = buildMap<Char, Long> {
            template.forEach { ch ->
                put(ch, getOrPut(ch) { 0 } + 1)
            }
        }
        val sorted = charMap.values.sorted()
        return sorted.last() - sorted.first()
    }

    fun part1(input: Pair<String, Map<String, Char>>): Long {
        val (template, dict) = input
        val inplace = template.toMutableList()
        return substructionAfter(10, inplace, dict)
    }

    fun part2(input: Pair<String, Map<String, Char>>): Long {
        val (template, dict) = input
        val inplace = template.toMutableList()
        return substructionAfter(40, inplace, dict)
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
