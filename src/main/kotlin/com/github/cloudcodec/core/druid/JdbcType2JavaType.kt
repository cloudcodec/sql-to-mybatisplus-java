package com.github.cloudcodec.core.druid

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl

object JdbcType2JavaType {

    private val javaTypeResolver = JavaTypeResolverDefaultImpl()
    private val typeMap: Map<Int, JavaTypeResolverDefaultImpl.JdbcTypeInformation>

    init {
        val declaredField = javaTypeResolver.javaClass.getDeclaredField("typeMap")
        declaredField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        typeMap = declaredField.get(javaTypeResolver) as Map<Int, JavaTypeResolverDefaultImpl.JdbcTypeInformation>
    }

    fun convert(jdbcTypes: Int): FullyQualifiedJavaType {
        return (typeMap[jdbcTypes] ?: error("java.sql.Types: $jdbcTypes is not in typeMap")).fullyQualifiedJavaType
    }

}