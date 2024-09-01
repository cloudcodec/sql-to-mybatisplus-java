package com.github.cloudcodec

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.github.cloudcodec.jpa.intellij.plugin.task.GenerateTask

class GenerateAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val selectedFile = requireNotNull(event.getData(LangDataKeys.VIRTUAL_FILE))
        val project = requireNotNull(event.project)
        GenerateTask(selectedFile, project).run()
    }

}