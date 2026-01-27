package gui.simple

import LoadedSeedsData
import SeedInfo
import gui.MainWindow
import gui.general.SeedOptionsSelectorPanel
import gui.simple.pillars.PillarSelectionPanel
import gui.simple.spawns.SpawnSelectionPanel
import pillars.SeedPillarInfo
import spawn.SeedSpawnInfo
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class SimpleSelectorPanel : JPanel(BorderLayout(10, 10)), SeedOptionsSelectorPanel {

    val pillarPanel = PillarSelectionPanel()
    val spawnPanel = SpawnSelectionPanel()
    val frontBackPanel = FrontBackPanel()

    fun init() {
        border = EmptyBorder(10, 10, 10, 10)

        pillarPanel.init()
        spawnPanel.init()
        frontBackPanel.init()

        val switchButton = JButton("Switch to Advanced Selector")
        switchButton.addActionListener { MainWindow.switchSelector("advanced") }

        val mainPanel = JPanel(GridLayout(1, 2, 10, 0))
        mainPanel.add(pillarPanel)
        mainPanel.add(spawnPanel)

        add(switchButton, BorderLayout.NORTH)
        add(mainPanel, BorderLayout.CENTER)
        add(frontBackPanel, BorderLayout.SOUTH)
    }

    override fun getNumApplicableSeeds(): Int {
        return findAllValidSeeds(gatherAllPillarInfos(), gatherAllSpawnInfos()).size
    }

    override fun getNextSeed(): Long {
        val pillars = gatherAllPillarInfos()
        if (pillars.isEmpty()) return -1

        // Different behaviour if we're using natural frequencies for spawns or not
        val spawns = if (spawnPanel.useNaturalFrequenciesCheckbox.isSelected) {
            // Natural frequencies
            gatherAllSpawnInfos()
        } else {
            // Non-natural frequencies. Pick a spawn option first then only add seeds with that spawn to the list to be picked from.
            val pickedOption = spawnPanel.getAllSpawnOptions().random()

            pickedOption.getSpawns(spawnPanel.flatBuriedsCheckbox.isSelected)
        }

        var seeds = findAllValidSeeds(pillars, spawns)

        if (seeds.isEmpty()) {
            return -2
        }

        // Avoid repeating recently used seeds
        seeds.removeAll(LoadedSeedsData.recentlyUsedSeeds)
        if (seeds.isEmpty()) {
            // Oops, run out of seeds, we can reset recently used ones.
            LoadedSeedsData.recentlyUsedSeeds.clear()
            seeds = findAllValidSeeds(pillars, spawns)
        }


        return seeds.random()
    }

    override fun getInvalidSelectionErrorPopup(): String {
        return "You must select at least one of Front/Back dragon, at least one pillar and at least one spawn!"
    }

    override fun getInvalidSelectionWarning(): String {
        return "Please select at least one option from each category!"
    }

    private fun findAllValidSeeds(
        pillars: MutableList<SeedPillarInfo>,
        spawns: List<SeedSpawnInfo>
    ): MutableSet<Long> {
        val seeds = hashSetOf<Long>()

        for (pillar in pillars) {
            for (spawn in spawns) {
                val seedInfo = SeedInfo(pillar, spawn)
                LoadedSeedsData.getData().map[seedInfo]?.let { seeds.addAll(it) }
            }
        }
        return seeds
    }

    private fun gatherAllSpawnInfos() = spawnPanel.getAllSelectedSpawns()

    private fun gatherAllPillarInfos(): MutableList<SeedPillarInfo> {
        val pillars = mutableListOf<SeedPillarInfo>()
        if (frontBackPanel.frontDragonCheckBox.isSelected) {
            pillars.addAll(pillarPanel.getAllSelectedPillars(true))
        }
        if (frontBackPanel.backDragonCheckBox.isSelected) {
            pillars.addAll(pillarPanel.getAllSelectedPillars(false))
        }
        return pillars
    }
}