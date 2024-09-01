package com.github.cloudcodec.intellij.util

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

object NotificationsUtil {

    private const val msgGroupId = "sql-to-jpa-kotlin"

    fun info(msg: Any) {
        Notifications.Bus.notify(Notification(msgGroupId, "", msg.toString(), NotificationType.INFORMATION))
    }

}