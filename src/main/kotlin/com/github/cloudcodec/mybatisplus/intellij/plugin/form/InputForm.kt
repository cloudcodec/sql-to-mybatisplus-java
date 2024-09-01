package com.github.cloudcodec.mybatisplus.intellij.plugin.form

import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextArea

class InputForm : DialogWrapper(true) {
    private val sqlTextArea = JTextArea()

    init {
        title = "sql-to-myatisplus-java Generator"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val jPanel = JPanel(BorderLayout())
        sqlTextArea.preferredSize= Dimension(700,1200)
        jPanel.add(sqlTextArea)
        return jPanel
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return sqlTextArea
    }

    val sql: String
        get() = sqlTextArea.text
}
