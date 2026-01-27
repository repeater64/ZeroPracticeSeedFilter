package gui.advanced

import gui.MainWindow
import pillars.SeedPillarInfo
import settings.SettingsManager
import spawn.SpawnOption
import javax.swing.*
import java.awt.*
import java.util.LinkedList
import java.util.Queue
import java.util.Stack
import javax.swing.border.EmptyBorder

class SelectorGrid(
    private val pillars: List<SeedPillarInfo>,
    private val spawns: List<SpawnOption>
) : JPanel() {

    private val buttons: Array<Array<ColorToggleCell>> = Array(pillars.size) { row -> Array(spawns.size) { col -> ColorToggleCell(this, row, col) } }

    private val changelist: Stack<List<Pair<Int, Int>>> = Stack()

    init {
        layout = GridBagLayout()
        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.BOTH
        gbc.insets = Insets(0, 0, 0, 0)

        // Top-left corner
        gbc.gridx = 0
        gbc.gridy = 0

        val cornerPanel = JPanel()
        cornerPanel.layout = BoxLayout(cornerPanel, BoxLayout.Y_AXIS)

        val cornerButtonPanel = JPanel()
        cornerButtonPanel.layout = FlowLayout(FlowLayout.CENTER)
        cornerButtonPanel.add(JButton("Select All").apply { addActionListener { selectAll(true) }; maximumSize=preferredSize })
        cornerButtonPanel.add(Box.createHorizontalStrut(2))
        cornerButtonPanel.add(JButton("Deselect All").apply { addActionListener { selectAll(false) }; maximumSize=preferredSize })
        cornerButtonPanel.border = EmptyBorder(2, 2, 2, 2)
        cornerButtonPanel.maximumSize = cornerButtonPanel.preferredSize
        cornerPanel.add(cornerButtonPanel)

        val undoPanel = JPanel(FlowLayout(FlowLayout.CENTER))
        undoPanel.add(JButton("Undo").apply { addActionListener { undo() }; maximumSize=preferredSize })
        undoPanel.maximumSize = undoPanel.preferredSize
        cornerPanel.add(undoPanel)

        add(cornerPanel, gbc)

        // --- Column labels + buttons (no rotation) ---
        for ((col, spawn) in spawns.withIndex()) {
            gbc.gridx = col + 1
            gbc.gridy = 0

            val colPanel = JPanel()
            colPanel.layout = BoxLayout(colPanel, BoxLayout.Y_AXIS)
            colPanel.border = EmptyBorder(2, 2, 2, 2)

            colPanel.alignmentY = Component.TOP_ALIGNMENT

            if (!spawn.shortDisplayName.contains("<br>")) colPanel.add(Box.createVerticalStrut(16))

            // Column label
            val labelPanel = JPanel(FlowLayout(FlowLayout.CENTER, 0, 0))
            labelPanel.add(JLabel("<html><div style='text-align: center'>${spawn.shortDisplayName}</div></html>"))
            colPanel.add(labelPanel)

            // Fix vertical size so it doesn't push buttons down
            labelPanel.maximumSize = Dimension(Int.MAX_VALUE, labelPanel.preferredSize.height)
            colPanel.add(labelPanel)

            // Buttons
            val allBtn = JButton("All")
            val noneBtn = JButton("None")
            allBtn.alignmentX = Component.CENTER_ALIGNMENT
            noneBtn.alignmentX = Component.CENTER_ALIGNMENT

            colPanel.add(allBtn)
            colPanel.add(Box.createVerticalStrut(2))
            colPanel.add(noneBtn)

            colPanel.toolTipText = "<html>${spawn.tooltip}</html>"

            // Actions
            allBtn.addActionListener { selectColumn(col, true) }
            noneBtn.addActionListener { selectColumn(col, false) }

            add(colPanel, gbc)
        }

        // Rows: labels + inline buttons + toggle buttons
        for ((row, pillar) in pillars.withIndex()) {
            // Row label + inline buttons
            gbc.gridx = 0
            gbc.gridy = row + 1

            val rowPanel = JPanel(FlowLayout(FlowLayout.RIGHT, 5, 0))
            rowPanel.border = EmptyBorder(0,2,0,2)
            rowPanel.add(JLabel(pillar.displayName, SwingConstants.RIGHT))

            val allBtn = JButton("All")
            val noneBtn = JButton("None")
            rowPanel.add(allBtn)
            rowPanel.add(noneBtn)
            add(rowPanel, gbc)

            // Row button logic
            allBtn.addActionListener { selectRow(row, true) }
            noneBtn.addActionListener { selectRow(row, false) }

            // Add toggle buttons
            for (col in spawns.indices) {
                gbc.gridx = col + 1
                gbc.gridy = row + 1
                val btn = ColorToggleCell(this, row, col)
                btn.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
                buttons[row][col] = btn
                add(btn, gbc)
            }
        }

        loadFromSettings()
    }

    private fun loadFromSettings() {
        for (pair in SettingsManager.settings.advancedEnabledGridCoords) {
            val row = pair.first
            val col = pair.second

            val button = buttons[row][col]
            button.selected = true
        }
    }

    private fun buttonStateChanged(row: Int, col: Int, button: ColorToggleCell) {
        if (button.selected) {
            SettingsManager.settings.advancedEnabledGridCoords.add(Pair(row, col))
        } else {
            SettingsManager.settings.advancedEnabledGridCoords.remove(Pair(row, col))
        }
    }

    private fun selectRow(row: Int, selected: Boolean) {
        val changes = mutableListOf<Pair<Int, Int>>()
        for (col in spawns.indices) {
            val button = buttons[row][col]
            if (button.selected != selected) changes.add(Pair(row, col))
            button.selected = selected
            buttonStateChanged(row, col, button)
        }
        changelist.push(changes)
        MainWindow.optionChanged()
    }

    private fun selectColumn(col: Int, selected: Boolean) {
        val changes = mutableListOf<Pair<Int, Int>>()
        for (r in pillars.indices) {
            val button = buttons[r][col]
            if (button.selected != selected) changes.add(Pair(r, col))
            button.selected = selected
            buttonStateChanged(r, col, button)
        }
        changelist.push(changes)
        MainWindow.optionChanged()
    }

    private fun selectAll(selected: Boolean) {
        val changes = mutableListOf<Pair<Int, Int>>()
        for (col in spawns.indices) {
            for (r in pillars.indices) {
                val button = buttons[r][col]
                if (button.selected != selected) changes.add(Pair(r, col))
                button.selected = selected
                buttonStateChanged(r, col, button)
            }
        }
        changelist.push(changes)
        MainWindow.optionChanged()
    }

    fun cellToggled(row: Int, col: Int) {
        changelist.push(listOf(Pair(row, col)))
        MainWindow.optionChanged()
        buttonStateChanged(row, col, buttons[row][col])
    }

    private fun undo() {
        if (changelist.empty()) return
        for (change in changelist.pop()) {
            val button = buttons[change.first][change.second]
            button.selected = !button.selected
            buttonStateChanged(change.first, change.second, button)
        }
        MainWindow.optionChanged()
    }

    fun getEnabledCombinations(): List<Pair<SeedPillarInfo, SpawnOption>> {
        val toReturn = mutableListOf<Pair<SeedPillarInfo, SpawnOption>>()

        for ((col, spawn) in spawns.withIndex()) {
            for ((row, pillar) in pillars.withIndex()) {
                val button = buttons[row][col]
                if (button.selected) {
                    toReturn.add(Pair(pillar, spawn))
                }
            }
        }

        return toReturn
    }
}