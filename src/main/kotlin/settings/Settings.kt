package settings

import pillars.Pillar
import spawn.SpawnOption

data class Settings(
    var simpleModeOpen: Boolean = true,
    var autoModeEnabled: Boolean = false,

    // Simple mode settings
    var simpleFrontDragon: Boolean = true,
    var simpleBackDragon: Boolean = true,
    var simplePillars: MutableList<Pillar> = mutableListOf(),
    var simpleSpawns: MutableList<SpawnOption> = mutableListOf(),
    var simpleFlatBurieds: Boolean = false,
    var simpleNaturalFrequencies: Boolean = true,

    // Advanced mode settings
    var advancedFlatBurieds: Boolean = false,
    var advancedEnabledGridCoords: MutableSet<Pair<Int, Int>> = hashSetOf()
)
