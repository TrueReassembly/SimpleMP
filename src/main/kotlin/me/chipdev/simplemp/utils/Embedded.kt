package me.chipdev.simplemp.utils

import me.chipdev.simplemp.SimpleMP
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

object Embedded {

    fun Player.getConfig() : FileConfiguration {
        val dataFile = File("${SimpleMP.instance.dataFolder}/players", "${this.uniqueId}.yml")
        return YamlConfiguration.loadConfiguration(dataFile)
    }

    fun Player.sendError(message: String) = this.sendMessage("&#ff6e6e⚠ &#ff7f6e$message".coloured())

    fun Player.sendSuccess(message: String) = this.sendMessage("&#c8e1cc✓ &#d2e7d6$message".coloured())

    // Stol- borrowed from ArozeUtils: https://github.com/UwUAroze/ArozeUtils/blob/master/src/main/kotlin/me/aroze/arozeutils/minecraft/generic/ChatUtils.kt
    val hexPattern = Pattern.compile("&(#[a-fA-F\\d]{6})");
    fun String.coloured(): String {
        var coloured = this

        var match: Matcher = hexPattern.matcher(coloured)
        while (match.find()) {
            val color: String = coloured.substring(match.start(), match.end())
            coloured = coloured.replace(color, ChatColor.of(color.substring(1)).toString())
            match = hexPattern.matcher(coloured)
        }
        return ChatColor.translateAlternateColorCodes('&', coloured)
    }

    fun async(callback: () -> Unit) = Bukkit.getScheduler().runTaskAsynchronously(SimpleMP.instance, Runnable(callback))

}