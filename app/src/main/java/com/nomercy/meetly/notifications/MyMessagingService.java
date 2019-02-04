package com.nomercy.meetly.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nomercy.meetly.Controller.ResultActivity;
import com.nomercy.meetly.Model.DBHelper;
import com.nomercy.meetly.Model.User;
import com.nomercy.meetly.R;
import com.nomercy.meetly.api.APIInterface;
import com.nomercy.meetly.api.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyMessagingService extends FirebaseMessagingService {
    Retrofit retrofit;
    DBHelper mDBhelper;
    @Override
    public void onNewToken(String token) {
        final String TAG = "NEW_TOKEN";
        Log.d(TAG, "Refreshed token: " + token);
        Toast.makeText(this,token, Toast.LENGTH_LONG).show();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        mDBhelper = new DBHelper(this);
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BaseUrl)
                .build();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       //sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        String date="", time="", title="", body="";
        String click_action = "";
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);
             date = remoteMessage.getData().get("date");
             time = remoteMessage.getData().get("time");
          //  RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);
             title = remoteMessage.getNotification().getTitle();
             body = remoteMessage.getNotification().getBody();
            click_action = remoteMessage.getNotification().getClickAction();





        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("dateOfMeet222", "fdfdf");
        resultIntent.putExtra("timeOfMeet222", time);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        1,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body + "\n" + date + "\n" + time )
                .setCustomContentView(notificationLayout)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());


    }


}
