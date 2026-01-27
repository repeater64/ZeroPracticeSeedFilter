package gui

import gui.advanced.AdvancedSelectorPanel
import gui.general.CardPanel
import gui.general.SeedOptionsSelectorPanel
import gui.simple.SimpleSelectorPanel
import settings.SettingsManager
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

object MainWindow : JFrame("Zero Practice Seed Filter by repeater64") {

    var simpleSelectorPanel = SimpleSelectorPanel()
    var advancedSelectorPanel = AdvancedSelectorPanel()
    var currentSelectorPanel: SeedOptionsSelectorPanel
    var selectorPanelCard = CardPanel()

    val infoPanel = InfoPanel()
    val manualPanel = ManualPanel(this)
    val autoPanel = AutoPanel(this)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 1000)
        setLocationRelativeTo(null)

        layout = BorderLayout(10, 10)
        (contentPane as JComponent).border = EmptyBorder(10, 10, 10, 10)

        if (SettingsManager.settings.simpleModeOpen) {
            currentSelectorPanel = simpleSelectorPanel
        } else {
            currentSelectorPanel = advancedSelectorPanel
        }

        simpleSelectorPanel.init()
        advancedSelectorPanel.init()
        infoPanel.init()
        manualPanel.init()
        autoPanel.init()

        val bottomContainer = JPanel()
        bottomContainer.layout = BoxLayout(bottomContainer, BoxLayout.Y_AXIS)
        bottomContainer.add(infoPanel)
        bottomContainer.add(Box.createVerticalStrut(15))
        bottomContainer.add(manualPanel)
        bottomContainer.add(Box.createVerticalStrut(15))
        bottomContainer.add(autoPanel)

        selectorPanelCard.add(simpleSelectorPanel, "simple")
        selectorPanelCard.add(advancedSelectorPanel, "advanced")

        add(selectorPanelCard, BorderLayout.CENTER)
        add(bottomContainer, BorderLayout.SOUTH)

        if (!SettingsManager.settings.simpleModeOpen) {
            switchSelector("advanced")
        }

        MainWindow.pack()

        optionChanged()
    }

    fun switchSelector(to: String) {
        val layout = selectorPanelCard.layout as CardLayout
        layout.show(selectorPanelCard, to)

        currentSelectorPanel = if (to == "simple") {
            SettingsManager.settings.simpleModeOpen = true
            simpleSelectorPanel
        } else {
            SettingsManager.settings.simpleModeOpen = false
            advancedSelectorPanel
        }

        optionChanged()

        MainWindow.pack()
    }

    fun optionChanged() {
        infoPanel.updateInfoText(false)
        autoPanel.copyNextSeedToClipboardIfEnabled()
    }
}