package day6

import java.io.File
import kotlin.math.max

fun main(args: Array<String>) {
    val input = File(args[0]).readText()
    println("A: " + findAnswer(input, 4))
    println("B: " + findAnswer(input, 14))
}

fun findAnswer(input: String, uniqueCharactersRequired: Int) : Int {
    val lastChars = ArrayDeque<Char>()
    var skipUntil = uniqueCharactersRequired - 1
    input.forEachIndexed { index, char ->
        if (lastChars.contains(char)) {
            val skipUntilDelta = 1 + lastChars.indexOfLast { it == char }
            val potentialSkipUntil = index + skipUntilDelta
            skipUntil = max(skipUntil, potentialSkipUntil)
        } else if (index >= skipUntil){
            return index + 1
        }
        if (lastChars.size >= (uniqueCharactersRequired - 1))
        {
            lastChars.removeFirst()
        }
        lastChars.addLast(char)
    }
    return -1
}
