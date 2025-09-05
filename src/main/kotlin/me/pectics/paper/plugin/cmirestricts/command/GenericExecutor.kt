package me.pectics.paper.plugin.cmirestricts.command

import me.pectics.paper.plugin.cmirestricts.CMIRestricts
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class GenericExecutor(private val plugin: CMIRestricts) : TabExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name != "cmirestricts") return true
        if (args.isEmpty()) return true
        when (args[0].lowercase()) {
            "reload" -> {
                plugin.reloadConfig()
                plugin.initialize()
                sender.sendMessage("已重载配置。")
                return true
            }
            else -> {
                sender.sendMessage("未知子命令: ${args[0]}")
                return false
            }
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        if (command.name != "cmirestricts") return emptyList()
        return when (args.size) {
            1 -> listOf("reload").filter { it.startsWith(args[0], true) }
            else -> emptyList()
        }
    }

}