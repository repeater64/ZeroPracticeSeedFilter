import gui.MainWindow
import settings.SettingsManager
import javax.swing.SwingUtilities

fun main() {
    LoadedSeedsData.load()

    SwingUtilities.invokeLater {
        MainWindow.isVisible = true
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        SettingsManager.save()
    })
}