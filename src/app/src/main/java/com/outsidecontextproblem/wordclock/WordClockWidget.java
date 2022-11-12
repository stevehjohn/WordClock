package com.outsidecontextproblem.wordclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import java.util.Set;

public class WordClockWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Settings settings) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.word_clock_widget);

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
        WordClockWidgetRenderer renderer = new WordClockWidgetRenderer();

        Bitmap bitmap = renderer.render(context);

        views.setImageViewBitmap(R.id.imageView, bitmap);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        bitmap.recycle();
    }
}