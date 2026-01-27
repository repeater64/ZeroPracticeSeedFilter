// Some of the code in this file was taken from/inspired by NinjabrainBot - https://github.com/Ninjabrain1/Ninjabrain-Bot - thanks to Ninjabrain for this!

package gui.mcinstance

import com.sun.jna.platform.DesktopWindow
import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.ptr.IntByReference
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object WindowsActiveInstanceListener {

    fun tryGetDotMinecraftDirectory(): String? {
        val windows: List<DesktopWindow> = WindowUtils.getAllWindows(true)

        for (window in windows) {
            if (window.title.startsWith("Minecraft")) {
                if (window.filePath?.contains("javaw.exe") == true) {
                    val processId = getWindowProcessId(window.hwnd)
                    val directory = getDotMinecraftDirectoryFromProcessId(processId)
                    if (directory != null) {
                        return directory
                    }
                }
            }
        }
        return null
    }

    private fun getWindowProcessId(windowHandle: HWND): Int {
        val pid = IntByReference()
        User32.INSTANCE.GetWindowThreadProcessId(windowHandle, pid)
        return pid.getValue()
    }

    private fun getDotMinecraftDirectoryFromProcessId(pid: Int): String? {
        val runtime = Runtime.getRuntime()
        val commands = arrayOf("jcmd", "" + pid, "VM.command_line")
        try {
            val process = runtime.exec(commands)
            val commandOutputReader = BufferedReader(InputStreamReader(process.inputStream))
            var vmArgument: String?
            while (commandOutputReader.readLine().also { vmArgument = it } != null) {
                if (vmArgument!!.startsWith("jvm_args")) {
                    for (jvmArgument in vmArgument!!.split(" -".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()) {
                        if (jvmArgument.startsWith("Djava.library.path")) {
                            process.destroy()
                            commandOutputReader.close()
                            return getDotMinecraftDirectory(jvmArgument.trim { it <= ' ' })
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getDotMinecraftDirectory(nativesJvmArgument: String): String? {
        val dotMinecraftIndex = nativesJvmArgument.lastIndexOf(".minecraft")
        if (dotMinecraftIndex != -1) return nativesJvmArgument.substring(19, dotMinecraftIndex + 10)
        return if (nativesJvmArgument.endsWith("natives")) nativesJvmArgument.substring(19)
            .replace("natives", ".minecraft") else null
    }
}

