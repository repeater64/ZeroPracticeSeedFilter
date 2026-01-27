package gui.general

import gui.MainWindow
import java.awt.*
import javax.swing.*

abstract class CheckboxListPanel<T>(
    val title: String,
) : JPanel() {

    private val checkboxes = mutableListOf<JCheckBox>()

    abstract fun getCheckboxListElements(): List<CheckboxListElement<T>>

    open fun init() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        border = BorderFactory.createTitledBorder(title)
        this.alignmentX = Component.LEFT_ALIGNMENT

        val listPanel = JPanel()
        listPanel.layout = BoxLayout(listPanel, BoxLayout.Y_AXIS)
        listPanel.alignmentX = Component.LEFT_ALIGNMENT

        // Add each item row
        getCheckboxListElements().forEach {
            it.init()
            checkboxes.add(it.checkBox)
            listPanel.add(it)
        }

        // 2. Wrap listPanel in a top-aligned wrapper panel
        val wrapper = JPanel(BorderLayout())
        wrapper.add(listPanel, BorderLayout.NORTH) // this pins items to top

        // 3. Scroll pane (greedy vertical expansion)
        val scrollPane = JScrollPane(wrapper)
        scrollPane.alignmentX = Component.LEFT_ALIGNMENT
        scrollPane.maximumSize = Dimension(Int.MAX_VALUE, Int.MAX_VALUE) // can grow vertically

        // 4. Add scroll pane to your vertical stack
        add(scrollPane)

        // --- 2. Select/Deselect buttons ---
        val buttonPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        buttonPanel.alignmentX = Component.LEFT_ALIGNMENT
        val selectAllButton = JButton("Select All")
        val deselectAllButton = JButton("Deselect All")
        selectAllButton.addActionListener { setAll(true) }
        deselectAllButton.addActionListener { setAll(false) }
        buttonPanel.add(selectAllButton)
        buttonPanel.add(deselectAllButton)
        add(buttonPanel)
    }

    open fun setAll(selected: Boolean) {
        checkboxes.forEach { it.isSelected = selected }
        MainWindow.optionChanged()
    }
}