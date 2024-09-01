package com.github.cloudcodec.mybatisplus.intellij.plugin.task

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.github.cloudcodec.mybatisplus.intellij.plugin.form.InputForm
import com.github.cloudcodec.mybatisplus.intellij.core.*
import com.github.cloudcodec.mybatisplus.intellij.plugin.util.NotificationsUtil

class GenerateTask(
    private val directory: VirtualFile,
    private val project: Project
) {

    fun run() {
        val inputForm = InputForm()
        if (!inputForm.showAndGet()) {
            NotificationsUtil.info("canceled")
            return
        }
        NotificationsUtil.info(inputForm.sql)
        if(inputForm.sql.isBlank()){
            NotificationsUtil.info("sql is blank")
            return
        }

        val packageName = getPackageName(directory)
        val mybatisPlusThings = Converter.convert(inputForm.sql, packageName)

        ApplicationManager.getApplication().runWriteAction {
            mybatisPlusThings.forEach {
                writeFiles(directory, it)
            }
        }
    }

    private fun writeFiles(directory: VirtualFile, mybatisPlusThing: MybatisPlusThing) {
        writeFile(
            mkdirs(directory, listOf("entity")),
            mybatisPlusThing.entity.className + ".java",
            mybatisPlusThing.entity.content
        )
        writeFile(
            mkdirs(directory, listOf("mapper")),
            mybatisPlusThing.mapper.className + ".java",
            mybatisPlusThing.mapper.content
        )
        writeFile(
            mkdirs(directory, listOf("dao")),
            mybatisPlusThing.dao.className + ".java",
            mybatisPlusThing.dao.content
        )
    }

    private fun getPackageName(directory: VirtualFile): String {
        var cur: VirtualFile? = directory
        var nameList = mutableListOf<String>()
        while (cur != null) {
            if (cur.name == "java" && cur.parent != null && cur.parent.name == "main" && cur.parent.parent != null && cur.parent.parent.name == "src") {
                if (nameList.isEmpty()) {
                    throw IllegalStateException("${directory.path}对应的src/main/java下没有package,请先新建")
                }
                return nameList.reversed().joinToString(separator = ".")
            }
            nameList.add(cur.name)
            cur = cur.parent
        }
        throw IllegalStateException("can't find src/main/java in ${directory.path}")
    }


    private fun mkdirs(parent: VirtualFile, subDirList: List<String>): VirtualFile {
        var p = parent
        subDirList.forEach {
            p = p.findChild(it) ?: p.createChildDirectory(p, it)
        }
        return p
    }

    private fun writeFile(directory: VirtualFile, fileName: String, content: String) {
        val file = directory.findChild(fileName) ?: directory.createChildData(directory, fileName)
        file.setBinaryContent(content.toByteArray())
    }

}
