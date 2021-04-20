package spazzylemons.protoplayermodels.config

import sun.security.action.GetPropertyAction
import java.io.File
import java.io.StringReader
import java.io.StringWriter
import java.io.Writer
import java.security.AccessController
import java.util.Properties

class CommentedProperties(source: File) {
    sealed class Line(val value: String) {
        class Comment(value: String) : Line(value)
        class Entry(val key: String, val storedValue: String, value: String) : Line(value)
    }

    private val lines = mutableListOf<Line>()

    init {
        source.forEachLine { line ->
            val props = Properties()
            props.load(StringReader(line))
            props.entries.firstOrNull()?.let {
                lines += Line.Entry(it.key as String, it.value as String, line)
            } ?: run {
                lines += Line.Comment(line)
            }
        }
    }

    private val properties = Properties().apply {
        source.reader().use { load(it) }
    }

    operator fun get(key: String): String? {
        return properties.getProperty(key)
    }

    operator fun set(key: String, value: String) {
        if (!properties.containsKey(key)) {
            val writer = StringWriter()
            Properties().apply { setProperty(key, value) }.store(writer, null)
            lines += Line.Entry(key, value, writer.toString())
        }
        properties.setProperty(key, value)
    }

    fun save(dst: Writer) {
        for (line in lines) {
            when (line) {
                is Line.Comment -> {
                    dst.write(line.value)
                    dst.write(lineSeparator)
                }
                is Line.Entry -> {
                    val props = Properties()
                    props.setProperty(line.key, this[line.key] ?: line.storedValue)
                    props.store(SkipFirstLineWriter(dst), null)
                }
            }
        }
        dst.flush()
    }
}

// custom writer that discards writes until a new line is seen
private class SkipFirstLineWriter(private val dst: Writer) : Writer() {
    private var firstLine = true

    override fun close() {
        dst.close()
    }

    override fun flush() {
        dst.flush()
    }

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        if (firstLine) {
            for (i in 0 until len) {
                if (cbuf[i + off] == '\n') {
                    firstLine = false
                    if (i + 1 != len) {
                        dst.write(cbuf, i + off + 1, len - i - 1)
                    }
                    return
                }
            }
        } else {
            dst.write(cbuf, off, len)
        }
    }
}

private val lineSeparator = AccessController.doPrivileged(GetPropertyAction("line.separator"))