@file:Command("homes")

package me.chipdev.simplemp.commands

import me.chipdev.simplemp.handlers.DatabaseHandler
import me.chipdev.simplemp.utils.Embedded.sendError
import me.chipdev.simplemp.utils.Embedded.sendSuccess
import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player

fun homes(executor: Player) {
    DatabaseHandler.getHomeNames(executor.uniqueId.toString()) {
        if (it.isEmpty()) {
            executor.sendError("You have no got any homes set, use /sethome <name> to set a home")
            return@getHomeNames
        }

        executor.sendSuccess("Your set homes are: ${it.joinToString(",")}")
        return@getHomeNames
    }
}
