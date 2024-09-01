package com.github.cloudcodec.core

import org.junit.jupiter.api.Assertions.*

class JpaConverterTest {

    @org.junit.jupiter.api.Test
    fun convertTest() {
        val sql = """
            CREATE TABLE `tb_service_account`(
                `id` BIGINT  NOT NULL AUTO_INCREMENT COMMENT 'id comment',
                `name` VARCHAR(255) NOT NULL COMMENT 'name comment',
                `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                PRIMARY KEY(`id`)
            )
        """.trimIndent()

        val ormThings = Convert.convert(sql, "com.github.cloudcodec", true, true)
        for (thing in ormThings) {
            thing as JpaThing

            assertEquals(thing.entity.className, "ServiceAccount")
            assertEquals(thing.repository.className, "ServiceAccountRepository")

            assertEquals(
                thing.entity.content, """package com.github.cloudcodec.entity

import java.util.Date

@javax.persistence.Entity
@javax.persistence.Table(name="tb_service_account")
@org.hibernate.annotations.DynamicInsert
@org.hibernate.annotations.DynamicUpdate
data class ServiceAccount(
    @javax.persistence.Column(name="id")
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    var id: Long? = null,
    @javax.persistence.Column(name="name")
    var name: String? = null,
    @javax.persistence.Column(name="created_time")
    @org.hibernate.annotations.CreationTimestamp
    var createdTime: Date? = null,
    @javax.persistence.Column(name="updated_time")
    @org.hibernate.annotations.UpdateTimestamp
    var updatedTime: Date? = null
)"""
            )


            assertEquals(
                thing.repository.content, """package com.github.cloudcodec.repository

import com.github.cloudcodec.entity.ServiceAccount
import org.springframework.data.repository.PagingAndSortingRepository

@org.springframework.stereotype.Repository
interface ServiceAccountRepository : PagingAndSortingRepository<ServiceAccount, Long>"""
            )

        }
    }
}