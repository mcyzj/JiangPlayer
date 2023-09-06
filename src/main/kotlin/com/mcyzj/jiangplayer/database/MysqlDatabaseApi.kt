package com.mcyzj.jiangplayer.database

import com.mcyzj.jiangplayer.Main
import com.xbaimiao.easylib.module.database.OrmliteMysql

class MysqlDatabaseApi : AbstractDatabaseApi(OrmliteMysql(
    Main.instance.config.getConfigurationSection("Mysql")!!,
    Main.instance.config.getBoolean("Mysql.HikariCP")
)
)