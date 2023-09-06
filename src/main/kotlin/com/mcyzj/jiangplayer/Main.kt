package com.mcyzj.jiangplayer

/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */

import com.mcyzj.jiangplayer.database.DatabaseApi
import com.mcyzj.jiangplayer.database.MysqlDatabaseApi
import com.mcyzj.jiangplayer.player.Command
import com.mcyzj.jiangplayer.player.Listener
import com.xbaimiao.easylib.EasyPlugin
import com.xbaimiao.easylib.module.chat.BuiltInConfiguration
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import redis.clients.jedis.JedisPool
import com.mcyzj.jiangplayer.redis.RedisConfig
import com.mcyzj.jiangplayer.redis.RedisListener
import com.xbaimiao.easylib.module.utils.registerListener
import com.xbaimiao.easylib.module.utils.submit
import com.xbaimiao.easylib.task.EasyLibTask

@Suppress("unused")
class Main : EasyPlugin(){

    companion object {
        lateinit var databaseApi: DatabaseApi
        lateinit var instance: Main
        lateinit var Config: FileConfiguration
        lateinit var jedisPool: JedisPool
        lateinit var subscribeTask: EasyLibTask
        const val channel = "JiangPlayer"
    }

    private var redisListener: RedisListener? = null
    private var config = BuiltInConfiguration("config.yml")

    override fun onLoad() {

    }

    override fun onEnable() {
        Bukkit.getConsoleSender().sendMessage("§aJiangPlayer 加载中....祈祷成功")
        saveDefaultConfig()
        instance = this

        Config = config
        Config.options().copyDefaults(true)
        saveConfig()

        //注册redis监听
        val redisConfig = RedisConfig(config)
        Bukkit.getConsoleSender().sendMessage("§aJiangPlayer §fRedisInfo: " + redisConfig.host + ":" + redisConfig.port)
        jedisPool = if (redisConfig.password != null) {
            JedisPool(redisConfig, redisConfig.host, redisConfig.port, 1000, redisConfig.password)
        } else {
            JedisPool(redisConfig, redisConfig.host, redisConfig.port)
        }
        redisListener = RedisListener()
        subscribeTask = submit(async = true) {
            jedisPool.resource.use { jedis ->
                jedis.subscribe(redisListener, "JiangPlayer")
            }
        }

        //加载数据库
        databaseApi = MysqlDatabaseApi()

        //注册监听事件
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord")
        registerListener(Listener())
        //注册指令
        Command().commandRoot.register()
    }

    override fun onDisable() {

    }
}