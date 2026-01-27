import pillars.SeedPillarInfo
import spawn.SeedSpawnInfo

data class SeedInfo(val pillarInfo: SeedPillarInfo, val spawnInfo: SeedSpawnInfo) {
    override fun toString(): String {
        return "$pillarInfo!$spawnInfo"
    }

    companion object {
        fun fromString(str: String) : SeedInfo {
            val split = str.split("!")
            return SeedInfo(SeedPillarInfo.fromString(split[0]), SeedSpawnInfo.fromString(split[1]))
        }
    }
}
