package gui

import com.sun.jna.Platform
import gui.general.FileSelectorPanel
import gui.mcinstance.MinecraftLogWatcher
import gui.mcinstance.WindowsActiveInstanceListener
import settings.SettingsManager
import java.awt.*
import java.awt.datatransfer.StringSelection
import java.io.File
import javax.swing.*
import javax.swing.border.EmptyBorder

class AutoPanel(val parent: MainWindow) : JPanel() {

    private val enableCheckbox = JCheckBox("Enable Automatic Mode")
    private val fileSelector = FileSelectorPanel(parent=this)
    private val autoDetectButton = JButton("Auto detect from currently running instance")

    lateinit var logWatcher: MinecraftLogWatcher

    init {
        layout = GridBagLayout()
        border = EmptyBorder(10, 10, 10, 10)
    }

    fun init() {
        val gbc = GridBagConstraints()

        val headingLabel = JLabel("Automatic Mode")
        headingLabel.font = headingLabel.font.deriveFont(Font.BOLD, 18f)

        gbc.gridx = 0
        gbc.gridy = 0
        gbc.gridwidth = 1
        gbc.weightx = 1.0
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.insets = Insets(0, 0, 10, 0) // Bottom padding
        add(headingLabel, gbc)

        gbc.gridy = 1
        gbc.insets = Insets(0, 0, 5, 0)
        add(enableCheckbox, gbc)

        val explanation = object : JTextArea(
            "If using automatic mode, a new seed will automatically be copied " +
                    "to your clipboard each time you generate a new world, so you can " +
                    "just paste in a seed every time without needing to tab out and " +
                    "press the Copy button."
        ) {
            private val dummy = JTextArea()

            override fun getPreferredSize(): Dimension {
                dummy.text = this.text
                dummy.font = this.font
                dummy.lineWrap = true
                dummy.wrapStyleWord = true
                dummy.columns = 70

                val fm = dummy.getFontMetrics(dummy.font)
                val targetWidth = fm.charWidth('m') * dummy.columns

                dummy.setSize(targetWidth, Int.MAX_VALUE)

                return dummy.preferredSize
            }
        }
        explanation.columns = 70
        explanation.rows = 0
        explanation.lineWrap = true
        explanation.wrapStyleWord = true
        explanation.isEditable = false
        explanation.isOpaque = false
        explanation.font = UIManager.getFont("Label.font")
        explanation.border = null

        gbc.gridy = 2
        gbc.insets = Insets(0, 0, 20, 0) // Extra gap after text
        add(explanation, gbc)

        val pathLabel = JLabel("Select your .minecraft/logs/latest.log file for the instance you're using:")
        gbc.gridy = 3
        gbc.insets = Insets(0, 0, 5, 0)
        add(pathLabel, gbc)

        val selectorRow = JPanel(BorderLayout(5, 0)) // 5px horizontal gap
        selectorRow.isOpaque = false

        fileSelector.preferredSize = Dimension(300, 30)
        selectorRow.add(fileSelector, BorderLayout.CENTER)
        selectorRow.add(autoDetectButton, BorderLayout.EAST)

        gbc.gridy = 4
        gbc.insets = Insets(0, 0, 0, 0)
        add(selectorRow, gbc)

        val spacer = JPanel()
        spacer.isOpaque = false
        gbc.gridy = 5
        gbc.weighty = 1.0
        add(spacer, gbc)

        autoDetectButton.addActionListener {
            tryAutoDetect(true)
        }

        // Set up initial state based on settings
        enableCheckbox.isSelected = SettingsManager.settings.autoModeEnabled

        enableCheckbox.addActionListener {
            toggleControls(enableCheckbox.isSelected)
            copyNextSeedToClipboardIfEnabled()
            SettingsManager.settings.autoModeEnabled = enableCheckbox.isSelected
        }
        toggleControls(enableCheckbox.isSelected)


        logWatcher = MinecraftLogWatcher(
            targetLineSuffix = "Preparing start region for dimension minecraft:overworld"
        ) {
            copyNextSeedToClipboardIfEnabled()
        }

        tryAutoDetect(false)

        copyNextSeedToClipboardIfEnabled()
    }

    fun copyNextSeedToClipboardIfEnabled() {
        if (enableCheckbox.isSelected) {
            val nextSeed = parent.currentSelectorPanel.getNextSeed()
            if (nextSeed > 0) {
                val selection = StringSelection(nextSeed.toString())
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(selection, null)
            }
        }
    }

    private fun tryAutoDetect(showErrorIfFail: Boolean) {
        if (Platform.isWindows()) {
            val directoryMaybe = WindowsActiveInstanceListener.tryGetDotMinecraftDirectory()
            directoryMaybe?.let { directory ->
                val logsDir = File(directory, "logs")
                val latestLog = File(logsDir, "latest.log")

                if (latestLog.exists()) {
                    fileSelector.setFile(latestLog)
                    logWatcher.startWatching(latestLog)

                    if (!showErrorIfFail) {
                        // This was an auto call on startup, make sure auto mode is enabled
                        enableCheckbox.isSelected = true
                        toggleControls(enableCheckbox.isSelected)
                    }
                } else {
                    if (!showErrorIfFail) return
                    JOptionPane.showMessageDialog(
                        parent,
                        "Found .minecraft at $directory, but could not find logs/latest.log. Try selecting the latest.log file manually?",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            } ?: run {
                if (!showErrorIfFail) return
                JOptionPane.showMessageDialog(
                    parent,
                    "Failed to auto-detect! Are you sure you have Minecraft open? If auto-detect isn't working, just select your latest.log file manually.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        } else {
            if (!showErrorIfFail) return
            JOptionPane.showMessageDialog(
                parent,
                "Auto-detect only works on Windows! Please locate your latest.log folder manually. Sorry :(",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }

    private fun toggleControls(enabled: Boolean) {
        fileSelector.isEnabled = enabled
        autoDetectButton.isEnabled = enabled
    }
}