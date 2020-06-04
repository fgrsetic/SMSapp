package com.franjo.smsapp.ui.messages.conversations_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.franjo.smsapp.data.model.entity.asDomainModel
import com.franjo.smsapp.data.repository.MessageRepository
import com.franjo.smsapp.domain.Message

class DetailsViewModel(val threadId: Int) : ViewModel() {

    private val messageRepository = MessageRepository.getInstance()

    val showMessagesList: LiveData<List<Message>> = Transformations.map(messageRepository.loadMessagesById(threadId)) {
        it.asDomainModel()
    }
}