import spawn.SeedSpawnInfo

data class SeedsMap(
    private val _map: HashMap<String, MutableSet<Long>> = hashMapOf(),
    private val _spawnCounts: HashMap<String, Int> = hashMapOf(),
    var totalSeeds: Int = 0
) {
    fun addSeed(info: SeedInfo, seed: Long) {
        (_map.computeIfAbsent(info.toString()) { mutableSetOf() }).add(seed)
        _spawnCounts.merge(info.spawnInfo.toString(), 1) { a, b -> a+b}
        totalSeeds++
    }

    @delegate:Transient
    val map: Map<SeedInfo, Set<Long>> by lazy { _map.mapKeys { (str, _) -> SeedInfo.fromString(str) } }

    @delegate:Transient
    val spawnCounts: Map<SeedSpawnInfo, Int> by lazy { _spawnCounts.mapKeys { (str, _) -> SeedSpawnInfo.fromString(str) } }
}