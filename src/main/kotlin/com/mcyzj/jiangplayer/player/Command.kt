package com.mcyzj.jiangplayer.player

import com.xbaimiao.easylib.module.command.command
import org.bukkit.command.CommandSender
import com.mcyzj.jiangplayer.Main
import com.mcyzj.jiangplayer.redis.RedisManager
import org.bukkit.entity.Player

class Command {

    private val findplayer = command<CommandSender>("findplayer") {
        permission = "jiangplayer.command.findplayer"
        exec{
            val value = PlayerApi.findPlayer(args[0])
            if (value != null){
                sender.sendMessage(value)
            }else{
                sender.sendMessage("未找到玩家")
            }
        }
    }
    private val send = command<CommandSender>("send") {
        permission = "jiangplayer.command.send"
        exec{
            val value = PlayerApi.findPlayer(args[0])
            if (value == null) {
                sender.sendMessage("未找到玩家")
                return@exec
            }
            val name = sender.name
            if (args[1].isEmpty()){
                sender.sendMessage("消息不能为空")
                return@exec
            }
            RedisManager.sandMsgToPlayer(args[0], value, args[1], name)
            sender.sendMessage("$name -> $args[1]")
        }
    }
    private val tp = command<CommandSender>("tp") {
        permission = "jiangplayer.command.tp"
        exec{
            if (sender is Player) {
                val value = PlayerApi.findPlayer(args[0])
                if (value == null) {
                    sender.sendMessage("未找到玩家")
                    return@exec
                }
                RedisManager.teleportToPlayer(sender.name, args[0], value)
                PlayerApi.connect(sender as Player, value)
            }
        }
    }

    private val mainCommand = Main.instance.config.getString("command.main") ?:"jiangplayer"
    val commandRoot = command<CommandSender>(mainCommand) {
        permission = "jiangplayer.command"
        sub(findplayer)
        sub(send)
        sub(tp)
    }
}