package com.github.cloudcodec.mybatisplus.intellij

import com.github.cloudcodec.mybatisplus.intellij.core.Converter
import org.junit.jupiter.api.Assertions.*

class ConverterTest {

    @org.junit.jupiter.api.Test
    fun convertTest() {
        val sql="""
            CREATE TABLE `tb_service_account`(
                `id` BIGINT  NOT NULL AUTO_INCREMENT COMMENT 'id comment',
                `name` VARCHAR(255) NOT NULL COMMENT 'name comment',
                `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                PRIMARY KEY(`id`)
            )
        """.trimIndent()

        val mybatisPlusThings = Converter.convert(sql, "com.github.cloudcodec")
        for (mybatisPlusThing in mybatisPlusThings) {
            assertEquals(mybatisPlusThing.entity.className,"ServiceAccountDO")
            assertEquals(mybatisPlusThing.mapper.className,"ServiceAccountMapper")
            assertEquals(mybatisPlusThing.dao.className,"ServiceAccountDAO")

            assertEquals(mybatisPlusThing.entity.content,"""package com.github.cloudcodec.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@TableName(value="`tb_service_account`",autoResultMap = true)
@Data
public class ServiceAccountDO {
    /**
     * id comment
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "`id`")
    Long id;

    /**
     * name comment
     */
    @TableField(value = "`name`")
    String name;

    /**
     * 创建时间
     */
    @TableField(value = "`created_time`", fill = FieldFill.INSERT)
    Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "`updated_time`", fill = FieldFill.INSERT_UPDATE)
    Date updatedTime;
}""")


            assertEquals(mybatisPlusThing.mapper.content,"""package com.github.cloudcodec.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.cloudcodec.entity.ServiceAccountDO;

public class ServiceAccountMapper implements BaseMapper<ServiceAccountDO> {
}""")

            assertEquals(mybatisPlusThing.dao.content,"""package com.github.cloudcodec.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudcodec.entity.ServiceAccountDO;
import com.github.cloudcodec.mapper.ServiceAccountMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceAccountDAO extends ServiceImpl<ServiceAccountMapper, ServiceAccountDO> {
}""")

        }
    }
}