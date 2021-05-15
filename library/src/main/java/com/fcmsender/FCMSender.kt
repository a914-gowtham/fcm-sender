package com.fcmsender

import android.content.Context
import org.json.JSONObject

class FCMSender(
    private val serverKey: String?,
    private val toTokenOrTopic: String?,
    private val toMultipleToken: ArrayList<String>?,
    private val timeToLive: Long?,
    private val dryRun: Boolean?,
    private val customData: JSONObject?,
    private val listener: ResponseListener?) {

    interface ResponseListener {
        fun onSuccess(response: String)
        fun onFailure(errorCode: Int,message: String)
    }

    fun sendPush() {
        try {
            when {
                serverKey.isNullOrEmpty() -> {
                    throw NullPointerException("Serverkey is null")
                }
                listener == null -> throw NullPointerException("Response Listener is null")
                toTokenOrTopic == null && toMultipleToken==null -> throw NullPointerException("toTokenOrTopic and toMultipleToken is null.You can use either one")
                toTokenOrTopic != null && toMultipleToken!=null -> throw IllegalArgumentException("You've passed toTokenOrTopic and toMultipleToken.You can use either one")
                toMultipleToken != null && toMultipleToken.isEmpty() -> throw IllegalArgumentException("You've passed a empty list for toMultipleToken")
                customData == null -> throw NullPointerException("data is null")
                else -> {
                    val apiCall = ApiCall(
                         serverKey, listener,
                        toTokenOrTopic,toMultipleToken, timeToLive, dryRun, customData
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
        private var toTokenOrTopic: String? = null,
        private var toMultipleTokens: ArrayList<String>?=null,
        private var timeToLive: Long? = null,
        private var dryRun: Boolean=false,
        private var customData: JSONObject? = null) {

        fun serverKey(serverKey: String) = apply { this.serverKey = serverKey }
        fun toTokenOrTopic(to: String) = apply { this.toTokenOrTopic = to }
        fun toMultipleTokens(tokens: ArrayList<String>) = apply { this.toMultipleTokens = tokens }
        fun setTimeToLive(timeToLive: Long) = apply { this.timeToLive = timeToLive }
        fun setDryRun(enable: Boolean) = apply { this.dryRun = enable }
        fun setData(data: JSONObject) = apply { this.customData = data }
        fun responseListener(listener: ResponseListener?) = apply { this.listener = listener }
        fun build() =
            FCMSender(serverKey, toTokenOrTopic,toMultipleTokens, timeToLive, dryRun, customData, listener)
    }
}