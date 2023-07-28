@file:Command("sethome")

package me.chipdev.simplemp.commands

import me.chipdev.simplemp.SimpleMP
import me.chipdev.simplemp.handlers.DatabaseHandler
import me.chipdev.simplemp.utils.Embedded.sendError
import me.chipdev.simplemp.utils.Embedded.sendSuccess
import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player

fun sethome(executor: Player, name: String) {

    val maxHomes = SimpleMP.instance.config.getInt("max-homes")

    if (name.length > 20 ) {
        executor.sendError("That name is too long")
        return
    }

    if (DatabaseHandler.getSetHomes(executor.uniqueId.toString()) >= maxHomes) {
        executor.sendError("You have reached the maximum of $maxHomes homes")
        return
    }

    if (!DatabaseHandler.isNameAvailable(name, executor.uniqueId.toString())) {
        executor.sendError("You already have a home named $name")
        return
    }

    DatabaseHandler.addHome(executor, name)
    executor.sendSuccess("Home created")
}