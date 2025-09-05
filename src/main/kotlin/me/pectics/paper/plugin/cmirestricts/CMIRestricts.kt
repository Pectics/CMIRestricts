package me.pectics.paper.plugin.cmirestricts

import me.pectics.paper.plugin.cmirestricts.command.GenericExecutor
import me.pectics.paper.plugin.cmirestricts.machanic.WorldVisitTracker
import me.pectics.paper.plugin.cmirestricts.restrict.TpaWorldRestriction
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin

class CMIRestricts : JavaPlugin(), TabExecutor {

    override fun onEnable() {
        saveDefaultConfig()
        initialize()
        getCommand("cmirestricts")?.setExecutor(GenericExecutor(this))
            ?: throw IllegalStateException("无法注册命令 cmirestricts")
    }

    override fun onDisable() {

    }

    fun initialize() {
        WorldVisitTracker.init(this)
        // 注册限制项
        "tpa_world_restriction".let {
            TpaWorldRestriction(this, config.getConfigurationSection(it) ?: config.createSection(it))
        }
    }

}
