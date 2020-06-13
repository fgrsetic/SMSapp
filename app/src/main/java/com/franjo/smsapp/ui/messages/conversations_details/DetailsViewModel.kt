package com.franjo.smsapp.ui.messages.conversations_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.franjo.smsapp.data.model.entity.asDomainModel
import com.franjo.smsapp.data.repository.MessageRepository
import com.franjo.smsapp.domain.Message

class DetailsViewModel(private val threadId: Int) : ViewModel() {

    private val messageRepository = MessageRepository.getInstance()

    private val _text = MutableLiveData<String>()
    val message: LiveData<String> get() = _text

    private val _enterMessageVisibility = MutableLiveData<Boolean>(true)
    val enterMessageVisibility: LiveData<Boolean> get() = _enterMessageVisibility

    private val _iconsVisibility = MutableLiveData<Boolean>(true)
    val iconsVisibility: LiveData<Boolean> get() = _iconsVisibility

    private val _enterMessageFullVisibility = MutableLiveData<Boolean>(false)
    val enterMessageFullVisibility: LiveData<Boolean> get() = _enterMessageFullVisibility

    private val _redoArrowVisibility = MutableLiveData<Boolean>(false)
    val redoArrowVisibility: LiveData<Boolean> get() = _redoArrowVisibility


    val showMessagesList: LiveData<List<Message>> = Transformations.map(messageRepository.loadMessagesById(threadId)) {
        it.asDomainModel()
    }

    fun onViewClickListener() {
        _enterMessageVisibility.value = false
        _iconsVisibility.value = false
        _redoArrowVisibility.value = true
        _enterMessageFullVisibility.value = true
    }

    fun onRedoArrowClickListener() {
        _enterMessageVisibility.value = true
        _iconsVisibility.value = true
        _redoArrowVisibility.value = false
        _enterMessageFullVisibility.value = false
    }

}