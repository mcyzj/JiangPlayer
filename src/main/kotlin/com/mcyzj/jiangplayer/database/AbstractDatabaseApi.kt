package com.mcyzj.jiangplayer.database

import com.j256.ormlite.dao.Dao
import com.xbaimiao.easylib.module.database.Ormlite
import java.util.*


abstract class AbstractDatabaseApi(ormlite: Ormlite) : DatabaseApi {
    private val jiangPlayerTable: Dao<JiangPlayerDao, Int> = ormlite.createDao(JiangPlayerDao::class.java)

    override fun getPlayerName(uuid: UUID): String? {
        val queryBuilder = jiangPlayerTable.queryBuilder()
        queryBuilder.where().eq("uuid", uuid)
        val jiangPlayerDao = queryBuilder.queryForFirst() ?: return null
        return jiangPlayerDao.name
    }

    override fun getPlayerUUID(name: String): UUID? {
        val queryBuilder = jiangPlayerTable.queryBuilder()
        queryBuilder.where().eq("name", name)
        val jiangPlayerDao = queryBuilder.queryForFirst() ?: return null
        return jiangPlayerDao.uuid
    }

    override fun setPlayerUUID(name: String, uuid: UUID) {
        val queryBuilder = jiangPlayerTable.queryBuilder()
        queryBuilder.where().eq("name", name)
        var jiangPlayerDao = queryBuilder.queryForFirst()
        if (jiangPlayerDao == null) {
            jiangPlayerDao = JiangPlayerDao()
            jiangPlayerDao.uuid = uuid
            jiangPlayerDao.name = name
            jiangPlayerTable.create(jiangPlayerDao)
        } else {
            jiangPlayerDao.uuid = uuid
            jiangPlayerTable.update(jiangPlayerDao)
        }
    }

    override fun setPlayerName(uuid: UUID, name: String) {
        val queryBuilder = jiangPlayerTable.queryBuilder()
        queryBuilder.where().eq("uuid", uuid)
        var jiangPlayerDao = queryBuilder.queryForFirst()
        if (jiangPlayerDao == null) {
            jiangPlayerDao = JiangPlayerDao()
            jiangPlayerDao.uuid = uuid
            jiangPlayerDao.name = name
            jiangPlayerTable.create(jiangPlayerDao)
        } else {
            jiangPlayerDao.name = name
            jiangPlayerTable.update(jiangPlayerDao)
        }
    }
}