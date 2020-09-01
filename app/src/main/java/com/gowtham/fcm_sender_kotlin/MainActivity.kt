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
            .serverKey("your serverkey")
            .to("/topics/myTopic") //either topic or user registration token
            .responseListener(this)
//          .setTimeToLive(30) // 0 to 2,419,200 seconds (4 weeks)
//          .setDryRun(false)  //test a request without actually sending a message.
            .setData(data)
            .build()
        push.sendPush(this)
    }

    override fun onSuccess(response: String) {
        Log.d("onSuccess",response)
    }

    override fun onFailure(errorCode: Int) {
        Log.d("onFailure","s$errorCode")
    }

}
