package com.github.cloudcodec.mybatisplus.intellij.core.druid

import com.mysql.jdbc.MysqlDefs

object MysqlType2JdbcType {

    private val mysqlToJavaTypeMethod = MysqlDefs::class.java.getDeclaredMethod("mysqlToJavaType", String::class.java)

    init {
        mysqlToJavaTypeMethod.isAccessible = true
    }

    fun convert(mysqlType: String): Int {
        val result = mysqlToJavaTypeMethod.invoke(null, mysqlType) as Int
        return result
    }
}
