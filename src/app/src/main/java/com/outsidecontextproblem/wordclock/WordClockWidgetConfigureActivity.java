package com.outsidecontextproblem.wordclock;

import android.app.Activity;
import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.outsidecontextproblem.wordclock.databinding.WordClockWidgetConfigureBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class WordClockWidgetConfigureActivity extends Activity {

    @SuppressWarnings("FieldCanBeLocal")
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
        _binding.buttonAdd.setOnClickListener(_addOnClickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            _appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (_appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        Context context = getApplicationContext();

        if (WordClockWidgetRenderer._typeface == null) {
            WordClockWidgetRenderer._typeface = context.getResources().getFont(R.font.roboto);
        }

        _settings = new Settings(_appWidgetId);

        WordClockWidgetService serviceInstance = WordClockWidgetService.getInstance();
        if (serviceInstance != null) {
            WordClockWidgetService.getInstance().setNextCallback();
        }

        if (! serviceIsRunning(context)) {
            Log.i(WordClockWidgetConfigureActivity.class.getName(), "onCreate(): Starting service.");

            Intent serviceIntent = new Intent(context, WordClockWidgetService.class);
            context.startForegroundService(serviceIntent);
        }

        configureTimezones(context);
    }

    private void configureTimezones(Context context) {
        Spinner spinner = findViewById(R.id.spinContinent);

        List<String> continents = new ArrayList<>();

        continents.add("Africa");
        continents.add("America");
        continents.add("Antarctica");
        continents.add("Arctic");
        continents.add("Asia");
        continents.add("Atlantic");
        continents.add("Australia");
        continents.add("Brazil");
        continents.add("Canada");
        continents.add("Chile");
        continents.add("Cuba");
        continents.add("Egypt");
        continents.add("Eire");
        continents.add("Europe");
        continents.add("Iceland");
        continents.add("Indian");
        continents.add("Iran");
        continents.add("Israel");
        continents.add("Jamaica");
        continents.add("Japan");
        continents.add("Kwajalein");
        continents.add("Libya");
        continents.add("Mexico");
        continents.add("Navajo");
        continents.add("Pacific");
        continents.add("Poland");
        continents.add("Portugal");
        continents.add("Singapore");
        continents.add("Turkey");
        continents.add("US");
        continents.add("UTC");
        continents.add("Zulu");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, continents);
        spinner.setAdapter(adapter);

        String[] timezone = _settings.getTimeZone().split("/");

        int index = continents.indexOf(timezone[0]);

        if (index > -1) {
            spinner.setSelection(index);
        }

        continentSelected();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                continentSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void continentSelected() {
        Spinner continentSpinner = findViewById(R.id.spinContinent);

        String selection = (String) continentSpinner.getSelectedItem();

        List<String> locations = new ArrayList<>();

        for (String id : TimeZone.getAvailableIDs()) {
            String[] split = id.split("/");

            if (split.length > 1 && split[0].equals(selection)) {
                locations.add(split[1]);
            }
        }

        if (locations.size() == 0) {
            locations.add(" - ");

            _settings.setTimeZone(selection);
        }

        Spinner locationSpinner = findViewById(R.id.spinLocation);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, locations);
        locationSpinner.setAdapter(adapter);

        String[] timezone = _settings.getTimeZone().split("/");

        if (timezone.length > 1) {
            int index = locations.indexOf(timezone[1]);

            if (index > -1) {
                locationSpinner.setSelection(index);
            }
        }

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void locationSelected() {
        Spinner continentSpinner = findViewById(R.id.spinContinent);

        String continent = (String) continentSpinner.getSelectedItem();

        Spinner locationSpinner = findViewById(R.id.spinLocation);

        String location = (String) locationSpinner.getSelectedItem();

        if (! location.equals(" - ")) {
            _settings.setTimeZone(String.format("%s/%s", continent, location));
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