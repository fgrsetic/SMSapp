package com.franjo.smsapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.franjo.smsapp.domain.Message


@Entity(tableName = "MessageTable")
class DatabaseMessage {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var contentType: String? = null
    var threadId: Int = 0
    var address: String? = null
    var messageBody: String? = null
    var messageType: Int? = null
    var mmsType: String? = null
    var dateMsgReceived: Long? = null
    var dateMsgSent: Long? = null
    var bodyMessageAttachment: String? = null
}

fun List<DatabaseMessage>.asDomainModel(): List<Message> {
    return map {
        Message(
                id = it.id,
                contentType = it.contentType,
                threadId = it.threadId,
                phoneNumber = it.address,
                messageBody = it.messageBody,
                messageType = it.messageType,
                mmsType = it.mmsType,
                dateMsgReceived = it.dateMsgReceived,
                dateMsgSent = it.dateMsgSent,
                bodyMessageAttachment = it.bodyMessageAttachment
        )
    }
}