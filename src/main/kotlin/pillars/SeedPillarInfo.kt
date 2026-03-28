package pillars

import kotlinx.serialization.Serializable

@Serializable
data class SeedPillarInfo(
    val frontDragon: Boolean,
    val pillar: Pillar
) {
    override fun toString(): String {
        return "${if (frontDragon) "Front" else "Back"} ${pillar.name}"
    }

    val displayName = "${if (frontDragon) "Front" else "Back"} ${pillar.displayName}"

    companion object {
        fun fromString(str: String) : SeedPillarInfo {
            val frontDragon = str.startsWith("Front")
            val pillar = if (frontDragon) {
                Pillar.valueOf(str.replace("Front ", ""))
            } else {
                Pillar.valueOf(str.replace("Back ", ""))
            }
            return SeedPillarInfo(frontDragon, pillar)
        }
    }
}
