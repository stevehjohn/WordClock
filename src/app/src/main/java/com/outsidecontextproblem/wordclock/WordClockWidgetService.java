package com.outsidecontextproblem.wordclock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.Nullable;

public class WordClockWidgetService extends Service implements Runnable, DisplayManager.DisplayListener {

    private static final String NOTIFICATION_CHANNEL_ID = "com.outsidecontextproblem.wordclock";

    private static final int NOTIFICATION_ID = 721356359;

    private Handler _handler;

    private static WordClockWidgetService _instance;

    public static WordClockWidgetService getInstance() {
        return _instance;
    }

    public WordClockWidgetService() {
        _instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.i(WordClockWidgetService.class.getName(), "onStartCommand()");

        NotificationChannel notificationChannel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
        );

        NotificationManager notificationManager = getSystemService((NotificationManager.class));

        notificationManager.createNotificationChannel(notificationChannel);

        Notification.Builder notificationBuilder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.notification);

        startForeground(NOTIFICATION_ID, notificationBuilder.build());

        if (_handler == null) {
            _handler = new Handler();

            setNextCallback();
        }

        DisplayManager displayManager = (DisplayManager) getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(this, null);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        Log.i(WordClockWidgetService.class.getName(), "run()");

        Context context = getApplicationContext();

        Intent intent = new Intent(context, WordClockWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, WordClockWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

        context.sendBroadcast(intent);

        setNextCallback();
    }

    @Override
    public void onDisplayAdded(int i) {
    }

    @Override
    public void onDisplayRemoved(int i) {
    }

    @Override
    public void onDisplayChanged(int i) {
        Log.i(WordClockWidgetService.class.getName(), "onDisplayChanged()");

        run();
    }

    public void setNextCallback() {
        Log.i(WordClockWidgetService.class.getName(), "setNextCallBack()");

        _handler.removeCallbacks(this);

        long callbackMillis = DateUtils.MINUTE_IN_MILLIS - System.currentTimeMillis() % DateUtils.MINUTE_IN_MILLIS;

        Log.i(WordClockWidgetService.class.getName(), String.format("Setting callback for %d", callbackMillis));

        _handler.postDelayed(this, callbackMillis);
    }
}
