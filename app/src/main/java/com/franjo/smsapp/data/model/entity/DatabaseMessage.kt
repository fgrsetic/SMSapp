package com.franjo.smsapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.franjo.smsapp.domain.Message

@Entity
class DatabaseMessage {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var threadId: Int = 0
    var address: String = ""
    var messageBody: String? = ""
    var messageType: Int = 0
    var date: String? = ""
}


fun List<DatabaseMessage>.asDomainModel(): List<Message> {
    return map {
        Message(
                phoneNumber = it.address,
                name = "",
                messageBody = it.messageBody.toString(),
                date = it.date
        )
    }

}





