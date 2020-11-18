package com.huashengfu.StemCellsManager.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideRoundTransformation extends BitmapTransformation {

    private float radius;

    public GlideRoundTransformation(Context context) {
        super(context);
    }

    public GlideRoundTransformation(Context context, int dp){
        super(context);
        radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return crop(pool, toTransform);
    }

    private Bitmap crop(BitmapPool pool, Bitmap source){
        if(source == null)
            return null;

        Bitmap output = Bitmap.createBitmap(source.getWidth(),
                source.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = radius;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, rect, rect, paint);

        return output;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}
