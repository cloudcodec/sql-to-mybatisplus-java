package com.github.cloudcodec.mybatisplus.intellij.plugin.util

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

object NotificationsUtil {

    private const val msgGroupId = "sql-to-mybatisplus-java"

    fun info(msg: Any) {
        Notifications.Bus.notify(Notification(msgGroupId, "", msg.toString(), NotificationType.INFORMATION))
    }

}