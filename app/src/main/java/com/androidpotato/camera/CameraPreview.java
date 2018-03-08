package com.androidpotato.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.davidzhou.library.util.LogUtil;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by David on 2017/5/31 0031.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private CameraInterfaces cameraInterfaces;
    private Camera.PreviewCallback mCallback;

    public CameraPreview(Context context) {
        this(context, null);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        cameraInterfaces = CameraInterfaces.getInstance();
    }

    public void setPreviewCallback( Camera.PreviewCallback callback) {
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        LogUtil.i(TAG, "get height = " + height + ", width = " + width  + ", ratio = " + new DecimalFormat("##.##").format(height / (double)width));
        cameraInterfaces.setCameraParameters(getContext(), width, height);
        try {
            cameraInterfaces.setPreviewDisplay(holder);
            cameraInterfaces.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            return;
        }
        cameraInterfaces.stopPreview();
        try {
            cameraInterfaces.setPreviewDisplay(holder);
//            mCamera.setPreviewCallback(mCallback);
            cameraInterfaces.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    public void release() {
        this.getHolder().removeCallback(this);
        cameraInterfaces.setPreviewCallback(null);
    }
}
