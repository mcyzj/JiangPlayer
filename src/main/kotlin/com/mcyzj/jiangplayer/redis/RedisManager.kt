package com.mcyzj.jiangplayer.redis

import com.mcyzj.jiangplayer.Main
import com.xbaimiao.easylib.EasyPlugin
import com.xbaimiao.easylib.module.utils.Module
import java.util.*


object RedisManager : Module<EasyPlugin> {

    private val jedisPool = Main.jedisPool
    private val serverName = Main.Config.getString("ServerName")

    fun setPlayerServer(name: String, uuid: UUID) {
        jedisPool.resource.also {
            it.set("JiangPlayer_${name}", serverName)
            it.set("JiangPlayer_${uuid}", serverName)
            it.close()
        }
    }

    fun getPlayerServerByUUID(uuid: UUID): String? {
        jedisPool.resource.also {
            val value = it.get("JiangPlayer_${uuid}")
            it.close()
            if (value == null) {
                return null
            }
            return value
        }
    }

    fun getPlayerServerByName(name: String): String? {
        jedisPool.resource.also {
            val value = it.get("JiangPlayer_${name}")
            it.close()
            if (value == null) {
                return null
            }
            return value
        }
    }

    fun removePlayerServer(name: String, uuid: UUID) {
        jedisPool.resource.also {
            it.del("JiangPlayer_${name}")
            it.del("JiangPlayer_${uuid}")
            it.close()
        }
    }

    fun removeOtherServerPlayer(name: String, uuid: UUID) {
        jedisPool.resource.also {
            val server = it.get("JiangPlayer_${uuid}")
            if(server == Main.Config.getString("ServerName")) {
                it.del("JiangPlayer_${name}")
                it.del("JiangPlayer_${uuid}")
            }
            it.close()
        }
    }

    fun isPlayerInServer(value: Any, server: String) {
        push("checkPlayer|,|${server}|,|${value}")
    }

    fun updatePlayer() {
        push("updatePlayer")
    }

    fun sandMsgToPlayer(value: Any, server: String, msg: String, sender: String) {
        push("message|,|${server}|,|${sender}|,|${value}|,|${msg}")
    }

    fun teleportToPlayer(valueFrom: Any, valueTo: Any, server: String) {
        push("teleport|,|${server}|,|${valueFrom}|,|${valueTo}")
    }

    private fun push(message: String) {
        jedisPool.resource.use { jedis -> jedis.publish("JiangPlayer", message) }
    }
}