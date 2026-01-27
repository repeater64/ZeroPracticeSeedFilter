package gui.mcinstance

import java.io.File
import java.io.RandomAccessFile
import javax.swing.SwingUtilities

class MinecraftLogWatcher(
    private val targetLineSuffix: String,
    private val onMatch: () -> Unit
) {
    private var watcherThread: Thread? = null
    @Volatile private var isRunning = false

    private var currentFile: File? = null

    fun startWatching(file: File) {
        stopWatching()

        if (!file.exists()) return

        currentFile = file
        isRunning = true

        watcherThread = Thread {
            monitorFile(file)
        }.apply {
            isDaemon = true
            start()
        }
    }

    fun stopWatching() {
        isRunning = false
        watcherThread?.join(500)
        watcherThread = null
    }

    private fun monitorFile(file: File) {
        try {
            RandomAccessFile(file, "r").use { reader ->
                var filePointer = reader.length()
                reader.seek(filePointer)

                while (isRunning) {
                    val length = file.length()

                    // File grew (new logs added)
                    if (length > filePointer) {
                        reader.seek(filePointer)
                        var line: String? = reader.readLine()
                        while (line != null) {
                            if (line.trim().endsWith(targetLineSuffix)) {
                                SwingUtilities.invokeLater {
                                    onMatch()
                                }
                            }
                            filePointer = reader.filePointer
                            line = reader.readLine()
                        }
                    }
                    // File got smaller (Log rotated/reset)
                    else if (length < filePointer) {
                        filePointer = 0
                        reader.seek(0)
                    }

                    // Sleep for a bit to avoid eating CPU
                    Thread.sleep(1000)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Optional: Handle file read errors (e.g. file deleted)
        }
    }
}