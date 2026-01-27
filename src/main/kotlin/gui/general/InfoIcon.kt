package gui.general

import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.SwingConstants

class InfoIcon(tooltip: String): JLabel("\u2139", SwingConstants.CENTER) {
    init {
        this.apply {
            toolTipText = "<html>$tooltip</html>"
            font = font.deriveFont(Font.BOLD)
            foreground = Color.BLUE
            preferredSize = Dimension(18, 18)
            maximumSize = Dimension(18, 18)
            minimumSize = Dimension(18, 18)

            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(2, 2, 2, 2) // padding inside the box
            )
            background = Color.WHITE
            isOpaque = true
        }
    }
}