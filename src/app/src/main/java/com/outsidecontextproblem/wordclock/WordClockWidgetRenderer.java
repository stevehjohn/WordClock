package com.outsidecontextproblem.wordclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

public class WordClockWidgetRenderer {

    private static final String[] _clockText = {
            "ITLISASAMPM",
            "ACQUARTERDC",
            "TWENTYFIVEX",
            "HALFSTENFTO",
            "PASTERUNINE",
            "ONESIZTHREE",
            "FOURFIVETWO",
            "EIGHTELEVEN",
            "SEVENTWELVE",
            "TENSEOCLOCK" };

    public static Typeface _typeface;

    private final Paint _backgroundTextPaint;

    public WordClockWidgetRenderer() {

        _backgroundTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _backgroundTextPaint.setARGB(255, 255, 255, 255);
        _backgroundTextPaint.setTextSize(Constants.TextSizeDefault);
        _backgroundTextPaint.setTextAlign(Paint.Align.CENTER);
        if (_typeface != null) {
            _backgroundTextPaint.setTypeface(_typeface);
        }
    }

    public Bitmap render(Context context) {
        Log.i(WordClockWidgetRenderer.class.getName(), "render()");

        // TODO: Cache?
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_1);
        Bitmap drawable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(drawable);

        for (int y = 0; y < 10; y++) {
            canvas.drawText(_clockText[y], 250, y * 45, _backgroundTextPaint);
        }

        return drawable;
    }
}
