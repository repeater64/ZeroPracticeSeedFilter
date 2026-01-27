package gui.general

import gui.MainWindow
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.border.EmptyBorder

abstract class CheckboxListElement<T>(val data: T) : JPanel(FlowLayout(FlowLayout.LEFT, 5, 0)) {
    val checkBox = JCheckBox("a")

    open fun addAdditionalElements() {}

    fun init() {
        this.add(checkBox)
        checkBox.addActionListener { MainWindow.optionChanged() }
        this.border = EmptyBorder(2, 0, 2, 0)
        addAdditionalElements()
    }
}