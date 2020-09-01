package com.gowtham.library

import android.content.Context
import org.json.JSONObject
import kotlin.NullPointerException

class FCMSender(
    private val serverKey: String?,
    private val to: String?,
    private val timeToLive: Long?,
    private val customData: JSONObject?,
    private val listener: ResponseListener?
) {

    interface ResponseListener {
        fun onResponse(messageId: String, responseCode: Int)
    }

    fun sendPush(context: Context) {
        try {
            when {
                serverKey.isNullOrEmpty() -> { throw NullPointerException("Serverkey is null")}
                listener == null -> throw NullPointerException("Response Listener is null")
                to == null -> throw NullPointerException("to is null")
                customData == null -> throw NullPointerException("data is null")
                else -> {
                    val apiCall = ApiCall(
                        context, serverKey, listener,
                        to, timeToLive,customData
                    )
                    apiCall.sendPush()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    data class Builder(
        private var serverKey: String? = null,
        private var listener: ResponseListener? = null,
        private var to: String? = null,
        private var timeToLive: Long?=null,
        private var customData: JSONObject? = null
    ) {
        fun serverKey(serverKey: String) = apply { this.serverKey = serverKey }
        fun to(to: String) = apply { this.to = to }
        fun setTimeToLive(timeToLive: Long) = apply { this.timeToLive = timeToLive }
        fun setData(data: JSONObject) = apply { this.customData = data }
        fun responseListener(listener: ResponseListener?) = apply { this.listener = listener }
        fun build() =
            FCMSender(serverKey, to,timeToLive, customData, listener)
    }
}