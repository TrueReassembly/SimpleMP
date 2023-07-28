@file:Command("home")
package me.chipdev.simplemp.commands

import me.chipdev.simplemp.SimpleMP
import me.chipdev.simplemp.handlers.DatabaseHandler
import me.chipdev.simplemp.utils.Embedded.sendError
import me.honkling.commando.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.function.Consumer

fun home(executor: Player, name: String) {
    DatabaseHandler.getHomeByName(name, executor.uniqueId.toString()).thenAccept(Consumer {
        if (it == null) {
            executor.sendError("You have not set a home named $name!")
            return@Consumer
        }
        Bukkit.getScheduler().runTask(SimpleMP.instance, Runnable {
            executor.teleport(it)
        })
    })
    return
}