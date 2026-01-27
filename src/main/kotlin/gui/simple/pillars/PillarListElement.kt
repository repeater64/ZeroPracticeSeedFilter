package gui.simple.pillars

import gui.general.CheckboxListElement
import pillars.Pillar
import settings.SettingsManager

class PillarListElement(val pillar: Pillar) : CheckboxListElement<Pillar>(pillar) {
    init {
        checkBox.text = pillar.displayName
    }

    override fun addAdditionalElements() {
        super.addAdditionalElements()

        // Set up initial state based on settings
        if (SettingsManager.settings.simplePillars.contains(pillar)) {
            checkBox.isSelected = true
        } else {
            checkBox.isSelected = false
        }

        checkBox.addActionListener { if (checkBox.isSelected) SettingsManager.settings.simplePillars.add(pillar) else SettingsManager.settings.simplePillars.remove(pillar) }
    }
}