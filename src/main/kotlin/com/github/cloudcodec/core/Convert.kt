package com.github.cloudcodec.core

import com.github.cloudcodec.core.druid.Parser

object Convert {

    fun convert(sql: String, pkg: String, isJpa: Boolean, isKotlin: Boolean): List<OrmThing> {
        val tableInfos = Parser.parse(sql)
        if (isJpa) {
            if (isKotlin) {
                return JpaKotlinConverter.convert(tableInfos, pkg)
            }
        } else {
            if (!isKotlin) {
                return MybatisPlusJavaConverter.convert(tableInfos, pkg)
            }
        }
        throw IllegalArgumentException("unsupported types, isJpa:$isJpa, isKotlin:$isKotlin")
    }

}