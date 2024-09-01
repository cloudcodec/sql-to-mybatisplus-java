package com.github.cloudcodec.core.druid

import com.alibaba.druid.sql.SQLUtils
import com.alibaba.druid.sql.ast.expr.SQLCharExpr
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser
import com.github.cloudcodec.core.Column
import com.github.cloudcodec.core.TableInfo

object Parser {

    fun parse(sql: String): List<TableInfo> {
        val createStatementList = MySqlStatementParser(sql).parseStatementList()
        check(createStatementList.isNotEmpty()) { "there is no 'CREATE TABLE' in sql" }
        return createStatementList.filterIsInstance<MySqlCreateTableStatement>().map { statement ->
            val visitor = CustomMySqlSchemaStatVisitor()
            statement.accept(visitor)
            check(statement.like == null) { "like is forbidden" }
            check(visitor.tables.size == 1) { "there should only be one table" }
            val tableNameWithDatabase = visitor.tables.keys.first().name // test-db1.table1
            val arr = tableNameWithDatabase.split(".")
            val tableName = SQLUtils.normalize(arr.last())
            check(tableName.isNotBlank()) { "tableName[$tableName] is blank" }
            check(visitor.myColumns.isNotEmpty()) { "columns should not be empty" }
            val columns = visitor.myColumns.map { column ->
                Column(
                    SQLUtils.normalize(column.columnName),
                    TypesConverter.getJavaType(column),
                    (column.comment as SQLCharExpr?)?.text,
                    column.isPrimaryKey,
                    column.isAutoIncrement,
                )
            }
            TableInfo(tableName, columns)
        }
    }


}

