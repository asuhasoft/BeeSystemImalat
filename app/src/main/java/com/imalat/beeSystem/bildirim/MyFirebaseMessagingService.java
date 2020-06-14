package com.imalat.beeSystem.bildirim;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.imalat.beeSystem.R;
import com.imalat.beeSystem.view.AcilisSayfasi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.util.Map;


/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]


    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            sendNotification(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    private String mesajBasligi = "";
    private String mesajIcerigi = "";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.d(TAG, "Message Notification getBody: " + remoteMessage.getNotification().getBody());
            //Log.d(TAG, "Message Notification getImageUrl: " + remoteMessage.getNotification().getImageUrl());

        }


        if (remoteMessage.getData() != null) {

            mesajBasligi = remoteMessage.getNotification().getTitle();
            mesajIcerigi = remoteMessage.getNotification().getBody();
            getImage(remoteMessage);

            Log.e(TAG, "getTitle" + mesajBasligi);
            Log.e(TAG, "getTitle" + mesajIcerigi);

        } else {
            //Log.d(TAG, "data yok");
            sendNotificationNormal(remoteMessage);
        }

    }

    @Override
    public void onNewToken(String token) {
        //Log.d(TAG, "Refreshed token: " + token);
    }


    public void sendNotificationNormal(RemoteMessage remoteMessage) {
        Log.d(TAG, "tiklandi" + "sendNotificationNormal");

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);

        Log.e(TAG, "params" + remoteMessage.toString());
        Log.e(TAG, "params" + remoteMessage.getData());
        Log.e(TAG, "JSON_OBJECT" + object.toString());

/*        Intent intent = new Intent(this, safakEkraniYeniTasarim.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);
*/
        Intent notifyIntent = new Intent(this, AcilisSayfasi.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String NOTIFICATION_CHANNEL_ID = getResources().getString(R.string.app_name);

        long pattern[] = {0, 1000, 500, 1000};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Bildirimler", NotificationManager.IMPORTANCE_DEFAULT);

            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_ALARM).build();
            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setSound(defaultSoundUri, audioAttributes);
            notificationChannel.setShowBadge(true);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }


        /*if (remoteMessage.getNotification().getImageUrl()!= null) {
            final NotificationTarget notificationTarget = new NotificationTarget(getApplicationContext(), largeRemoteViews, R.id.notification_image, notification, 0);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override public void run() {
                    Glide
                            .with(getApplicationContext())
                            .load(remoteMessage.getNotification().getImageUrl().toString())
                            .asBitmap()
                            .into(notificationTarget);
                }
            });
        }*/

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification))

                // .setStyle(new NotificationCompat.BigPictureStyle()
                //.bigPicture(image))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


       /* Glide.with(getApplicationContext())
                .asBitmap()
                .load(remoteMessage.getNotification().getImageUrl().toString())
                ;*/




        /*NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = notificationBuilder.build();
            notificationManager.notify(0, notification);

            if (remoteMessage.getData().get(IMAGE_URL) != null) {
                final NotificationTarget notificationTarget = new NotificationTarget(getApplicationContext(),
                        largeRemoteViews, R.id.notification_image, notification, 0);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override public void run() {
                        Glide.with(getApplicationContext()).load(remoteMessage.getData().get(IMAGE_URL)).asBitmap().into(notificationTarget);
                    }
                });
            }*/


        int NOTIFICATION_ID = (int) (System.currentTimeMillis() % 10000);


        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    private void sendNotification(Bitmap bitmap) {

        Log.d(TAG, "tiklandi" + "sendNotification");


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), AcilisSayfasi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                //.setContentTitle(Config.title)
                .setContentTitle(mesajBasligi)
                .setContentText(mesajIcerigi)
                .setAutoCancel(true)
                .setSound(defaultSound)
                //.setContentText(Config.content)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setLargeIcon(bitmap)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(1, notificationBuilder.build());


    }

    private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        Config.title = data.get("title");
        Config.content = data.get("kategoriid");
        Config.imageUrl = data.get("image");
        Config.gameUrl = data.get("urunid");

        Log.e(TAG, "gameUrl" + Config.gameUrl);

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);

        Log.e(TAG, "paramsX" + remoteMessage.toString());
        Log.e(TAG, "paramsX" + remoteMessage.getData());
        Log.e(TAG, "JSON_OBJECTX" + object.toString());


        //Create thread to fetch image from notification
        if (remoteMessage.getData() != null) {

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Get image from data Notification
                    Picasso.get()
                            .load(remoteMessage.getNotification().getImageUrl())
                            .into(target);
                }
            });


        }
    }

}