package com.example.aqil.katalogfilmuiux.scheduler;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.aqil.katalogfilmuiux.DetailActivity;
import com.example.aqil.katalogfilmuiux.MainActivity;
import com.example.aqil.katalogfilmuiux.R;
import com.example.aqil.katalogfilmuiux.adapter.MovieAdapter;
import com.example.aqil.katalogfilmuiux.entity.Movie;
import com.example.aqil.katalogfilmuiux.service.GetReleaseMovieService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AlarmScheduler extends BroadcastReceiver {
    public static final String TYPE_REMINDER = "Reminder";
    public static final String TYPE_RELEASE_DATE = "Release Date";
    public static final String EXTRA_TYPE = "type";
    private static final String TAG = "AlarmScheduler";
    private final int NOTIF_ID_REMINDER = 100;
    private final int NOTIF_ID_RELEASE = 101;
    public static String EXTRA_RECEIVER = "extra receiver";
    public static String EXTRA_LIST = "extra_list";


    public AlarmScheduler() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = "null";
        if (intent != null) {
            type = intent.getStringExtra(EXTRA_TYPE);
            Log.d(TAG, "onReceive: " + type);
        }

        String message;
        if (type != null) {
            if (type.equals(TYPE_REMINDER)) {
                String title;
                title = context.getString(R.string.notify_reminder_title);
                int notifId;
                notifId = NOTIF_ID_REMINDER;
                message = context.getString(R.string.notify_reminder_message);
                showAlarmNotification(context, title, message, notifId, null);
            }
        } else if (type != null) {
            if (type.equals(TYPE_RELEASE_DATE)) {
                Receiver receiver = new Receiver(new Handler(), context);
                Intent sendReceiverIntent = new Intent(context, GetReleaseMovieService.class);
                sendReceiverIntent.putExtra(EXTRA_RECEIVER, receiver);
                context.startService(sendReceiverIntent);
            }

        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId, Movie movie) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String CHANNEL_ID = "";


        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CHANNEL_ID = "0";
            CharSequence name = "main channel";
            String Description = "notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(mChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        Intent intent;
        if (notifId == NOTIF_ID_REMINDER) {
            intent = new Intent(context, MainActivity.class);
        } else {
            intent = new Intent(context, DetailActivity.class);
            intent.putExtra(MovieAdapter.EXTRA_MOVIE, movie);
            intent.putExtra(MovieAdapter.EXTRA_REQUEST_CODE, 1);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, notifId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, builder.build());
        }

    }

    public void setRepeatTimeAlarm(Context context, String type, String time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmScheduler.class);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        int requestCode;
        String toastMessege;
        if (type.equals(TYPE_REMINDER)) {
            requestCode = NOTIF_ID_REMINDER;
            toastMessege = context.getString(R.string.enable_reminder);

        } else {
            requestCode = NOTIF_ID_RELEASE;
            toastMessege = context.getString(R.string.enable_notification);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, toastMessege, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "setRepeatTimeAlarm: " + type);
    }


    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmScheduler.class);
        int requestCode;
        String toastMesssege;
        if (type.equals(TYPE_REMINDER)) {
            requestCode = NOTIF_ID_REMINDER;
            toastMesssege = context.getString(R.string.cancel_reminder);
        } else {
            requestCode = NOTIF_ID_RELEASE;
            toastMesssege = context.getString(R.string.cancel_notification);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, toastMesssege, Toast.LENGTH_SHORT).show();
    }

    public class Receiver extends android.os.ResultReceiver implements Parcelable {
        private ArrayList<Movie> list;
        Context context;

        Receiver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            list = resultData.getParcelableArrayList(EXTRA_LIST);
            int notifId = NOTIF_ID_RELEASE;

            Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String foramttedCurrentDate = formatter.format(currentDate);
            //foramttedCurrentDate = "2018-05-23";

            for (int i = 0; i < list.size(); i++) {
                notifId++;
                if (list != null) {
                    Log.d(TAG, "onReceiveResult: " + foramttedCurrentDate + " VS " + list.get(i).getRelease_date());
                    if (foramttedCurrentDate.equals(list.get(i).getRelease_date())) {
                        showAlarmNotification(context, context.getString(R.string.release_notification_title), list.get(i).getTitle(), notifId, list.get(i));
                    }
                }
            }

        }


    }
}

