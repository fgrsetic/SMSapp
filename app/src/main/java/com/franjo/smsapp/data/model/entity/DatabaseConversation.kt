package com.franjo.smsapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.franjo.smsapp.domain.Conversation

@Entity(tableName = "ConversationTable")
class DatabaseConversation {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var threadId: Int = 0
    var recipient: String = ""
    var snippet: String? = null
    var attachment: Int? = null
    var dateMsgCreated: Long = 0L

}


fun List<DatabaseConversation>.asDomainModel(): List<Conversation> {
    return map {
        Conversation(
                id = it.id,
                threadId = it.threadId,
                recipient = it.recipient,
                snippet = it.snippet.toString(),
                dateMsgCreated = it.dateMsgCreated
        )
    }

}





