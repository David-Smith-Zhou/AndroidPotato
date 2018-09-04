package com.androidpotato.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.androidpotato.widget.dto.Coordinate;
import com.davidzhou.library.util.ULog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by David on 2017/6/21 0021.
 */

public class TouchView extends View {
    private static final String TAG = "TouchView";
    private static final float RADIUS = 30;
    private LinkedList<Coordinate> circleCenters;
    private Paint paint;
    private Canvas mCanvas;
    private static final int RECORD_POINTS_NUMBER = 10;
    private float[] mPoints = new float[RECORD_POINTS_NUMBER * 2];
    private int mIndex = 0;
    private Path mPointPath;

    public TouchView(Context context, AttributeSet sets) {
        super(context, sets);
        init();
    }

    public TouchView(Context context) {
        this(context, null);
    }

    private void init() {
        circleCenters = new LinkedList<Coordinate>();
        PathEffect pathEffect = new CornerPathEffect(60);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(pathEffect);
        mPointPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ULog.i(TAG, "onMeasure: width = " + getMeasuredWidth() + ", height = " + getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ULog.i(TAG, "onDraw");
        mCanvas = canvas;
    }

    public void clear() {
//        Paint paint = new Paint();
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        mCanvas.drawPaint(paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        ULog.i(TAG, "onTouchEvent: X = " + event.getX() + ", Y = " + event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_UP:
                clear();
                mPointPath.reset();
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:
                mPointPath.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPointPath.lineTo(event.getX(), event.getY());
                break;
        }
        if (!mPointPath.isEmpty()) {
            mCanvas.drawPoints(mPoints, paint);
            mCanvas.drawPath(mPointPath, paint);
            invalidate();
        }
        return true;
    }
}
