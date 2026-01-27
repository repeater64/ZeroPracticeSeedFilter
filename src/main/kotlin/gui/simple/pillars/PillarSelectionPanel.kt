package gui.simple.pillars

import gui.general.CheckboxListElement
import gui.general.CheckboxListPanel
import pillars.Pillar
import pillars.SeedPillarInfo

class PillarSelectionPanel : CheckboxListPanel<Pillar>("Select Tower(s)") {
    private val elements: MutableList<PillarListElement> = mutableListOf()

    override fun getCheckboxListElements(): List<CheckboxListElement<Pillar>> {
        elements.clear()
        for (pillar in Pillar.entries) {
            val element = PillarListElement(pillar)
            elements.add(element)
        }
        return elements
    }

    fun getAllSelectedPillars(frontDragon: Boolean): List<SeedPillarInfo> {
        return elements.filter { it.checkBox.isSelected }.map { SeedPillarInfo(frontDragon, it.data) }
    }
}