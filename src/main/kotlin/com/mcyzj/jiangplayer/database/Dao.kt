package com.mcyzj.jiangplayer.database

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*
//1.2.1删除
@DatabaseTable(tableName = "JiangPlayer_Bind")
class JiangPlayerDao {

    @DatabaseField(generatedId = true)
    var id: Int = 0
    //玩家uuid
    @DatabaseField(dataType = DataType.UUID, canBeNull = false, columnName = "uuid")
    lateinit var uuid: UUID
    //玩家名称
    @DatabaseField(dataType = DataType.LONG_STRING,canBeNull = false, columnName = "name")
    lateinit var name: String
}