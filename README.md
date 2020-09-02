[![](https://jitpack.io/v/a914-gowtham/fcm-sender.svg)](https://jitpack.io/#a914-gowtham/fcm-sender)

# FCM-Sender

##### Push Notification Library for Android

## Usage
*For a working implementation, please have a look at the Sample Project*

1. Include the library as local library project.

+ Setup firebase messsaging [Refer this](https://firebase.google.com/docs/cloud-messaging/android/client?authuser=1)

+ Add the dependency to your app `build.gradle` file
 ```gradle
 dependencies {
    implementation 'com.github.a914-gowtham:fcm-sender:Tag'
 }
 ```
 + Add to project's root `build.gradle` file:
```gradle
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```
2. Create FcmSender builder and sendPush
```kotlin
   val data = JSONObject()
        data.put("title", "Message from Jon")
        data.put("message", "Click to view..")
        data.put("type", "new_message")
        
         val push = FCMSender.Builder()
            .serverKey("your serverkey")
            .to("/topics/myTopic") //use either topic or user registration token
            .responseListener(this)
//          .setTimeToLive(30) // 0 to 2,419,200 seconds (4 weeks)
//          .setDryRun(false)  //test a request without actually sending a message.
            .setData(data)
            .build()
        push.sendPush(this)
```

* Java
```java
     JSONObject data=new JSONObject();
        data.put("title","Message from Jon");
        data.put("message", "Click to view..");
        data.put("type", "new_message");
        
       FCMSender fcmSender= new FCMSender.Builder()
               .serverKey("your serverkey")
               .to("/topics/myTopic") //use either topic or user registration token
               .responseListener(this)
//             .setTimeToLive(30) // 0 to 2,419,200 seconds (4 weeks)
//             .setDryRun(false)  //test a request without actually sending a message.
               .setData(new JSONObject())
               .build();
       fcmSender.sendPush(this);
```

3. Using callback
```kotlin
class MainActivity : AppCompatActivity(), FCMSender.ResponseListener{
   override fun onSuccess(response: String) {
        Log.d("onSuccess",response)
    }

    override fun onFailure(errorCode: Int) {
        Log.d("onFailure","ErrorCode::$errorCode")
    }
}
```

4 Handle notification data on Client side in FirebaseMessagingService
* Kotlin
```kotlin
class FireBasePush : FirebaseMessagingService() {
override fun onMessageReceived(remoteMessage: RemoteMessage) {
super.onMessageReceived(remoteMessage)

   val nData: Map<String,String> = remoteMessage.data
   Log.d("TAG","Remote msg::"+ndata.toString())
        val title= nData["title"]
        val message= nData["message"]
}}
```

* Java
```java
public class FireBasePush extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        
         Map<String, String> ndata = remoteMessage.getData();
	 Log.d("TAG","Remote msg::"+ndata.toString());
         String title=nData.get("title");
         String message=nData.get("message");
}}   
```

## Usages

### Send to Single device
.to("user registration token")

### Send to Group of devices
Subscribe a topic on client device and send push to that topic
```java
FirebaseMessaging.getInstance().subscribeToTopic("/topics/testTopic2");
```

### TimeToLive
This parameter specifies how long (in seconds) the message should be kept in FCM storage
if the device is offline. The maximum time to live supported is 4 weeks, and the default value is 4 weeks.

* use cases:
  - Video chat incoming calls 
  - Expiring invitation events
  - Calendar events

### DryRun
This parameter, when set to **true**, allows developers to test a request without actually sending a message.

The default value is **false.**

  
## Reference
https://firebase.google.com/docs/cloud-messaging/http-server-ref#table1
