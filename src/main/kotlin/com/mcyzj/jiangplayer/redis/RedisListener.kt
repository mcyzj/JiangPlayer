@file:Suppress("SameParameterValue")

package com.mcyzj.jiangplayer.redis

import com.mcyzj.jiangplayer.Main
import com.mcyzj.jiangplayer.player.PlayerApi
import org.bukkit.Bukkit
import redis.clients.jedis.JedisPubSub

class RedisListener : JedisPubSub() {

    override fun onMessage(channel: String?, message: String?) {
        if (channel == "JiangPlayer") {
            when(message!!.split("|,|")[0]){
                "updatePlayer" ->{
                    PlayerApi.updatePlayer()
                }
                "checkPlayer" ->{
                    PlayerApi.updatePlayer()
                    val server = message.split("|,|")[1]
                    if(server == Main.Config.getString("ServerName")){
                        PlayerApi.checkPlayer(message.split("|,|")[2])
                    }
                }
                "message" ->{
                    val server = message.split("|,|")[1]
                    if(server == Main.Config.getString("ServerName")){
                        val sender = message.split("|,|")[2]
                        val value = message.split("|,|")[3]
                        val msg = message.split("|,|")[4]
                        PlayerApi.sendMessage(value, msg, sender)
                    }
                }
                "teleport" ->{
                    val server = message.split("|,|")[1]
                    if(server == Main.Config.getString("ServerName")){
                        val valueFrom = message.split("|,|")[2]
                        val valueTo = message.split("|,|")[3]
                        PlayerApi.setTp(valueFrom, valueTo)
                    }
                }
                else ->{
                    Bukkit.getLogger().warning("§aJiangPlayer 未知的redis消息类型")
                }
            }
        }
    }
}