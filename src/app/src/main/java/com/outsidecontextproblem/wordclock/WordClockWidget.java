package com.outsidecontextproblem.wordclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.view.Display;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.TimeZone;

public class WordClockWidget extends AppWidgetProvider {

    private static final HashMap<Integer, WordClockWidgetRenderer> _renderers = new HashMap<>();
    private static final HashMap<Integer, Settings> _settings = new HashMap<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Settings settings) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.word_clock_widget);

        if (settings != null) {
            Log.i(WordClockWidget.class.getName(), String.format("Applying settings to widget %d.", appWidgetId));

            WordClockWidgetRenderer renderer = new WordClockWidgetRenderer();
            renderer.updateFromSettings(settings);
            _renderers.put(appWidgetId, renderer);

            _settings.put(appWidgetId, settings);
        } else {
            if (! _renderers.containsKey(appWidgetId)) {
                Log.i(WordClockWidget.class.getName(), String.format("Attempting load of settings for widget %d.", appWidgetId));

                settings = new Settings(appWidgetId);
                settings.loadSettings(context);

                WordClockWidgetRenderer renderer = new WordClockWidgetRenderer();
                renderer.updateFromSettings(settings);
                _renderers.put(appWidgetId, renderer);

                _settings.put(appWidgetId, settings);
            }
        }

        draw(context, appWidgetManager, appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Log.i(WordClockWidget.class.getName(), String.format("Deleting settings for widget %d.", appWidgetId));

            Settings settings = new Settings(appWidgetId);
            settings.deleteSettings(context);
        }
    }

    @Override
    public void onEnabled(Context context) {
        if (WordClockWidgetRenderer._typeface == null) {
            WordClockWidgetRenderer._typeface = context.getResources().getFont(R.font.roboto);
        }
    }

    @Override
    public void onDisabled(Context context) {
    }

    private static void draw(Context context, AppWidgetManager appWidgetManager, int appWidgetId, RemoteViews views) {
        Log.i(WordClockWidget.class.getName(), "draw()");

        boolean displayOn = false;

        DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);

        for (Display display : displayManager.getDisplays()) {
            if (display.getState() != Display.STATE_OFF) {
                displayOn = true;
                break;
            }
        }

        if (!displayOn) {
            Log.i(WordClockWidget.class.getName(), "Display is off, skipping update.");

            return;
        }

        if (views == null) {
            Log.i(WordClockWidget.class.getName(), "Views is null.");

            return;
        }

        Settings settings = _settings.get(appWidgetId);
        TimeZone timeZone;
        if (settings != null) {
            timeZone = TimeZone.getTimeZone(settings.getTimeZone());
        } else {
            Log.w(WordClockWidget.class.getName(), String.format("Settings not found in HashMap for widget %d, using default.", appWidgetId));

            timeZone = TimeZone.getDefault();
        }

        WordClockWidgetRenderer renderer = _renderers.get(appWidgetId);
        if (renderer == null) {
            Log.w(WordClockWidget.class.getName(), "No renderer found, using default.");

            renderer = new WordClockWidgetRenderer();
        }

        Bitmap bitmap = renderer.render(context, timeZone);

        views.setImageViewBitmap(R.id.imageView, bitmap);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        bitmap.recycle();
    }
}