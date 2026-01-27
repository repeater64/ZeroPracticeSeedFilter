package gui

import LoadedSeedsData
import java.awt.*
import javax.swing.*
import javax.swing.text.StyleConstants


class InfoPanel : JPanel(BorderLayout(10, 10)) {
    private val infoPane = JTextPane()


    val titleStyle = infoPane.addStyle("title", null).apply {
        StyleConstants.setFontSize(this, 16)
        StyleConstants.setBold(this, true)
    }
    val normalStyle = infoPane.addStyle("normal", null).apply {
        StyleConstants.setFontSize(this, 12)
    }
    val warningStyle = infoPane.addStyle("warning", null).apply {
        StyleConstants.setForeground(this, Color.RED)
        StyleConstants.setBold(this, true)
    }
    val greenStyle = infoPane.addStyle("green", null).apply {
        StyleConstants.setForeground(this, Color.GREEN)
        StyleConstants.setBold(this, true)
    }

    fun init() {
        infoPane.apply {
            isEditable = false
            isFocusable = false
            background = UIManager.getColor("Label.background")
            border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        }

        add(JScrollPane(infoPane), BorderLayout.CENTER)

        updateInfoText(false)
    }

    fun updateInfoText(showCopiedToClipboardMessage: Boolean) {
        val numSeeds = MainWindow.currentSelectorPanel.getNumApplicableSeeds()
        val totalSeeds = LoadedSeedsData.getData().totalSeeds
        val percentage = (numSeeds.toDouble() / totalSeeds) * 100

        val doc = infoPane.styledDocument
        doc.remove(0, doc.length)

        if (numSeeds > 0) {
            doc.insertString(doc.length, "Number of matching seeds in database: $numSeeds", titleStyle)
            doc.insertString(doc.length, "\n(${String.format("%.1f", percentage)}% of seeds)", normalStyle)
            if (showCopiedToClipboardMessage) {
                doc.insertString(doc.length, "\n\nCopied next seed to the clipboard!", greenStyle)
            } else {
                doc.insertString(doc.length, "\n\n", normalStyle)
            }
        } else {
            doc.insertString(doc.length, "${MainWindow.currentSelectorPanel.getInvalidSelectionWarning()}\n\n\n\n", warningStyle)
        }
    }
}