package me.chipdev.simplemp

import me.chipdev.simplemp.handlers.DatabaseHandler
import me.chipdev.simplemp.listeners.PlayerListeners
import me.honkling.commando.CommandManager
import org.bukkit.plugin.java.JavaPlugin

class SimpleMP : JavaPlugin() {


    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        val commandManager = CommandManager(this)
        commandManager.registerCommands("me.chipdev.simplemp.commands")
        DatabaseHandler.initializeDatabase()
        server.pluginManager.registerEvents(PlayerListeners, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        lateinit var instance: SimpleMP
    }


}
