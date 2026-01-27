package gui.simple.spawns

import LoadedSeedsData
import gui.general.CheckboxListElement
import gui.general.InfoIcon
import settings.SettingsManager
import spawn.SpawnOption
import javax.swing.JLabel

class SpawnListElement(val option: SpawnOption, val parent: SpawnSelectionPanel) : CheckboxListElement<SpawnOption>(option) {
    init {
        checkBox.text = option.displayName
    }

    val percentageLabel: JLabel = JLabel("?%")

    override fun addAdditionalElements() {
        // Set up initial state based on settings
        if (SettingsManager.settings.simpleSpawns.contains(option)) {
            checkBox.isSelected = true
        } else {
            checkBox.isSelected = false
        }

        checkBox.addActionListener { if (checkBox.isSelected) SettingsManager.settings.simpleSpawns.add(option) else SettingsManager.settings.simpleSpawns.remove(option) }

        // Info icon in a little box
        val infoLabel = InfoIcon(option.tooltip)
        add(infoLabel)

        add(percentageLabel)

        updatePercentageVisibility()

        checkBox.addActionListener {
            parent.updatePercentages()
            updatePercentageVisibility()
        }
    }

    fun getNumSeeds(): Int {
        val spawnCounts = LoadedSeedsData.getData().spawnCounts
        var toReturn = 0
        if (parent.flatBuriedsCheckbox.isSelected) {
            option.includedSpawnsWhenFlatForced.forEach { toReturn += spawnCounts[it] ?: 0 }
        } else {
            option.includedSpawns.forEach { toReturn += spawnCounts[it] ?: 0 }
        }
        return toReturn
    }

    fun updatePercentage(totalSelectedSeeds: Int) {
        val percentage = (getNumSeeds().toDouble() / totalSelectedSeeds) * 100
        percentageLabel.text = "${String.format("%.1f", percentage)}%"
    }

    fun updatePercentageVisibility() {
        percentageLabel.isVisible = checkBox.isSelected && parent.useNaturalFrequenciesCheckbox.isSelected
    }
}