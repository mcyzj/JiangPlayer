package com.mcyzj.jiangplayer.database


import java.util.*

interface DatabaseApi {
    fun getPlayerName(uuid: UUID): String?
    fun getPlayerUUID(name: String): UUID?
    fun setPlayerUUID(name: String, uuid: UUID)
    fun setPlayerName(uuid: UUID, name: String)
}