package com.mcyzj.jiangplayer.player

import com.mcyzj.jiangplayer.Main
import com.mcyzj.jiangplayer.redis.RedisManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Thread.sleep
import java.util.UUID

object PlayerApi {
    private val tpMap = mutableMapOf<Any, Any>()
    fun updatePlayer(){
        Bukkit.getConsoleSender().sendMessage("§aJiangPlayer 更新玩家列表")
        val onlinePlayerList = Bukkit.getOnlinePlayers()
        for(player in onlinePlayerList){
            val name = player.name
            val uuid = player.uniqueId
            RedisManager.setPlayerServer(name, uuid)
        }
    }
    fun checkPlayer(value: Any){
        Bukkit.getConsoleSender().sendMessage("§aJiangPlayer 修正玩家列表")
        try {
            val uuid = UUID.fromString(value.toString())
            val player = Bukkit.getPlayer(uuid)
            if(player == null){
                val name = Main.databaseApi.getPlayerName(uuid)!!
                RedisManager.removeOtherServerPlayer(name, uuid)
            }
        }catch (_:Exception){
            val name = value.toString()
            val player = Bukkit.getPlayer(name)
            if(player == null){
                val uuid = Main.databaseApi.getPlayerUUID(name)!!
                RedisManager.removeOtherServerPlayer(name, uuid)
            }
        }
    }
    fun findPlayer(value: String): String?{
        var server: String?
        server = try{
            val uuid = UUID.fromString(value)
            RedisManager.getPlayerServerByUUID(uuid)
        }catch (_:Exception){
            RedisManager.getPlayerServerByName(value)
        } ?: return null
        RedisManager.isPlayerInServer(value, server)
        sleep(500)
        server = try{
            val uuid = UUID.fromString(value)
            RedisManager.getPlayerServerByUUID(uuid)
        }catch (_:Exception){
            RedisManager.getPlayerServerByName(value)
        }
        if (server != null){
            return server
        }
        RedisManager.updatePlayer()
        sleep(500)
        return try{
            val uuid = UUID.fromString(value)
            RedisManager.getPlayerServerByUUID(uuid)
        }catch (_:Exception){
            RedisManager.getPlayerServerByName(value)
        }
    }
    fun sendMessage(value: Any, msg: String, sender: String){
        val player = try {
            val uuid = UUID.fromString(value.toString())
            Bukkit.getPlayer(uuid)
        }catch (_:Exception){
            val name = value.toString()
            Bukkit.getPlayer(name)
        } ?: return
        Bukkit.getConsoleSender().sendMessage("§aJiangPlayer $sender -> $msg")
        player.sendMessage("$sender -> $msg")
    }
    fun setTp(valueFrom: Any, valueTo: Any){
        tpMap[valueFrom] = valueTo
    }
    fun getTp(valueFrom: Any): Any? {
        return tpMap[valueFrom]
    }
    fun tpPlayer(valueFrom: Any, valueTo: Any){
        val playerFrom = try {
            val uuid = UUID.fromString(valueFrom.toString())
            Bukkit.getPlayer(uuid)
        }catch (_:Exception){
            val name = valueFrom.toString()
            Bukkit.getPlayer(name)
        } ?: return
        tpMap.remove(valueFrom)
        val playerTo = try {
            val uuid = UUID.fromString(valueTo.toString())
            Bukkit.getPlayer(uuid)
        }catch (_:Exception){
            val name = valueTo.toString()
            Bukkit.getPlayer(name)
        } ?: return
        playerFrom.teleport(playerTo.location)
    }
    fun connect(player: Player, server: String) {
        val byteArray = ByteArrayOutputStream()
        val out = DataOutputStream(byteArray)
        try {
            out.writeUTF("Connect")
            out.writeUTF(server)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        player.sendPluginMessage(Main.instance, "BungeeCord", byteArray.toByteArray())
    }
}