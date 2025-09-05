package me.pectics.paper.plugin.cmirestricts.machanic

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

fun Player.hasVisitedWorld(world: World) = WorldVisitTracker.hasVisited(this, world.name)
fun Player.recordWorldVisit(world: World) = WorldVisitTracker.recordVisit(this, world.name)
val Player.visitedWorlds: Set<String>
    get() = WorldVisitTracker.getVisited(this)

object WorldVisitTracker {

    private val GSON = Gson()
    private val DATA_TYPE = object : TypeToken<MutableList<String>>() {}.type

    private lateinit var plugin: Plugin

    fun init(plugin: Plugin) {
        this.plugin = plugin
        WorldVisitListener(plugin)
    }

    private val WORLDS_KEY: NamespacedKey by lazy {
        NamespacedKey(plugin, "ever_entered_worlds")
    }

    fun recordVisit(player: Player, worldName: String) {
        val set = loadSet(player.persistentDataContainer)
        if (set.add(worldName))
            saveSet(player.persistentDataContainer, set)
    }

    fun hasVisited(player: Player, worldName: String): Boolean {
        return loadSet(player.persistentDataContainer).contains(worldName)
    }

    fun getVisited(player: Player): Set<String> {
        return loadSet(player.persistentDataContainer)
    }

    private fun loadSet(pdc: PersistentDataContainer): MutableSet<String> {
        val raw = pdc.get(WORLDS_KEY, PersistentDataType.STRING) ?: return mutableSetOf()
        val list = GSON.fromJson<MutableList<String>>(raw, DATA_TYPE)
        return list.toMutableSet()
    }

    private fun saveSet(pdc: PersistentDataContainer, set: Set<String>) {
        val json = GSON.toJson(set.toList())
        pdc.set(WORLDS_KEY, PersistentDataType.STRING, json)
    }

    private class WorldVisitListener(plugin: Plugin) : Listener {

        init {
            plugin.server.pluginManager.registerEvents(this, plugin)
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        fun onTeleport(event: PlayerTeleportEvent) {
            val fromWorld = event.from.world ?: return
            val toWorld = event.to.world ?: return
            // 同世界传送不处理
            if (fromWorld == toWorld || fromWorld.uid == toWorld.uid) return
            event.player.recordWorldVisit(toWorld)
        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onJoin(e: PlayerJoinEvent) {
            val player = e.player
            player.recordWorldVisit(player.world)
        }

    }

}
