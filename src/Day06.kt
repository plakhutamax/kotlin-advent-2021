fun main() {
    class Lanterfish(daysBeforeSpawn: Int) {
        private var newSpawnIn = daysBeforeSpawn
        val spawnTimer get() = newSpawnIn

        fun dayPass(): Lanterfish? {
            if (newSpawnIn == 0) {
                newSpawnIn = 6
                return Lanterfish(8)
            }

            newSpawnIn--
            return null
        }

        override fun toString(): String = "${this.hashCode()}: $newSpawnIn"
    }

    fun part1(input: List<Lanterfish>): Int {
        val mutableInput = input.toMutableList()
        for (d in 0 until 80) {
            val newFishes = mutableListOf<Lanterfish>()
            mutableInput.forEach {
                it.dayPass()?.let { newFish ->
                    newFishes.add(newFish)
                }
            }
            mutableInput.addAll(newFishes)
        }
        return mutableInput.size
    }

    fun part2(input: List<Lanterfish>): Long {
        var generations = input.groupBy { it.spawnTimer }.mapValues { it.value.size.toLong() }.toMutableMap()
        println("day start $generations")

        for (d in 0 until 256) {
            val ng = generations[0]
            generations.remove(0)
            generations = generations.mapKeys {
                it.key - 1
            }.toMutableMap()
            ng?.let {
                generations[6] = (generations[6] ?: 0) + it
                generations[8] = (generations[8] ?: 0) + it
            }
            println("day $d: $generations")
        }
        var count = 0L
        generations.values.forEach {
            count += it
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readFirstLine("Day06_test").split(",").map(String::toInt).map(::Lanterfish)
//    check(part1(testInput) == 5934)
    println(part2(testInput))
    check(part2(testInput) == 26984457539)

    val input = readFirstLine("Day06_input").split(",").map(String::toInt).map(::Lanterfish)
//    println(part1(input))
    println(part2(input))
}
