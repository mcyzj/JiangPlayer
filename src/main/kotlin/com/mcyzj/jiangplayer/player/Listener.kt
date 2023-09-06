package com.mcyzj.jiangplayer.player

import com.mcyzj.jiangplayer.Main
import com.mcyzj.jiangplayer.redis.RedisManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.lang.Thread.sleep

class Listener : Listener {
    @EventHandler
    fun join(event: PlayerJoinEvent){
        //阻塞线程1秒给上一个服务器反应时间
        sleep(1000)
        //替换玩家服务器
        val player = event.player
        val uuid = player.uniqueId
        val name = player.name
        Main.databaseApi.setPlayerName(uuid, name)
        RedisManager.setPlayerServer(name, uuid)
        var valueFrom: Any
        var valueTo = PlayerApi.getTp(player.name)
        valueFrom = player.name
        if (valueTo == null){
            valueTo = PlayerApi.getTp(player.uniqueId)
            valueFrom = player.uniqueId
        }
        if(valueTo == null){
            return
        }
        PlayerApi.tpPlayer(valueFrom, valueTo)
    }

    @EventHandler
    fun out(event: PlayerQuitEvent){
        //替换玩家服务器
        val player = event.player
        val uuid = player.uniqueId
        val name = player.name
        RedisManager.removePlayerServer(name, uuid)
    }
}