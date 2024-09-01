package com.github.cloudcodec.mybatisplus.intellij.core.druid

import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import java.sql.Types

object TypesConverter {

//    private val availableTypes = setOf(Types.BOOLEAN,Types.TINYINT ,Types.SMALLINT, Types.INTEGER, Types.BIGINT, Types.VARCHAR, Types.TIMESTAMP, Types.)

    fun getJavaType(column: SQLColumnDefinition): FullyQualifiedJavaType {
        try {
            val mysqlType = column.dataType.name

            var jdbcType: Int? = null
            if (jdbcType == null && "TINYINT".equals(mysqlType, ignoreCase = true) && column.dataType.arguments.isNotEmpty()) {
                val width = (column.dataType.arguments[0] as SQLIntegerExpr).number
                if (width == 1) {
                    jdbcType = Types.BOOLEAN

                }
            }
            if (jdbcType == null && ("BOOLEAN".equals(mysqlType, ignoreCase = true) || "BOOL".equals(mysqlType, ignoreCase = true))) {
                jdbcType=Types.BOOLEAN
            }
            if (jdbcType == null && "JSON".equals(mysqlType,ignoreCase = true)) {
                jdbcType=Types.VARCHAR
            }
            if (jdbcType == null) {
                jdbcType = MysqlType2JdbcType.convert(mysqlType)
            }
//            if (!availableTypes.contains(jdbcType)) {
//                throw IllegalArgumentException("unsupported mysqlType:$mysqlType,jdbcType:$jdbcType")
//            }

            val javaType = JdbcType2JavaType.convert(jdbcType)
            return javaType
        } catch (e: Exception) {
            throw IllegalArgumentException("can't process type of column[$column]", e)
        }
    }

}