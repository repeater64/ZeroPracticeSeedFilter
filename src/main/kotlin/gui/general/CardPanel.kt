package gui.general

import java.awt.CardLayout
import java.awt.Dimension
import javax.swing.JPanel

class CardPanel : JPanel(CardLayout()) {
    override fun getPreferredSize(): Dimension {
        // Only calculate preferred size based on the visible component
        for (c in components) {
            if (c.isVisible) {
                return c.preferredSize
            }
        }
        return super.getPreferredSize()
    }
}