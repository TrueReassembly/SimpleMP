package me.chipdev.simplemp.handlers

import me.chipdev.simplemp.SimpleMP
import me.chipdev.simplemp.utils.Embedded
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.concurrent.CompletableFuture

object DatabaseHandler {
    private lateinit var connection: Connection
    fun initializeDatabase() {
        Embedded.async {
            if (!File("${SimpleMP.instance.dataFolder}/simplemp_data.db").exists()) {
                File("${SimpleMP.instance.dataFolder}/simplemp_data.db").createNewFile()
            }
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:plugins/SimpleMP/simplemp_data.db");
            } catch (e: Exception) {
                Bukkit.getLogger().severe("COULD NOT CONNECT TO SQLITE DB\n" + e.javaClass.name + ": " + e.message);
                SimpleMP.instance.onDisable()
            }
            Bukkit.getLogger().info("Opened database successfully");

            val statement = connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS homes
                      (
                         uuid  VARCHAR(36),
                         name  TEXT,
                         world TEXT,
                         x     REAL,
                         y     REAL,
                         z     REAL,
                         pitch REAL,
                         yaw   REAL
                      ); 
            """.trimIndent())

            statement.execute()
        }
    }

    fun getConnection() : Connection {
        return connection;
    }

    fun getHomeByName(name: String, uuid: String): CompletableFuture<Location?> {

        val future = CompletableFuture<Location?>()
        Embedded.async {
            val statement = connection.prepareStatement(
                """
                    SELECT * FROM homes WHERE name=? AND uuid=?
                """.trimIndent()
            )

            statement.setString(1, name)
            statement.setString(2, uuid)

            if (!statement.execute()) future.complete(null);
            else {
                val res = statement.resultSet
                future.complete(Location(
                    Bukkit.getWorld(res.getString("world")),
                    res.getDouble("x"),
                    res.getDouble("y"),
                    res.getDouble("z"),
                    res.getFloat("yaw"),
                    res.getFloat("pitch")
                ))
            }
        }
        return future
    }

    fun isNameAvailable(name: String, uuid: String): Boolean {
        val statement = connection.prepareStatement("""
            SELECT * FROM homes WHERE uuid=?
        """.trimIndent())

        statement.setString(1, uuid)
        try {
            val res = statement.executeQuery()

            while (res.next()) {
                if (res.getString("name") == name) return false
            }
        } catch (_: SQLException) {
            return true
        }
        return true
    }

    // NOTE: The below method will start from 1, if it returns 0, the user has no homes set
    fun getSetHomes(uuid: String): Int {
        val statement = connection.prepareStatement("""
            SELECT * FROM homes WHERE uuid=?
        """.trimIndent())
        statement.setString(1, uuid)
        var i = 0
        try {
            val res = statement.executeQuery()
            while (res.next()) {
                i++
            }
        } catch (_: SQLException) {
            return 0
        }
        return i
    }

    fun addHome(executor: Player, name: String) {
        Embedded.async {
            val statement = connection.prepareStatement("""
            INSERT INTO homes VALUES
            (
                        '${executor.uniqueId}',
                        ?,
                        '${executor.location.world!!.name}',
                        ${executor.location.x},
                        ${executor.location.y},
                        ${executor.location.z},
                        ${executor.location.pitch},
                        ${executor.location.yaw}
            )
        """.trimIndent())
            statement.setString(1, name)
            statement.execute()
        }
    }

    fun deleteHome(executor: Player, name: String) {
        Embedded.async {
            val statement = connection.prepareStatement("""
                DELETE FROM homes WHERE uuid=? AND name=?
            """.trimIndent())

            statement.setString(1, executor.uniqueId.toString())
            statement.setString(2, name)
            statement.execute()
        }

    }

    // Will return null if the user has no homes
    fun getHomeNames(uuid: String, callback: (ArrayList<String>) -> Unit) {
        var res: ResultSet
        var names = ArrayList<String>()
        Embedded.async {
            val statement = connection.prepareStatement(
                """
                SELECT * FROM homes WHERE uuid=?
                """.trimIndent()
            )
            statement.setString(1, uuid)
            try {
                res = statement.executeQuery()
                while (res.next()) {
                    names.add(res.getString("name"))
                }
                callback(names)
            } catch (_: SQLException) {
                callback(ArrayList())
            }
        }
    }
}