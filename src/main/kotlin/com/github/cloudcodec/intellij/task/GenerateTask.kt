package com.github.cloudcodec.jpa.intellij.plugin.task

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.github.cloudcodec.intellij.form.InputForm
import com.github.cloudcodec.core.*
import com.github.cloudcodec.intellij.util.NotificationsUtil

class GenerateTask(
    private val directory: VirtualFile,
    private val project: Project
) {
    private val isJpa = false
    private val isKotlin = false

    fun run() {
        val inputForm = InputForm()
        if (!inputForm.showAndGet()) {
            NotificationsUtil.info("canceled")
            return
        }
        NotificationsUtil.info(inputForm.sql)
        if (inputForm.sql.isBlank()) {
            NotificationsUtil.info("sql is blank")
            return
        }

        val packageName = getPackageName(directory)
        val things = Convert.convert(inputForm.sql, packageName, isJpa, isKotlin)
        val ext=if(isKotlin)".kt" else ".java"

        ApplicationManager.getApplication().runWriteAction {
            things.forEach { thing ->
                when(thing){
                    is MybatisPlusThing -> {
                writeMybatisPlusFiles(directory, thing,ext)
                    }
                    is JpaThing ->{
                writeJpaFiles(directory, thing,ext)
                    }
                }
            }
        }
    }

    private fun writeMybatisPlusFiles(directory: VirtualFile, thing: MybatisPlusThing, ext: String) {
        writeFile(
            mkdirs(directory, listOf("entity")),
            thing.entity.className + ext,
            thing.entity.content
        )
        writeFile(
            mkdirs(directory, listOf("mapper")),
            thing.mapper.className + ext,
            thing.mapper.content
        )
        writeFile(
            mkdirs(directory, listOf("dao")),
            thing.dao.className + ext,
            thing.dao.content
        )
    }

    private fun writeJpaFiles(directory: VirtualFile, thing: JpaThing, ext: String) {
        writeFile(
            mkdirs(directory, listOf("entity")),
            thing.entity.className + ext,
            thing.entity.content
        )
        writeFile(
            mkdirs(directory, listOf("repository")),
            thing.repository.className + ext,
            thing.repository.content
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
