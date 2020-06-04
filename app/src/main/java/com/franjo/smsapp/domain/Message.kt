package com.franjo.smsapp.domain

import android.os.Parcelable
import com.franjo.smsapp.data.model.entity.DatabaseConversation
import kotlinx.android.parcel.Parcelize

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app.
 * These are the objects that should be displayed on screen, or manipulated by the app.
 */
@Parcelize
data class Message(
        var id: Int,
        var contentType: String,
        var threadId: Int,
        var phoneNumber: String,
        var messageBody: String,
        var messageType: Int,
        var mmsType: String,
        var dateMsgReceived: Long,
        var dateMsgSent: Long,
        var bodyMessageAttachment: String

) : Parcelable
