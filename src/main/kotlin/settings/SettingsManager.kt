package settings

import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object SettingsManager {
    private const val APP_FOLDER_NAME = "EndPracticeFilter"
    private const val FILE_NAME = "savedData.json"

    private val gson = GsonBuilder().setPrettyPrinting().create()

    var settings: Settings = loadSettings()
        private set

    /**
     * Windows: %APPDATA%/AppName
     * Mac: ~/Library/Application Support/AppName
     * Linux: ~/.AppName
     */
    private fun getConfigFile(): File {
        val userHome = System.getProperty("user.home")
        val os = System.getProperty("os.name").lowercase()

        val appDir = when {
            os.contains("win") -> {
                val appData = System.getenv("APPDATA")
                if (appData != null) File(appData, APP_FOLDER_NAME)
                else File(userHome, APP_FOLDER_NAME) // Fallback
            }
            os.contains("mac") -> {
                File(userHome, "Library/Application Support/$APP_FOLDER_NAME")
            }
            else -> {
                File(userHome, ".$APP_FOLDER_NAME")
            }
        }

        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        return File(appDir, FILE_NAME)
    }

    fun save() {
        try {
            FileWriter(getConfigFile()).use { writer ->
                gson.toJson(settings, writer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadSettings(): Settings {
        val file = getConfigFile()
        if (!file.exists()) return Settings()

        return try {
            FileReader(file).use { reader ->
                gson.fromJson(reader, Settings::class.java) ?: Settings()
            }
        } catch (e: Exception) {
            println("Failed to load settings, reverting to defaults.")
            e.printStackTrace()
            Settings()
        }
    }

    fun reload() {
        settings = loadSettings()
    }
}