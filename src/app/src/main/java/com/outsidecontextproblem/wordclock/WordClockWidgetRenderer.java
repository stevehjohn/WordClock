package com.outsidecontextproblem.wordclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private final Paint _foregroundTextPaint;

    public WordClockWidgetRenderer() {

        _backgroundTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _backgroundTextPaint.setARGB(80, 0, 0, 0);
        _backgroundTextPaint.setTextAlign(Paint.Align.CENTER);
        if (_typeface != null) {
            _backgroundTextPaint.setTypeface(_typeface);
        }

        _foregroundTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foregroundTextPaint.setARGB(255, 255, 255, 255);
        _foregroundTextPaint.setTextAlign(Paint.Align.CENTER);
        _foregroundTextPaint.setShadowLayer(16f, 0,0, Color.argb(255, 255 ,255, 255));
        if (_typeface != null) {
            _foregroundTextPaint.setTypeface(_typeface);
        }
    }

    public Bitmap render(Context context) {
        Log.i(WordClockWidgetRenderer.class.getName(), "render()");

        // TODO: Cache?
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_2);
        Bitmap drawable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(drawable);

        int midX = drawable.getWidth() / 2;
        int height = drawable.getHeight();

        _backgroundTextPaint.setTextSize(height / 11f);
        _backgroundTextPaint.setLetterSpacing(0.25f);
        _foregroundTextPaint.setTextSize(height / 11f);
        _foregroundTextPaint.setLetterSpacing(0.25f);

        for (int y = 1; y < 11; y++) {
            canvas.drawText(_clockText[y - 1], midX, (height / 11f) * y + (height / 42f), _backgroundTextPaint);
        }

        canvas.drawText("IT IS      ", midX, (height / 11f) * 1 + (height / 42f), _foregroundTextPaint);
        canvas.drawText("TWENTY     ", midX, (height / 11f) * 3 + (height / 42f), _foregroundTextPaint);
        canvas.drawText("PAST       ", midX, (height / 11f) * 5 + (height / 42f), _foregroundTextPaint);
        canvas.drawText("    FIVE   ", midX, (height / 11f) * 7 + (height / 42f), _foregroundTextPaint);

        return drawable;
    }
}
