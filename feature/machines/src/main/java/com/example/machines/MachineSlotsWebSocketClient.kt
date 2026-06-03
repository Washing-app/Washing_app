package com.example.machines

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MachineSlotsWebSocketClient(
    private val onSlotHoldChanged: (machineId: Long, date: String, slotId: Long) -> Unit
) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/ws/slot-holds")
            .build()

        webSocket = client.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    val machineId = text.substringAfter("\"machineId\":")
                        .substringBefore(",")
                        .trim()
                        .toLong()

                    val date = text.substringAfter("\"date\":\"")
                        .substringBefore("\"")

                    val slotId = text.substringAfter("\"slotId\":")
                        .substringBefore("}")
                        .trim()
                        .toLong()

                    onSlotHoldChanged(machineId, date, slotId)
                }

                override fun onFailure(
                    webSocket: WebSocket,
                    t: Throwable,
                    response: Response?
                ) {
                    t.printStackTrace()
                }
            }
        )
    }

    fun disconnect() {
        webSocket?.close(1000, "Screen closed")
        webSocket = null
    }
}