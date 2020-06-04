package com.franjo.smsapp.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app.
 * These are the objects that should be displayed on screen, or manipulated by the app.
 */
@Parcelize
data class Conversation (
        var id: Int,
        var threadId: Int,
        var recipient: String,
        var snippet: String?,
        var dateMsgCreated: Long
) : Parcelable



