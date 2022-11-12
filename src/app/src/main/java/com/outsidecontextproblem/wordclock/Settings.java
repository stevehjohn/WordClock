package com.outsidecontextproblem.wordclock;

import java.util.TimeZone;

public class Settings {

    public static final String PREFERENCES_NAME = "com.outsidecontextproblem.wordclock.WordClockWidget";

    private static final String TIMEZONE = "Timezone";

    private String _timeZone;

    public String getTimeZone() {
        return _timeZone;
    }

    public void setTimeZone(String timeZone) {
        _timeZone = timeZone;
    }

    private final int _appWidgetId;

    public Settings(int appWidgetId) {
        _appWidgetId = appWidgetId;

        _timeZone = TimeZone.getDefault().getID();
    }
}
