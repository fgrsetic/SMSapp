package com.franjo.smsapp.ui.messages.inbox

import android.net.Uri
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.franjo.smsapp.app.AppExecutors
import com.franjo.smsapp.data.model.entity.asDomainModel
import com.franjo.smsapp.data.repository.MessageRepository
import com.franjo.smsapp.domain.Message


class InboxMessageViewModel : ViewModel() {

    private val notifiedSmsList: MutableLiveData<List<Message>>? = null
    private var navigateToMessageDetails: MutableLiveData<Message>? = null
    private var navigateToNewMessage: MutableLiveData<Boolean>? = null
    private var navigateToContactDetails: MutableLiveData<Uri>? = null

    private val messageRepository: MessageRepository = MessageRepository.getInstance()


    val showMessages: LiveData<List<Message>> = Transformations.map(messageRepository.allMessagesGroupedByName) {
        it.asDomainModel()
    }


    //    private void loadNotifiedSmsList() {
    //        notifiedSmsList.postValue(messageRepository.getSMSList());
    //    }
    // 3) Message details
    fun navigateToMessageDetails(): LiveData<Message?>? {
        if (navigateToMessageDetails == null) {
            navigateToMessageDetails = MutableLiveData()
        }
        return navigateToMessageDetails
    }

    fun toMessageDetailsNavigated(message: Message?) {
        navigateToMessageDetails!!.value = message
    }

    fun onMessageDetailsNavigated() {
        navigateToMessageDetails!!.value = null
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

    fun loadMessagesFromStorage() {
        AppExecutors.getInstance().diskIO().execute {
            messageRepository.loadMessages()
            messageRepository.loadMMMSMessages(Telephony.Mms.MESSAGE_BOX_INBOX)
            messageRepository.loadMMMSMessages(Telephony.Mms.MESSAGE_BOX_SENT)
        }
    }

}