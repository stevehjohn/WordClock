package com.outsidecontextproblem.wordclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.util.TimeZone;

public class Settings {

    public static final String PREFERENCES_NAME = "com.outsidecontextproblem.wordclock.WordClockWidget";

    private static final String TIMEZONE = "Timezone";
    private static final String FOREGROUND = "Foreground";
    private static final String BACKGROUND = "Background";
    private static final String IMAGEID = "ImageId";

    private String _timeZone;

    public String getTimeZone() {
        return _timeZone;
    }

    public void setTimeZone(String timeZone) {
        _timeZone = timeZone;
    }

    private long _imageId;

    public long getImageId() { return _imageId; }

    public void setImageId(long imageId) { _imageId = imageId; }

    private Uri _customImageUri;

    public Uri getCustomImageUri() { return _customImageUri; }

    public void setCustomImageUri(Uri customImageUri) { _customImageUri = customImageUri; }

    private final ElementSettings _foregroundSettings;
    private final ElementSettings _backgroundSettings;

    public ElementSettings getForegroundSettings() {
        return _foregroundSettings;
    }
    public ElementSettings getBackgroundSettings() {
        return _backgroundSettings;
    }

    private final int _appWidgetId;

    public Settings(int appWidgetId) {
        _appWidgetId = appWidgetId;

        _foregroundSettings = new ElementSettings(appWidgetId,51, 51, 51, 51);
        _backgroundSettings = new ElementSettings(appWidgetId,16, 0, 0, 0);

        _timeZone = TimeZone.getDefault().getID();

        _imageId = R.drawable.background_2;
    }

    @SuppressLint("DefaultLocale")
    public void loadSettings(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        _timeZone = prefs.getString(String.format("%s.%d", TIMEZONE, _appWidgetId), TimeZone.getDefault().getID());
        _imageId = prefs.getLong(String.format("%s.%d", IMAGEID, _appWidgetId), R.drawable.background_2);

        _foregroundSettings.loadSettings(context, FOREGROUND);
        _backgroundSettings.loadSettings(context, BACKGROUND);
    }

    @SuppressLint("DefaultLocale")
    public void saveSettings(Context context) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        prefs.putString(String.format("%s.%d", TIMEZONE, _appWidgetId), _timeZone);
        prefs.putLong(String.format("%s.%d", IMAGEID, _appWidgetId), _imageId);
        prefs.apply();

        _foregroundSettings.saveSettings(context, FOREGROUND);
        _backgroundSettings.saveSettings(context, BACKGROUND);
    }

    @SuppressLint("DefaultLocale")
    public void deleteSettings(Context context) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCES_NAME, 0).edit();
        prefs.remove(String.format("%s.%d", TIMEZONE, _appWidgetId));
        prefs.remove(String.format("%s.%d", IMAGEID, _appWidgetId));
        prefs.apply();

        _foregroundSettings.deleteSettings(context, FOREGROUND);
        _backgroundSettings.deleteSettings(context, BACKGROUND);
    }
}
