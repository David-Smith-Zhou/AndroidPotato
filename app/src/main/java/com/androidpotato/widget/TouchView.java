package com.androidpotato.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.androidpotato.widget.dto.Coordinate;
import com.davidzhou.library.util.ULog;

import java.util.Iterator;
import java.util.LinkedList;


/**
 * Created by David on 2017/6/21 0021.
 */

public class TouchView extends View {
    private static final String TAG = "TouchView";
    private static final float RADIUS = 30;
    private Context context;
    private boolean isInit = true;
    private LinkedList<Coordinate> circleCenters;
    private Paint paint;

    public TouchView(Context context, AttributeSet sets) {
        super(context, sets);
        this.context = context;
        init();
    }
    public TouchView(Context context) {
        this(context, null);
    }

    private void init() {
        circleCenters = new LinkedList<Coordinate>();
        paint  = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ULog.i(TAG, "onMeasure: width = " + getMeasuredWidth() + ", height = " + getMeasuredHeight());
    }

    private void createCircles(Canvas canvas) {
        paint.setColor(Color.BLUE);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        for (float x = RADIUS, y = RADIUS; x < width; x += 2 * RADIUS) {
            circleCenters.add(new Coordinate(x, y));
        }

        for (float x = RADIUS, y = height / 2; x < width; x += 2 * RADIUS) {
            circleCenters.add(new Coordinate(x, y));
        }

        for (float x = RADIUS, y = height - RADIUS; x < width; x += 2 * RADIUS) {
            circleCenters.add(new Coordinate(x, y));
        }

        for (float x = RADIUS, y = RADIUS; y < height; y += 2 * RADIUS) {
            circleCenters.add(new Coordinate(x, y));
        }

        for (float x = width / 2, y = RADIUS; y < height; y += 2 * RADIUS) {
            circleCenters.add(new Coordinate(x, y));
        }

        for (float x = width - RADIUS, y = RADIUS; y < height; y += 2 * RADIUS) {
            circleCenters.add(new Coordinate(x, y));
        }
        drawCircles(canvas);
        isInit = false;

    }
    private void drawCircles(Canvas canvas) {
        for (Coordinate center : circleCenters) {
//            if (center.isShow()) {
                canvas.drawCircle(center.getX(), center.getY(), RADIUS, paint);
//            }
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        ULog.i(TAG, "onDraw");
        super.onDraw(canvas);
//        paint.setColor(Color.YELLOW);
//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        if (isInit) {
            createCircles(canvas);
        } else {
            drawCircles(canvas);
        }

    }

    private void checkDistance(float x, float y) {

        Iterator<Coordinate> iterator = circleCenters.iterator();
        while(iterator.hasNext()) {
            Coordinate coordinate = iterator.next();
            float disXAbs = Math.abs(x - coordinate.getX());
            float disYAbs = Math.abs(y - coordinate.getY());
            double distance = Math.sqrt(Math.pow(disXAbs, 2) + Math.pow(disYAbs, 2));
            if (distance < RADIUS) {
                iterator.remove();
                ULog.i(TAG, "circleCenters size = " + circleCenters.size());
                invalidate();
                break;
            }
        }

//        for (Coordinate coordinate : circleCenters) {
//            if (coordinate.isShow()) {
//                float disXAbs = Math.abs(x - coordinate.getX());
//                float disYAbs = Math.abs(y - coordinate.getY());
//                double distance = Math.sqrt(Math.pow(disXAbs, 2) + Math.pow(disYAbs, 2));
//                if (distance < RADIUS) {
//                    coordinate.setShow(false);
//                    showCount--;
//                    invalidate();
//                }
//            }
//        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ULog.i(TAG, "onTouchEvent: X = " + event.getX() + ", Y = " + event.getY());

        if (circleCenters.size() != 0) {
            checkDistance(event.getX(), event.getY());
        } else {
            ULog.i(TAG, "onTouchEvent: no circles");
        }
        super.onTouchEvent(event);
        return true;
    }
}
