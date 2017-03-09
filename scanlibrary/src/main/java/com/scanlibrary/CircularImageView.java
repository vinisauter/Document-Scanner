package com.scanlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircularImageView extends ImageView {
    Path clipPath = new Path();

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (w != oldW || h != oldH) {
            generateMaskPath(w, h, h);
        }
    }

    private void generateMaskPath(int w, int h, int radius) {
        clipPath = new Path();
        RectF rect = new RectF(0, 0, w, h);
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}