package gui.general

import gui.AutoPanel
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

class FileSelectorPanel(
    private val promptText: String = "Click to select file",
    private val parent: AutoPanel
) : JPanel() {

    private var selectedFile: File? = null
    private var lastDirectory: File? = null
    private val label = JLabel(promptText)
    private val browseButton = JButton("Browse...")

    init {
        layout = BorderLayout(5, 0)
        add(label, BorderLayout.CENTER)
        add(browseButton, BorderLayout.EAST)
        border = BorderFactory.createLineBorder(Color.DARK_GRAY)
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        isOpaque = true

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent) {
                if (isEnabled) background = Color(0xeeeeee)
            }
            override fun mouseExited(e: MouseEvent) {
                background = null
            }
            override fun mouseClicked(e: MouseEvent) {
                if (isEnabled) openFileChooser()
            }
        })

        browseButton.addActionListener { openFileChooser() }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        browseButton.isEnabled = enabled
        label.isEnabled = enabled
        // If disabled, remove any hover background immediately
        if (!enabled) background = null
    }

    private fun openFileChooser() {
        val chooser = JFileChooser()
        chooser.fileFilter = FileNameExtensionFilter("Log files", "log")
        chooser.isAcceptAllFileFilterUsed = false

        // Logic: Use last used directory OR find .minecraft OR default to user home
        chooser.currentDirectory = lastDirectory ?: getDefaultDirectory()

        val result = chooser.showOpenDialog(this)
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.selectedFile
            label.text = selectedFile!!.absolutePath
            label.toolTipText = selectedFile!!.absolutePath
            lastDirectory = selectedFile!!.parentFile

            parent.logWatcher.startWatching(chooser.selectedFile)
        }
    }

    private fun getDefaultDirectory(): File {
        val os = System.getProperty("os.name", "").lowercase()
        val userHome = File(System.getProperty("user.home"))

        if (os.contains("win")) {
            val appDataPath = System.getenv("APPDATA")
            if (!appDataPath.isNullOrBlank()) {
                val minecraftLogsDir = File(File(appDataPath, ".minecraft"), "logs")

                if (minecraftLogsDir.exists() && minecraftLogsDir.isDirectory) {
                    return minecraftLogsDir
                }
            }
        }
        return userHome
    }

    fun setFile(file: File) {
        this.selectedFile = file
        this.lastDirectory = file.parentFile
        this.label.text = file.absolutePath
        this.label.toolTipText = file.absolutePath

        revalidate()
        repaint()
    }

    fun getSelectedFile(): File? = selectedFile
}