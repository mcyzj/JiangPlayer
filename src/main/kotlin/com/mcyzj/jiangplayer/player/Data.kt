package com.mcyzj.jiangplayer.player

import org.bukkit.entity.Player
import java.util.UUID

data class PlayerData(
    var name: String,
    var uuid: UUID,
    var online: Boolean,
    var server: String?,
    val player: Player?
)