package day10

fun main(args: Array<String>) {
    val input = java.io.File(args[0]).readLines()
    println("A: " + getAnswerA(input))
    println("B:\n" + getAnswerB(input))
}

fun getAnswerA(inputLines: List<String>): Int {
    val states = getStateList(inputLines)
    val cyclesOfInterest = listOf(20, 60, 100, 140, 180, 220)
    return cyclesOfInterest.sumOf { cycle ->
        cycle * states[cycle - 1]
    }
}

fun getStateList(inputLines: List<String>): List<Int> {
    var currentX = 1
    val cycleToState = MutableList(1) { currentX }

    for (line in inputLines) {
        val tokens = line.split(' ')
        when (tokens.first()) {
            "noop" -> cycleToState.add(currentX)
            "addx" -> {
                cycleToState.add(currentX)
                currentX += tokens[1].toInt()
                cycleToState.add(currentX)
            }
            else -> throw Exception("Unsupported operation: ${tokens.first()}")
        }
    }
    return cycleToState
}

fun getAnswerB(inputLines: List<String>): String {
    val states = getStateList(inputLines)
    var image = ""
    states.forEachIndexed { cycle, currentX ->
        val horizontalPos = cycle % 40
        if (horizontalPos == 0) {
            image += "\n"
        }

        if (horizontalPos in listOf(currentX - 1, currentX, currentX +1)) {
            image += "#"
        } else {
            image += "."
        }
    }
    return image
}