package com.outsidecontextproblem.wordclock;

import android.app.Activity;
import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.outsidecontextproblem.wordclock.databinding.WordClockWidgetConfigureBinding;

public class WordClockWidgetConfigureActivity extends Activity {

    private WordClockWidgetConfigureBinding _binding;

    public WordClockWidgetConfigureActivity() {
        super();
    }

    int _appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private Settings _settings;

    private final View.OnClickListener _addOnClickListener = view -> {
        final Context context = WordClockWidgetConfigureActivity.this;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        WordClockWidget.updateAppWidget(context, appWidgetManager, _appWidgetId, _settings);

        //_settings.saveSettings(context);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        _binding = WordClockWidgetConfigureBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
        _binding.addButton.setOnClickListener(_addOnClickListener);

        Context context = getApplicationContext();

        if (WordClockWidgetRenderer._typeface == null) {
            WordClockWidgetRenderer._typeface = context.getResources().getFont(R.font.roboto);
        }

        WordClockWidgetService serviceInstance = WordClockWidgetService.getInstance();
        if (serviceInstance != null) {
            WordClockWidgetService.getInstance().setNextCallback();
        }

        if (! serviceIsRunning(context)) {
            Log.i(WordClockWidgetConfigureActivity.class.getName(), "onCreate(): Starting service.");

            Intent serviceIntent = new Intent(context, WordClockWidgetService.class);
            context.startForegroundService(serviceIntent);
        }
    }

    private boolean serviceIsRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // noinspection deprecation - acceptable to use for locating an app's own service
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (WordClockWidgetService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}