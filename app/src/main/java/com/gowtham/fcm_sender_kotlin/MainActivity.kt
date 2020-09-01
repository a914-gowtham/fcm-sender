package com.gowtham.fcm_sender_kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gowtham.library.FCMSender
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), FCMSender.ResponseListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_send_push.setOnClickListener { sendPush() }
    }

    private fun sendPush() {
        val data = JSONObject()
        data.put("title", "Message from Jon")
        data.put("message", "Click to view..")
        data.put("type", "new_message")
        val push = FCMSender.Builder()
            .serverKey("ServerKey")
            .to("")    //give either topic or user registration token
            .responseListener(this)
            .setTimeToLive(30)
            .setData(data)
            .build()
        push.sendPush(this)
    }

    override fun onResponse(messageId: String, responseCode: Int) {
        Toast.makeText(this, responseCode.toString(), Toast.LENGTH_SHORT).show()
    }
}
