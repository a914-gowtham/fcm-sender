package com.fcmsender.fcm_sender_kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcmsender.FCMSender
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
        data.put("type", "new_logged_in")
        val push = FCMSender.Builder()
            .serverKey("Your server key")
            .toTokenOrTopic("topic/TestTopic") //either topic or user registration token
//          .toMultipleTokens(listOfToken)
            .responseListener(this)
//          .setTimeToLive(30) // 0 to 2,419,200 seconds (4 weeks)
//          .setDryRun(false)  //test a request without actually sending a message.
            .setData(data)
            .build()
        push.sendPush(this)
    }

    override fun onSuccess(response: String) {
        Log.d("onSuccess",response)
        Toast.makeText(this, "onSuccess", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(errorCode: Int,message: String) {
        Log.d("onFailure","$errorCode $message")
        Toast.makeText(this, "onFailure", Toast.LENGTH_SHORT).show()
    }

}
