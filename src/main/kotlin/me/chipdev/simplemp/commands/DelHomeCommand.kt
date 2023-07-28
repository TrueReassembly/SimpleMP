@file:Command("delhome")
package me.chipdev.simplemp.commands

import me.chipdev.simplemp.handlers.DatabaseHandler
import me.chipdev.simplemp.utils.Embedded.sendError
import me.chipdev.simplemp.utils.Embedded.sendSuccess
import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player

fun delhome(executor: Player, name: String) {
    if (DatabaseHandler.isNameAvailable(name, executor.uniqueId.toString())) {
        executor.sendError("You do not have a home named $name")
        return
    }

    DatabaseHandler.deleteHome(executor, name)
    executor.sendSuccess("Deleted home $name successfully")
}