package day5

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val input = File(args[0]).readLines()
    val modelA = getModel(input)
    val moveCommands = getMoveCommands(input)
    println("A: " + findAnswerA(modelA, moveCommands))
    val modelB = getModel(input)
    println("B: " + findAnswerB(modelB, moveCommands))
}

fun getModel(inputLines: List<String>) : Model {
    val stackNumbersLineIndex = inputLines.indexOfFirst {
        it.trimIndent().startsWith('1')
    }
    val stackNumbersLine = inputLines[stackNumbersLineIndex]
    val stackNumberings = stackNumbersLine
        .trim()
        .split("\\s+".toRegex())
        .map {
            it.toCharArray()[0]
        }
    val stacksCharIndex = stackNumberings
        .map {
            stackNumbersLine.indexOf(it)
        }

    val numberOfStacks = stackNumberings.count()
    val model = Model(numberOfStacks)

    val stackLines = inputLines.subList(0, stackNumbersLineIndex).reversed()
    stacksCharIndex.forEachIndexed { stackIdIndex, charIndex ->
        for (line in stackLines) {
            if (charIndex < line.length) {
                val char = line[charIndex]
                if (char.isLetterOrDigit()) {
                    model.stacks[stackIdIndex].push(char)
                }
            }
        }
    }
    return model
}

fun getMoveCommands(inputLines: List<String>) : List<MoveCommand> {
    return inputLines
        .dropWhile {
            !it.startsWith("move")
        }
        .map {
            val words = it.split("\\s+".toRegex())
            MoveCommand (
                from = words[3].toInt() - 1,
                to = words[5].toInt() - 1,
                count = words[1].toInt()
            )
        }
}

class Model(numberOfStacks: Int) {
    val stacks : List<Stack<Char>> = List(numberOfStacks) { Stack<Char>() }
}

data class MoveCommand(val from: Int, val to: Int, val count: Int)

fun findAnswerA(model: Model, moveCommands: List<MoveCommand>) : String {
    for (command in moveCommands) {
        repeat(command.count) {
            val movedChar = model.stacks[command.from].pop()
            model.stacks[command.to].push(movedChar)
        }
    }

    return String(model.stacks.map { it.peek() }.toCharArray())
}

fun findAnswerB(model: Model, moveCommands: List<MoveCommand>) : String {
    val cratesToMove = Stack<Char>()
    for (command in moveCommands) {
        repeat(command.count) {
            val movedChar = model.stacks[command.from].pop()
            cratesToMove.push(movedChar)
        }
        repeat(command.count) {
            val movedChar = cratesToMove.pop()
            model.stacks[command.to].push(movedChar)
        }
    }

    return String(model.stacks.map { it.peek() }.toCharArray())
}