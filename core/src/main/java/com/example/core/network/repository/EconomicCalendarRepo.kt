package com.example.core.network.repository

import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable

interface EconomicCalendarRepo {

    fun observeConnection(): Flowable<WebSocket.Event>
    fun sendMessage(message: ByteArray)
}