package day7

fun main(args: Array<String>) {
    val input = java.io.File(args[0]).readLines()
    val shell = Shell(input)
    shell.processCommands()

    println("A: " + getAnswerA(shell))
    println("B: " + getAnswerB(shell))
}

fun getAnswerA(shell: Shell): Int {
    return shell.root.getAllChildren()
        .filterIsInstance<Directory>()
        .map { it.getSize() }
        .filter { it <= 100_000 }
        .sum()
}

fun getAnswerB(shell: Shell): Int {
    val usedSpace = shell.root.getAllChildren()
        .filterIsInstance<File>()
        .sumOf { it.getSize() }
    val unusedSpace = 70_000_000 - usedSpace
    val missingSpace = 30_000_000 - unusedSpace
    return shell.root.getAllChildren()
        .asSequence()
        .filterIsInstance<Directory>()
        .map { it.getSize() }
        .filter { it >= missingSpace }
        .sorted()
        .first()
}

class Shell(private val inputLines: List<String>) {
    private var currentLineIndex = 0
    val root = Directory(null, "root")
    private var currentDir: Directory = root

    fun processCommands() {
        while (currentLineIndex < inputLines.size) {
            handleCommand()
        }
    }

    private fun handleCommand() {
        val line = inputLines[currentLineIndex]
        if (line.first() != '$') {
            throw java.lang.IllegalArgumentException("Line '$line' does not start with '$'")
        }

        val tokens = line.split("\\s+".toRegex())
        when (val command = tokens[1]) {
            "cd" -> {
                when (val target = tokens[2]) {
                    ".." -> moveOneDirectoryOut()
                    "/" -> currentDir = currentDir.getRoot()
                    else -> changeDirectoryTo(target)
                }
                currentLineIndex++
            }
            "ls" -> listContent()
            else -> throw IllegalArgumentException("'$command' is not a recognized command")
        }
    }

    private fun moveOneDirectoryOut() {
        currentDir = currentDir.parent!!
    }

    private fun changeDirectoryTo(target: String) {
        currentDir = if (currentDir.contains(target)) {
            currentDir.getContainedDirectory(target)
        } else {
            val newDir = Directory(currentDir, target)
            currentDir.addChildNode(newDir)
            newDir
        }
    }

    private fun listContent() {
        val listedContent = inputLines.drop(currentLineIndex + 1)
            .takeWhile { !it.startsWith('$') }
        currentLineIndex += 1 + listedContent.size

        for (item in listedContent) {
            val tokens = item.split("\\s+".toRegex())
            val name = tokens[1]
            if (currentDir.contains(name)) {
                continue
            }

            if (tokens[0] == "dir") {
                val directory = Directory(currentDir, name)
                currentDir.addChildNode(directory)
            } else {
                val file = File(currentDir, name, tokens[0].toInt())
                currentDir.addChildNode(file)
            }
        }

    }
}

abstract class Node(val parent: Directory?, val name: String) {
    abstract fun getSize(): Int

    fun getPath(): String {
        return (parent?.getPath() ?: "") + "/" + name
    }

    override fun toString(): String {
        return name
    }
}

class File(parent: Directory?, name: String, private val size: Int): Node(parent, name) {
    override fun getSize(): Int {
        return size
    }
}

class Directory(parent: Directory?, name: String): Node(parent, name) {
    private val content: MutableSet<Node> = mutableSetOf()

    override fun getSize(): Int {
        return content.sumOf { it.getSize() }
    }

    fun addChildNode(child: Node) {
        content.add(child)
    }

    fun getContent(): Set<Node> {
        return content
    }

    fun contains(name: String): Boolean {
        return content.any { it.name == name }
    }

    fun getContainedDirectory(name: String): Directory {
        return (content.first { it.name == name } as Directory)
    }

    fun getRoot(): Directory {
        return parent?.getRoot() ?: this
    }

    fun getAllChildren(): List<Node> {
        return listOf(this) + content.flatMap {
            if (it is Directory) {
                it.getAllChildren()
            } else {
                listOf(it)
            }
        }
    }
}

