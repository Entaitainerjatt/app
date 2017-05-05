package com.entertainerJatt.app.android.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.UpdateActivity;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.util.Util;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by sony on 4/6/2017.
 */

public class FireMsgService extends FirebaseMessagingService {
    private Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = this;
        Log.d("Msg", "Message received [" + remoteMessage.toString() + "]");
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);
        Map<String, String> map = remoteMessage.getData();

        UserInfo userInfo = new UserInfo();
        userInfo.setMAP(map.toString());
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        userInfo.setTime(currentDateTimeString);
        String imageUrl = map.get("imgUrl");
        try {
            URL url = new URL(imageUrl);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(this)
                    .setLargeIcon(image)
                    .setSmallIcon(R.mipmap.defaut_directory)
                    .setContentTitle("Entertainer Jatt")
                    .setContentText(map.get("title"))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1410, notificationBuilder.build());
        } catch (Exception e) {
            System.out.println(e);
        }
        Log.i("Map : ", map.toString());

    }
}