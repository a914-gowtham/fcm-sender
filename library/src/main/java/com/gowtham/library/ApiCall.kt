package com.gowtham.library

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class ApiCall(val context: Context) {

    private var serverKey: String? = null

    private var responseListener: FCMSender.ResponseListener? = null

    private var to: String? = null

    private var timeToLive: Long?=null

    private var customData: JSONObject? = null

    constructor(
        context: Context,
        serverKey: String,
        responseListener: FCMSender.ResponseListener,
        to: String?,
        timeToLive: Long?,
        customData: JSONObject?
    ) : this(context) {
        this.serverKey = serverKey
        this.responseListener = responseListener
        this.to = to
        this.timeToLive = timeToLive
        this.customData = customData
    }

    fun sendPush() {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("to", to)
            jsonObject.put("priority","high")
            timeToLive?.let { jsonObject.put("time_to_live",it) }
            customData?.let { jsonObject.put("data", it) }
            requestApi(createBody(jsonObject.toString()))
        } catch (e: Exception) {
        }
    }

    private fun requestApi(requestBody: RequestBody) {
        CoroutineScope(IO).launch {
            apiRequest(requestBody)
        }
    }

    private fun apiRequest(requestBody: RequestBody) {
        try {
            val client = OkHttpClient.Builder()
                .readTimeout(30000, TimeUnit.SECONDS).build()
            val request = Request.Builder()
            request.url("https://fcm.googleapis.com/fcm/send")
            request.addHeader("Authorization", "key=$serverKey")
            request.addHeader("Content-Type", "application/json")
            request.post(requestBody)
            client.newCall(request.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    GlobalScope.launch(Dispatchers.Main) {
                        responseListener?.onResponse(e.localizedMessage!!,2)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    GlobalScope.launch(Dispatchers.Main) {
                        responseListener?.onResponse(response.body.toString(), response.code)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createBody(body: String) =
        body.toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull());

}