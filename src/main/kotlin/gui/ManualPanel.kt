package gui

import java.awt.*
import java.awt.datatransfer.StringSelection
import javax.swing.*
import javax.swing.border.EmptyBorder

class ManualPanel(val parent: MainWindow) : JPanel() {

    val copyNextToClipboardButton = JButton("Copy next seed to clipboard!")

    init {
        layout = GridBagLayout()
        border = EmptyBorder(10, 10, 10, 10)
    }

    fun init() {
        val gbc = GridBagConstraints()

        val headingLabel = JLabel("Manual Mode")
        headingLabel.font = headingLabel.font.deriveFont(Font.BOLD, 18f)

        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weightx = 1.0
        gbc.anchor = GridBagConstraints.NORTHWEST
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.insets = Insets(0, 0, 10, 0)
        add(headingLabel, gbc)

        copyNextToClipboardButton.apply {
            font = font.deriveFont(Font.BOLD, 18f)
            preferredSize = Dimension(300, 50)
            addActionListener { copyToClipboardButtonPressed() }
        }

        gbc.gridx = 0
        gbc.gridy = 1
        gbc.weightx = 1.0
        gbc.anchor = GridBagConstraints.SOUTHEAST
        gbc.fill = GridBagConstraints.NONE
        gbc.insets = Insets(10, 0, 0, 0) // Gap above the button
        add(copyNextToClipboardButton, gbc)

        SwingUtilities.invokeLater {
            copyNextToClipboardButton.requestFocusInWindow()
        }
    }

    fun copyToClipboardButtonPressed() {
        val nextSeed = parent.currentSelectorPanel.getNextSeed()
        if (nextSeed == -1L) {
            JOptionPane.showMessageDialog(
                parent,
                parent.currentSelectorPanel.getInvalidSelectionErrorPopup(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
        } else if (nextSeed == -2L) {
            JOptionPane.showMessageDialog(
                parent,
                "No seeds in the filtered database were found that meet these criteria! Try a more common spawn type or report this issue if you think it shouldn't be happening.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
        } else {
            val selection = StringSelection(nextSeed.toString())
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(selection, null)

            parent.infoPanel.updateInfoText(true)

            Timer(2000) {
                parent.infoPanel.updateInfoText(false)
            }.apply {
                isRepeats = false
                start()
            }
        }
    }
}