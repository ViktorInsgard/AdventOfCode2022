package day9

import kotlin.math.abs

fun main(args: Array<String>) {
    val input = java.io.File(args[0]).readLines()
    val testInput = listOf("R 4", "U 2", "L 2", "D 2")
    println("A: " + getAnswerA(input))
    println("B: " + getAnswerB(input))
}

fun getAnswerA(inputLines: List<String>): Int {
    val commands = inputLines.map { Command(it) }
    val head = Head()
    val tail = Tail()

    val visitedCoordinates = mutableSetOf(tail.position)
    for (command in commands) {
        for (step in 1..command.steps) {
            head.move(command.direction);
            tail.move(head)
            visitedCoordinates.add(tail.position)
        }
    }

    return visitedCoordinates.size
}

fun getAnswerB(inputLines: List<String>): Int {
    val commands = inputLines.map { Command(it) }
    val head = Head()
    val tails = List(9) { Tail() }

    val visitedCoordinates = mutableSetOf(tails.last().position)

    for (command in commands) {
        repeat(command.steps) {
            head.move(command.direction);

            tails.forEachIndexed { index, tail ->
                if (index == 0) {
                    tail.move(head)
                }
                else {
                    val previous = tails[index - 1]
                    tail.move(previous.position)
                }
            }

            visitedCoordinates.add(tails.last().position)
        }
    }

    return visitedCoordinates.size
}

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

data class Command(private val definition: String) {
    val steps: Int = definition.drop(2).toInt()
    val direction: Direction = when(definition[0]) {
        'L' -> Direction.LEFT
        'R' -> Direction.RIGHT
        'U' -> Direction.UP
        'D' -> Direction.DOWN
        else -> throw IllegalArgumentException("Unexpected direction: " + definition[0])
    }
}

data class Coordinate(val x: Int, val y: Int) {
    fun magnitude(): Int = maxOf(abs(x), abs(y))

    override fun toString(): String {
        return "($x, $y)"
    }
}
operator fun Coordinate.plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
operator fun Coordinate.minus(other: Coordinate) = Coordinate(x - other.x, y - other.y)

abstract class Entity() {
    var position = Coordinate(0, 0)
        protected set

    protected companion object {
        val directions = mapOf(
            Direction.LEFT to Coordinate(-1, 0),
            Direction.RIGHT to Coordinate(1, 0),
            Direction.UP to Coordinate(0, 1),
            Direction.DOWN to Coordinate(0, -1)
        )
    }
}

class Head(): Entity() {
    fun move(direction: Direction) {
        position += directions[direction] ?: Coordinate(0, 0)
    }
}

class Tail(): Entity() {
    fun move(head: Head) {
        move(head.position)
    }

    fun move(headPosition: Coordinate) {
        val difference = headPosition - position
        val distance = difference.magnitude()
        if (distance <= 1) {
            return
        } else if (distance > 2) {
            throw Exception("Distance between head and tail is too big: head = ${headPosition}, tail = $position")
        }

        val move = Coordinate(
            clamp(difference.x, -1, 1),
            clamp(difference.y, -1, 1)
        )
        position += move
    }
}

fun clamp(v: Int, min: Int, max: Int): Int {
    return maxOf(min, minOf(max, v))
}