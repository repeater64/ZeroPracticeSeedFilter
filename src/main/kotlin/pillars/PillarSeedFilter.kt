package pillars

import java.util.*

object PillarSeedFilter {
    fun getPillarInfo(seed: Long): SeedPillarInfo {
        val pillarSeed = Random(seed).nextLong() and 65535L

        val pillarIndices = (0..9).toMutableList()
        Collections.shuffle(pillarIndices, Random(pillarSeed))
        val frontStraightPillar = Pillar.entries[pillarIndices[0]]
        val backStraightPillar = Pillar.entries[pillarIndices[5]]
        val frontDragon = frontStraightPillar.topHeight > backStraightPillar.topHeight
        return SeedPillarInfo(frontDragon, if (frontDragon) Pillar.entries[pillarIndices[9]] else Pillar.entries[pillarIndices[4]])
    }
}