import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.InputStreamReader

object LoadedSeedsData {
    private var seedsMap: SeedsMap? = null
    var recentlyUsedSeeds: MutableSet<Long> = hashSetOf() // Non-persistent, because nbd if someone gets the same seed from a previous session, they probably don't remember it. This just prevents getting repeat seeds in quick succession.

    fun load() {
        seedsMap = loadSeedsFromJar()
    }

    fun getData() : SeedsMap {
        if (seedsMap == null) load()

        return seedsMap!!
    }

    fun loadSeedsFromJar(): SeedsMap {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val resourceName = "seedsMap.json"

        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(resourceName)

        if (inputStream == null) {
            println("Resource $resourceName not found in the JAR.")
            return SeedsMap()
        }

        return try {
            InputStreamReader(inputStream).use { reader ->
                gson.fromJson(reader, SeedsMap::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            SeedsMap()
        }
    }
}