package gui.advanced

import gui.MainWindow
import java.awt.*
import javax.swing.JComponent


class ColorToggleCell(val parent: SelectorGrid, val row: Int, val col: Int) : JComponent() {
    var selected = false
        set(value) {
            repaint()
            field = value
        }
    private val selectedColor = Color(0x4CAF50)
    private val unselectedColor = Color(0xF44336)

    init {
        preferredSize = Dimension(30, 30)
        addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mousePressed(e: java.awt.event.MouseEvent) {
                selected = !selected
                repaint()
                parent.cellToggled(row, col)
            }
        })
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g.create() as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = if (selected) selectedColor else unselectedColor
        g2.fillRect(0, 0, width, height)
        g2.color = Color.DARK_GRAY
        g2.drawRect(0, 0, width-1, height-1)
        g2.dispose()
    }
}