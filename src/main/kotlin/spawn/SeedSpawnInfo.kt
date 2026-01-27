package spawn

data class SeedSpawnInfo(
    val type: SpawnType,
    val oLevel: Int = -1,
) {
    override fun toString(): String {
        if (oLevel == -1) {
            return type.name
        } else {
            return "${type.name} $oLevel"
        }
    }

    companion object {
        fun fromString(str: String) : SeedSpawnInfo {
            if (str.contains(" ")) {
                val split = str.split(" ")
                val type: SpawnType = SpawnType.valueOf(split[0])
                val oLevel = split[1].toInt()

                return SeedSpawnInfo(type, oLevel)
            } else {
                return SeedSpawnInfo(SpawnType.valueOf(str))
            }
        }
    }
}
