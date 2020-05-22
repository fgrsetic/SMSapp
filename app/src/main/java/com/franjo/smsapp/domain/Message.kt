package com.franjo.smsapp.domain

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app.
 * These are the objects that should be displayed on screen, or manipulated by the app.
 */
data class Message(
        val phoneNumber: String,
        var name: String,
        val messageBody: String,
        val date: String?

)




