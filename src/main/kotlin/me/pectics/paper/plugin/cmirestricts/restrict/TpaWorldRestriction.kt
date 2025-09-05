package me.pectics.paper.plugin.cmirestricts.restrict

import com.Zrips.CMI.Modules.Teleportations.TeleportManager.TpAction.*
import com.Zrips.CMI.events.CMIPlayerTeleportRequestEvent
import me.pectics.paper.plugin.cmirestricts.CMIRestricts
import me.pectics.paper.plugin.cmirestricts.machanic.hasVisitedWorld
import me.pectics.paper.plugin.cmirestricts.util.colorMessage
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class TpaWorldRestriction(
    plugin: CMIRestricts,
    config: ConfigurationSection
) : Listener {

    private val message: String = config.getString("message")
        ?.takeUnless { it.isBlank() }
        ?: "<red>您还未到达过该区域！"

    init {
        val enabled = config.getBoolean("enabled", false)
        if (enabled) plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onTeleportRequest(event: CMIPlayerTeleportRequestEvent) {
        // 仅对 tpa 和 tpahere 做处理
        if (event.action !in listOf(tpa, tpahere)) return
        val player = event.player
        // 处于同一世界，不做处理
        val fromWorld = player.world
        val destWorld = event.whoAccepts.world
        if (fromWorld == destWorld) return
        // 进入过该世界，不做处理
        if (player.hasVisitedWorld(destWorld)) return
        // 未进入过该世界
        event.isCancelled = true
        player.colorMessage(message)
    }

}