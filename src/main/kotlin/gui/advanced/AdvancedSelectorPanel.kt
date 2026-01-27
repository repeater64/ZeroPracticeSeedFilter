package gui.advanced

import LoadedSeedsData
import SeedInfo
import gui.MainWindow
import gui.general.InfoIcon
import gui.general.SeedOptionsSelectorPanel
import gui.simple.spawns.SpawnSelectionPanel
import pillars.Pillar
import pillars.SeedPillarInfo
import settings.SettingsManager
import java.awt.BorderLayout
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

class AdvancedSelectorPanel : JPanel(BorderLayout(10, 10)), SeedOptionsSelectorPanel {

    lateinit var grid: SelectorGrid
    val flatBuriedsCheckbox = JCheckBox("Force buried spawns to be flat")

    fun init() {
        border = EmptyBorder(10, 10, 10, 10)

        // Set up initial state based on settings
        flatBuriedsCheckbox.isSelected = SettingsManager.settings.advancedFlatBurieds

        val switchButton = JButton("Switch to Simple Selector")
        switchButton.addActionListener { MainWindow.switchSelector("simple") }

        val pillarsToList = mutableListOf<SeedPillarInfo>()
        for (pillar in Pillar.entries) {
            pillarsToList.add(SeedPillarInfo(true, pillar))
        }
        for (pillar in Pillar.entries) {
            pillarsToList.add(SeedPillarInfo(false, pillar))
        }

        grid = SelectorGrid(pillarsToList, SpawnSelectionPanel.spawnsToList)
        add(JScrollPane(grid), BorderLayout.CENTER)

        add(switchButton, BorderLayout.NORTH)

        val flatBuriedsPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        flatBuriedsPanel.add(flatBuriedsCheckbox)
        flatBuriedsCheckbox.addActionListener {
            MainWindow.optionChanged()
            SettingsManager.settings.advancedFlatBurieds = flatBuriedsCheckbox.isSelected
        }
        val infoLabel = InfoIcon("<html><p>If checked, this will only include seeds where your pearl clip should be 100% consistent because<br> the O-level is the same for the block in front of and behind where you mine up.</p><br><p>If not checked, these non-flat spawns where pearl clips might occasionally fail, will still be included.</p></html>")
        flatBuriedsPanel.add(infoLabel)
        flatBuriedsPanel.alignmentX = Component.LEFT_ALIGNMENT
        add(flatBuriedsPanel, BorderLayout.SOUTH)
    }

    override fun getNumApplicableSeeds(): Int {
        return getAllSeeds().size
    }

    override fun getNextSeed(): Long {
        var allSeeds = getAllSeeds()
        if (allSeeds.isEmpty()) return -1

        // Avoid repeating recently used seeds
        allSeeds.removeAll(LoadedSeedsData.recentlyUsedSeeds)
        if (allSeeds.isEmpty()) {
            // Oops, run out of seeds, we can reset recently used ones.
            LoadedSeedsData.recentlyUsedSeeds.clear()
            allSeeds = getAllSeeds()
        }

        return allSeeds.random()
    }

    private fun getAllSeeds(): MutableSet<Long> {
        val allSeeds = hashSetOf<Long>()
        grid.getEnabledCombinations().forEach {combo ->
            combo.second.getSpawns(flatBuriedsCheckbox.isSelected).forEach {spawn ->
                allSeeds.addAll(LoadedSeedsData.getData().map[SeedInfo(combo.first, spawn)] ?: emptyList())
            }
        }
        return allSeeds
    }

    override fun getInvalidSelectionErrorPopup(): String {
        return "You must select at least one grid square!"
    }

    override fun getInvalidSelectionWarning(): String {
        return "Please select at least one grid square!"
    }
}