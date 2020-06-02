package com.franjo.smsapp.ui.messages.conversations

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.franjo.smsapp.app.AppExecutors
import com.franjo.smsapp.data.model.entity.asDomainModel
import com.franjo.smsapp.data.repository.MessageRepository
import com.franjo.smsapp.domain.Conversation


class ConversationsViewModel : ViewModel() {

    // Internally, we use a MutableLiveData to handle navigation to the selected conversation
    private val _navigateToConversationDetails = MutableLiveData<Conversation>()
    // The external immutable LiveData for the navigation conversation
    val navigateToConversationDetails: LiveData<Conversation> get() = _navigateToConversationDetails



    private var navigateToNewMessage: MutableLiveData<Boolean>? = null
    private var navigateToContactDetails: MutableLiveData<Uri>? = null

    private val messageRepository: MessageRepository = MessageRepository.getInstance()


    // 1. Load and save messages from storage to local DB
    fun loadMessagesFromStorage() {
        AppExecutors.getInstance().diskIO().execute {
            messageRepository.loadAndSaveStorageMessages()
        }
    }

    // 2. Load from local DB and show it in the list
    val showConversationsList: LiveData<List<Conversation>> = Transformations.map(messageRepository.conversations) {
        it.asDomainModel()
    }

    // 3) Conversation details
    fun toConversationsDetailsNavigated(conversation: Conversation) {
        _navigateToConversationDetails.value = conversation
    }

    fun onConversationsDetailsNavigated() {
        _navigateToConversationDetails.value = null
    }


    // 4) New message
    fun navigateToNewMessage(): LiveData<Boolean?>? {
        if (navigateToNewMessage == null) {
            navigateToNewMessage = MutableLiveData()
        }
        return navigateToNewMessage
    }

    fun toNewMessageNavigated() {
        navigateToNewMessage!!.value = true
    }

    fun doneNavigationToNewMessage() {
        navigateToNewMessage!!.value = false
    }



    // 5) Contact details
    fun navigateToContactDetails(): LiveData<Uri?>? {
        if (navigateToContactDetails == null) {
            navigateToContactDetails = MutableLiveData()
        }
        return navigateToContactDetails
    }

    fun doneNavigationToContactDetails() {
        navigateToContactDetails!!.postValue(null)
    }

}