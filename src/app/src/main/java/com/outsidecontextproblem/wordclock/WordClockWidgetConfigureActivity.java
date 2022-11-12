package com.outsidecontextproblem.wordclock;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.outsidecontextproblem.wordclock.databinding.WordClockWidgetConfigureBinding;

public class WordClockWidgetConfigureActivity extends Activity {

    private WordClockWidgetConfigureBinding _binding;

    public WordClockWidgetConfigureActivity() {
        super();
    }

    private final View.OnClickListener _addOnClickListener = view -> {
        final Context context = WordClockWidgetConfigureActivity.this;

//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        BatteryClockWidget.updateAppWidget(context, appWidgetManager, _appWidgetId, _settings);
//
//        _settings.saveSettings(context);

        Intent resultValue = new Intent();
//        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _appWidgetId);
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
    }
}