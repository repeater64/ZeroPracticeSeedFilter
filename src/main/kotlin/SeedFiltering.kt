import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pillars.PillarSeedFilter
import spawn.SpawnSeedFilter
import java.io.*

fun main() {
//    VisualisationTest.main()


//    printSeedInfo(22386)
    val seedsMap = loadSeedsFromWorkingDirectory()

    filterAndSaveSeeds(50000+1, 1000000, seedsMap)

//    printSeeds(SeedInfo(SeedPillarInfo(true, Pillar.SMALL_BOY), SeedSpawnInfo(SpawnType.OVERHANG)), seedsMap)
}

fun loadSeedsFromWorkingDirectory(): SeedsMap {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    val fileName = "seedsMap.json"
    val file = File(fileName)

    // Load or create SeedsMap
    return if (file.exists()) {
        try {
            FileReader(file).use { reader ->
                gson.fromJson(reader, SeedsMap::class.java)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            SeedsMap() // fallback in case of error reading the file
        }
    } else {
        SeedsMap() // create new instance if file doesn't exist
    }

}

fun filterAndSaveSeeds(start: Int, end: Int, seedsMap: SeedsMap) {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    val fileName = "seedsMap.json"
    val file = File(fileName)

    filterSeeds(start, end, seedsMap)


    // Save SeedsMap to JSON
    try {
        FileWriter(file).use { writer ->
            gson.toJson(seedsMap, writer)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun printSeeds(seedInfo: SeedInfo, seedsMap: SeedsMap) {
    seedsMap.map[seedInfo]?.let {
        println(it)
    } ?: run {
        println("No matching seeds found!")
    }
}

fun printSeedInfo(seed: Long) {
    println(PillarSeedFilter.getPillarInfo(seed))
    println(SpawnSeedFilter.getSpawnInfo(seed))
}

fun filterSeeds(start: Int, end: Int, seedsMap: SeedsMap) {
    for (seedInt in start .. end) {
        val seed = seedInt.toLong()

        val pillarInfo = PillarSeedFilter.getPillarInfo(seed)
        val spawnsInfo = SpawnSeedFilter.getSpawnInfo(seed)
        val seedInfo = SeedInfo(pillarInfo, spawnsInfo)

        seedsMap.addSeed(seedInfo, seed)
    }
}

