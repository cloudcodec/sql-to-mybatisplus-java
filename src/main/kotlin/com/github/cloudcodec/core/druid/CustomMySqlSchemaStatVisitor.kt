package com.github.cloudcodec.core.druid

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor

class CustomMySqlSchemaStatVisitor : MySqlSchemaStatVisitor() {

    val myColumns = mutableListOf<SQLColumnDefinition>()

    override fun visit(x: SQLColumnDefinition): Boolean {
        myColumns.add(x)
        return false
    }

}