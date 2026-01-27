package gui.simple

import gui.MainWindow
import gui.general.InfoIcon
import settings.SettingsManager
import java.awt.*
import javax.swing.*


class FrontBackPanel : JPanel(BorderLayout(10, 10)) {

    val frontDragonCheckBox = JCheckBox("Front Dragon", true)
    val backDragonCheckBox = JCheckBox("Back Dragon", true)

    fun init() {
        val frontBackPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        frontBackPanel.add(frontDragonCheckBox)
        frontBackPanel.add(backDragonCheckBox)
        frontDragonCheckBox.addActionListener { MainWindow.optionChanged(); SettingsManager.settings.simpleFrontDragon = frontDragonCheckBox.isSelected }
        backDragonCheckBox.addActionListener { MainWindow.optionChanged(); SettingsManager.settings.simpleBackDragon = backDragonCheckBox.isSelected }

        // Set up initial state based on settings
        frontDragonCheckBox.isSelected = SettingsManager.settings.simpleFrontDragon
        backDragonCheckBox.isSelected = SettingsManager.settings.simpleBackDragon

        val oneInEightPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        val oneInEightHoverText = "<html><p>Unfortunately, one in eight (when the dragon flies to one of the towers directly aligned with the spawn platform<br> rather than a diagonal tower) is NOT seed based, and is just a random chance on your computer.<br><br>This means that unfortunately you can't use this tool to specifically practice 1/8 zeroes, and when trying to <br>practice normal diagonal zeroes you will get 1/8 happening randomly 1/8th of the time.</p></html>"
        oneInEightPanel.add(JLabel("<html><strike>One in Eight</strike></html>").apply { toolTipText = oneInEightHoverText })
        oneInEightPanel.add(InfoIcon(oneInEightHoverText))

        val topPanel = JPanel(BorderLayout())
        topPanel.add(frontBackPanel, BorderLayout.WEST)
        topPanel.add(oneInEightPanel, BorderLayout.EAST)

        add(topPanel, BorderLayout.NORTH)
    }
}