package com.androidpotato.androidpotato.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.androidpotato.androidpotato.widget.dto.Coordinate;
import com.androidpotato.mylibrary.util.UtilLog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David on 2017/6/21 0021.
 */

public class TouchView extends View {
    private static final String TAG = "TouchView";
    private static final float RADIUS = 30;
    private Context context;
    private boolean isInit = true;
    private List<Coordinate> circleCenters;
    private int showCount;
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
        circleCenters = new ArrayList<Coordinate>();
        paint  = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        showCount = circleCenters.size();
        drawCircles(canvas);
        isInit = false;

    }
    private void drawCircles(Canvas canvas) {
        for (Coordinate center : circleCenters) {
            if (center.isShow()) {
                canvas.drawCircle(center.getX(), center.getY(), RADIUS, paint);
            }
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        UtilLog.i(TAG, "onDraw");
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
        for (Coordinate coordinate : circleCenters) {
            if (coordinate.isShow()) {
                float disXAbs = Math.abs(x - coordinate.getX());
                float disYAbs = Math.abs(y - coordinate.getY());
                double distance = Math.sqrt(Math.pow(disXAbs, 2) + Math.pow(disYAbs, 2));
                if (distance < RADIUS) {
                    coordinate.setShow(false);
                    showCount--;
                    invalidate();
                }
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        UtilLog.i(TAG, "onTouchEvent: X = " + event.getX() + ", Y = " + event.getY());

        if (showCount != 0) {
            checkDistance(event.getX(), event.getY());
        } else {
            Toast.makeText(context, "检测完毕", Toast.LENGTH_SHORT).show();
        }


        super.onTouchEvent(event);
        return true;
    }
}
