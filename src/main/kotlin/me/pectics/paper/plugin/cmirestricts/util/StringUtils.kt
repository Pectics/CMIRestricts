package me.pectics.paper.plugin.cmirestricts.util

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

private val MINI_MESSAGE = MiniMessage.miniMessage()

fun String?.takeUnlessEmpty(): String? = this.takeIf { it != null && it.isNotBlank() }

fun Player.colorMessage(message: String) = this.sendMessage(MINI_MESSAGE.deserialize(PlaceholderAPI.setPlaceholders(this, message)))