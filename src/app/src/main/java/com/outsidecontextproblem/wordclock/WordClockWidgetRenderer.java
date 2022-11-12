package com.outsidecontextproblem.wordclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class WordClockWidgetRenderer {

    public Bitmap render(Context context) {
        Log.i(WordClockWidgetRenderer.class.getName(), "render()");

        // TODO: Cache?
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_1);
        Bitmap drawable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(drawable);

        return drawable;
    }
}
