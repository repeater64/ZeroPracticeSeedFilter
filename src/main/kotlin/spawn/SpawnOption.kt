package spawn

data class SpawnOption(
    val displayName: String,
    val shortDisplayName: String,
    val tooltip: String,
    val includedSpawns: List<SeedSpawnInfo>,
    val includedSpawnsWhenFlatForced: List<SeedSpawnInfo>
) {
    fun getSpawns(flatForced: Boolean): List<SeedSpawnInfo> {
        return if (flatForced) includedSpawnsWhenFlatForced else includedSpawns
    }

    constructor(displayName: String, shortDisplayName: String, tooltip: String, includedSpawns: List<SeedSpawnInfo>) : this(displayName, shortDisplayName, tooltip, includedSpawns, includedSpawns)
}
