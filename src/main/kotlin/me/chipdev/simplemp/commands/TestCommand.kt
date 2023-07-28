@file:Command("test")

package me.chipdev.simplemp.commands

import me.chipdev.simplemp.handlers.DatabaseHandler
import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player

fun test(executor: Player) {

}
fun dump(executor: Player, neverUsed: String, name: String) {
    DatabaseHandler.addHome(executor, name)
}

fun get(executor: Player, neverUsed: String, name: String) {

}