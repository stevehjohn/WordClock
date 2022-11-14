package com.outsidecontextproblem.wordclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class WordClockWidgetRenderer {

    private static final String[] _clockText = {
            "ITLISASAMPM",
            "ACQUARTERDC",
            "TWENTYFIVEX",
            "HALFSTENFTO",
            "PASTERUNINE",
            "ONESIXTHREE",
            "FOURFIVETWO",
            "EIGHTELEVEN",
            "SEVENTWELVE",
            "TENSEOCLOCK" };

    public static Typeface _typeface;

    private final Paint _backgroundTextPaint;
    private final Paint _foregroundTextPaint;

    private long _imageId;
    private Uri _customImageUri;

    public WordClockWidgetRenderer() {
        Log.i(this.getClass().getName(), "Constructing renderer.");

        _backgroundTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _backgroundTextPaint.setARGB(80, 0, 0, 0);
        _backgroundTextPaint.setTextAlign(Paint.Align.CENTER);
        if (_typeface != null) {
            _backgroundTextPaint.setTypeface(_typeface);
        } else {
            Log.i(this.getClass().getName(), "Typeface is null.");
        }

        _foregroundTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foregroundTextPaint.setARGB(255, 255, 255, 255);
        _foregroundTextPaint.setTextAlign(Paint.Align.CENTER);
        _foregroundTextPaint.setShadowLayer(16f, 0,0, Color.argb(255, 255 ,255, 255));
        if (_typeface != null) {
            _foregroundTextPaint.setTypeface(_typeface);
        }

        _imageId = R.drawable.background_2;
    }

    public Bitmap render(Context context, TimeZone timeZone) {
        Log.i(WordClockWidgetRenderer.class.getName(), "render()");

        // TODO: Cache?
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inScaled = false;
        options.outWidth = 500;
        options.outHeight = 500;
        Bitmap bitmap = null;
        if (_customImageUri != null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), (int) _imageId, options);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), (int) _imageId, options);
        }
        Canvas canvas = new Canvas(bitmap);

        int height = bitmap.getHeight();

        _backgroundTextPaint.setTextSize(height / 11f);
        _backgroundTextPaint.setLetterSpacing(0.25f);
        _foregroundTextPaint.setTextSize(height / 11f);
        _foregroundTextPaint.setLetterSpacing(0.25f);

        renderText(bitmap, canvas);

        renderTime(bitmap, canvas, timeZone);

        return bitmap;
    }

    public void updateFromSettings(Settings settings) {
        updatePaint(_backgroundTextPaint, settings.getBackgroundSettings());
        updatePaint(_foregroundTextPaint, settings.getForegroundSettings());

        ElementSettings shadow = settings.getForegroundSettings();

        _foregroundTextPaint.setShadowLayer(16f, 0, 0, Color.argb(shadow.getOpacity() * 5, shadow.getRed() * 5, shadow.getGreen() * 5, shadow.getBlue() * 5));

        _imageId = settings.getImageId();
        _customImageUri = settings.getCustomImageUri();
    }

    private void updatePaint(Paint paint, ElementSettings settings) {
        Log.i(this.getClass().getName(), "Updating paints.");

        paint.setARGB(settings.getOpacity() * 5, settings.getRed() * 5, settings.getGreen() * 5, settings.getBlue() * 5);
    }

    private void renderText(Bitmap drawable, Canvas canvas) {
        int midX = drawable.getWidth() / 2;
        int height = drawable.getHeight();

        for (int y = 1; y < 11; y++) {
            canvas.drawText(_clockText[y - 1], midX, (height / 11f) * y + (height / 42f), _backgroundTextPaint);
        }
    }

    private void renderTime(Bitmap drawable, Canvas canvas, TimeZone timeZone) {
        int midX = drawable.getWidth() / 2;
        int height = drawable.getHeight();

        Calendar calendar = Calendar.getInstance(timeZone);

        int minute = calendar.get(Calendar.MINUTE);

        if (minute < 5) {
            canvas.drawText("IT IS      ", midX, (height / 11f) * 1 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("     OCLOCK", midX, (height / 11f) * 10 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 10) {
            canvas.drawText("      FIVE ", midX, (height / 11f) * 3 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("PAST       ", midX, (height / 11f) * 5 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 15) {
            canvas.drawText("     TEN   ", midX, (height / 11f) * 4 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("PAST       ", midX, (height / 11f) * 5 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 20) {
            canvas.drawText("  QUARTER  ", midX, (height / 11f) * 2 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("PAST       ", midX, (height / 11f) * 5 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 25) {
            canvas.drawText("TWENTY     ", midX, (height / 11f) * 3 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("PAST       ", midX, (height / 11f) * 5 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 30) {
            canvas.drawText("TWENTYFIVE ", midX, (height / 11f) * 3 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("PAST       ", midX, (height / 11f) * 5 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 35) {
            canvas.drawText("HALF       ", midX, (height / 11f) * 4 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("PAST       ", midX, (height / 11f) * 5 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 40) {
            canvas.drawText("TWENTYFIVE ", midX, (height / 11f) * 3 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("         TO", midX, (height / 11f) * 4 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 45) {
            canvas.drawText("TWENTY     ", midX, (height / 11f) * 3 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("         TO", midX, (height / 11f) * 4 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 50) {
            canvas.drawText("  QUARTER  ", midX, (height / 11f) * 2 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("         TO", midX, (height / 11f) * 4 + (height / 42f), _foregroundTextPaint);
        } else if (minute < 55) {
            canvas.drawText("     TEN   ", midX, (height / 11f) * 4 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("         TO", midX, (height / 11f) * 4 + (height / 42f), _foregroundTextPaint);
        } else  {
            canvas.drawText("      FIVE ", midX, (height / 11f) * 3 + (height / 42f), _foregroundTextPaint);
            canvas.drawText("         TO", midX, (height / 11f) * 4 + (height / 42f), _foregroundTextPaint);
        }

        int hour = calendar.get(Calendar.HOUR);

        if (minute >= 35) {
            hour++;
        }

        switch (hour) {
            case 0:
            case 12:
                canvas.drawText("     TWELVE", midX, (height / 11f) * 9 + (height / 42f), _foregroundTextPaint);
                break;
            case 1:
                canvas.drawText("ONE        ", midX, (height / 11f) * 6 + (height / 42f), _foregroundTextPaint);
                break;
            case 2:
                canvas.drawText("        TWO", midX, (height / 11f) * 7 + (height / 42f), _foregroundTextPaint);
                break;
            case 3:
                canvas.drawText("      THREE", midX, (height / 11f) * 6 + (height / 42f), _foregroundTextPaint);
                break;
            case 4:
                canvas.drawText("FOUR       ", midX, (height / 11f) * 7 + (height / 42f), _foregroundTextPaint);
                break;
            case 5:
                canvas.drawText("    FIVE   ", midX, (height / 11f) * 7 + (height / 42f), _foregroundTextPaint);
                break;
            case 6:
                canvas.drawText("   SIX     ", midX, (height / 11f) * 6 + (height / 42f), _foregroundTextPaint);
                break;
            case 7:
                canvas.drawText("SEVEN      ", midX, (height / 11f) * 9 + (height / 42f), _foregroundTextPaint);
                break;
            case 8:
                canvas.drawText("EIGHT      ", midX, (height / 11f) * 8 + (height / 42f), _foregroundTextPaint);
                break;
            case 9:
                canvas.drawText("       NINE", midX, (height / 11f) * 5 + (height / 42f), _foregroundTextPaint);
                break;
            case 10:
                canvas.drawText("TEN        ", midX, (height / 11f) * 10 + (height / 42f), _foregroundTextPaint);
                break;
            case 11:
                canvas.drawText("     ELEVEN", midX, (height / 11f) * 8 + (height / 42f), _foregroundTextPaint);
                break;
        }
    }
}
