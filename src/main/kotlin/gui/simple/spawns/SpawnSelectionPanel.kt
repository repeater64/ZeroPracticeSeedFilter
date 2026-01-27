package gui.simple.spawns

import gui.general.CheckboxListElement
import gui.general.CheckboxListPanel
import gui.general.InfoIcon
import gui.MainWindow
import settings.SettingsManager
import spawn.SeedSpawnInfo
import spawn.SpawnOption
import spawn.SpawnType
import java.awt.*
import javax.swing.*

class SpawnSelectionPanel : CheckboxListPanel<SpawnOption>("Select Spawn Type(s)") {
    private val listElements: MutableList<SpawnListElement> = mutableListOf();

    val useNaturalFrequenciesCheckbox: JCheckBox = JCheckBox("Use natural frequencies", true)
    val flatBuriedsCheckbox = JCheckBox("Force buried spawns to be flat")

    companion object {
        val spawnsToList = listOf(
            SpawnOption("Void", "Void", "Void between the obsidian platform and the end island. <br>Should virtually guarantee a clear first pearl.", listOf(SeedSpawnInfo(SpawnType.VOID))),
            SpawnOption("Easy", "Easy", "Not total void between the obsidian platform and the end island, but a (likely) clear first pearl.", listOf(SeedSpawnInfo(SpawnType.EASY))),
            SpawnOption("Slightly Blocked", "Blocked", "Your first pearl throw might be slightly blocked, but not properly buried or overhanging.", listOf(SeedSpawnInfo(SpawnType.POTENTIALLY_MILD_OVERHANG))),
            SpawnOption("Overhang", "Overhang", "You'll need to tower up a few blocks before throwing your first pearl.", listOf(SeedSpawnInfo(SpawnType.OVERHANG))),
            SpawnOption("Proper Overhang", "Proper<br>Overhang", "You'll need to bridge backwards and upwards before throwing your first pearl.", listOf(SeedSpawnInfo(SpawnType.SEVERE_OVERHANG))),
            SpawnOption("Buried 52", "O 52", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 52), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 52)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 52))),
            SpawnOption("Buried 53", "O 53", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 53), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 53)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 53))),
            SpawnOption("Buried 54", "O 54", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 54), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 54)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 54))),
            SpawnOption("Buried 55", "O 55", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 55), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 55)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 55))),
            SpawnOption("Buried 56", "O 56", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 56), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 56)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 56))),
            SpawnOption("Buried 57", "O 57", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 57), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 57)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 57))),
            SpawnOption("Buried 58", "O 58", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 58), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 58)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 58))),
            SpawnOption("Buried 59", "O 59", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 59), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 59)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 59))),
            SpawnOption("Buried >59", "O>59", "Buried O-levels are based on the back middle block of the obsidian platform, <br>which is where you should be pearl clipping.", listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 60), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 60), SeedSpawnInfo(SpawnType.BURIED_FLAT, 61), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 61), SeedSpawnInfo(SpawnType.BURIED_FLAT, 62), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 62), SeedSpawnInfo(SpawnType.BURIED_FLAT, 63), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 63), SeedSpawnInfo(SpawnType.BURIED_FLAT, 64), SeedSpawnInfo(SpawnType.BURIED_NOT_FLAT, 64)), listOf(SeedSpawnInfo(SpawnType.BURIED_FLAT, 60), SeedSpawnInfo(SpawnType.BURIED_FLAT, 61), SeedSpawnInfo(SpawnType.BURIED_FLAT, 62), SeedSpawnInfo(SpawnType.BURIED_FLAT, 63), SeedSpawnInfo(SpawnType.BURIED_FLAT, 64))),
            SpawnOption("Weird", "Weird", "Any spawn that isn't determined to fit into the above categories is counted as \"weird\". <br>These spawns may include large overhangs, partially but not fully buried spawns, etc.", listOf(SeedSpawnInfo(SpawnType.WEIRD)))
        )
    }

    override fun getCheckboxListElements(): List<CheckboxListElement<SpawnOption>> {
        listElements.clear()
        for (spawn in spawnsToList) {
            val element = SpawnListElement(spawn, this)
            listElements.add(element)
        }
        return listElements
    }

    override fun init() {
        super.init()

        // Set up initial state based on settings
        useNaturalFrequenciesCheckbox.isSelected = SettingsManager.settings.simpleNaturalFrequencies
        flatBuriedsCheckbox.isSelected = SettingsManager.settings.simpleFlatBurieds

        val flatBuriedsPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        flatBuriedsPanel.add(flatBuriedsCheckbox)
        flatBuriedsCheckbox.addActionListener {
            updatePercentages()
            MainWindow.optionChanged()
            SettingsManager.settings.simpleFlatBurieds = flatBuriedsCheckbox.isSelected
        }
        val infoLabel = InfoIcon("<html><p>If checked, this will only include seeds where your pearl clip should be 100% consistent because<br> the O-level is the same for the block in front of and behind where you mine up.</p><br><p>If not checked, these non-flat spawns where pearl clips might occasionally fail, will still be included.</p></html>")
        flatBuriedsPanel.add(infoLabel)
        flatBuriedsPanel.alignmentX = Component.LEFT_ALIGNMENT

        this.add(flatBuriedsPanel)


        val naturalDistributionPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        naturalDistributionPanel.add(useNaturalFrequenciesCheckbox)
        useNaturalFrequenciesCheckbox.addActionListener {
            listElements.forEach { it.updatePercentageVisibility() }
            SettingsManager.settings.simpleNaturalFrequencies = useNaturalFrequenciesCheckbox.isSelected
        }
        val infoLabel2 = InfoIcon("<html><p>If checked, selected spawn types will be given to you with the frequencies shown, which are <br>based on how common these spawn types are in random seeds (recommended to use this option).</p><br><p>If unchecked, you'll be equally likely to be given each of the spawn types you've ticked <br>(recommended if you're actively trying to practice the different pearl clips, for example).</p></html>")
        naturalDistributionPanel.add(infoLabel2)
        naturalDistributionPanel.alignmentX = Component.LEFT_ALIGNMENT

        this.add(naturalDistributionPanel)

        updatePercentages()
    }

    fun updatePercentages() {
        var total = 0
        for (listElement in listElements) {
            if (listElement.checkBox.isSelected) {
                total += listElement.getNumSeeds()
            }
        }
        for (listElement in listElements) {
            listElement.updatePercentage(total)
        }
    }

    override fun setAll(selected: Boolean) {
        super.setAll(selected)
        updatePercentages()
        listElements.forEach { it.updatePercentageVisibility() }
    }

    fun getAllSelectedSpawns() : List<SeedSpawnInfo> {
        val list = mutableListOf<SeedSpawnInfo>()

        for (listElement in listElements) {
            if (listElement.checkBox.isSelected) {
                list.addAll(listElement.option.getSpawns(flatBuriedsCheckbox.isSelected))
            }
        }

        return list
    }

    fun getAllSpawnOptions(): List<SpawnOption> {
        return listElements.filter { it.checkBox.isSelected }.map { it.option }
    }
}