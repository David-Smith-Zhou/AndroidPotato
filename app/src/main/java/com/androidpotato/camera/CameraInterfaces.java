package com.androidpotato.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.androidpotato.mylibrary.util.UtilLog;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;



/**
 * Created by David on 2017/5/31 0031.
 */

public class CameraInterfaces {
    private static final String TAG = "CameraInterfaces";
    private Camera mCamera;
    private static CameraInterfaces mCameraInterfaces;

    private CameraInterfaces() {
        getCamera();
    }

    public static synchronized CameraInterfaces getInstance() {
        if (mCameraInterfaces == null) {
            mCameraInterfaces = new CameraInterfaces();
        }
        return mCameraInterfaces;
    }

    public Camera getCamera() {
        if (mCamera == null) {
            mCamera = Camera.open();
        }
        return mCamera;
    }

    public void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }
    public void setCameraParameters(Context context, int width, int height) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        int displayRotation = display.getRotation();
        int preViewRotation = 0;
        switch (displayRotation) {
            case Surface.ROTATION_0:
                preViewRotation = 0;
                break;
            case Surface.ROTATION_90:
                preViewRotation = 90;
                break;
            case Surface.ROTATION_180:
                preViewRotation = 180;
                break;
            case Surface.ROTATION_270:
                preViewRotation = 270;
                break;
            default:
                if (displayRotation % 90 == 0) {
                    preViewRotation = (360 + displayRotation) % 360;
                }
        }
        preViewRotation += 90;
        mCamera.setDisplayOrientation(preViewRotation);

        Camera.Size  currSize = mCamera.getParameters().getPreviewSize();
        UtilLog.i(TAG, "current preview sizes: size.width = " + currSize.width + ", size.height = " + currSize.height + ", ratio = " + new DecimalFormat("##.##").format(currSize.width / (double)currSize.height));

        Camera.Parameters parameters = mCamera.getParameters();

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Iterator<Camera.Size> psIterator = previewSizes.iterator();
        while (psIterator.hasNext()) {
            Camera.Size size = psIterator.next();
            UtilLog.i(TAG, "support preview sizes: size.width = " + size.width + ", size.height = " + size.height + ", ratio = " + new DecimalFormat("##.##").format(size.width / (double)size.height));
        }
        Camera.Size setSize = previewSizes.get(0);
        UtilLog.i(TAG, "set size: width = " + setSize.width + ", height = " + setSize.height);
        parameters.setPreviewSize(setSize.width, setSize.height);

        // show the support FPS range on this device
        List<int[]> fpsList = parameters.getSupportedPreviewFpsRange();
        for (int[] each : fpsList) {
            UtilLog.i(TAG, "fpsList: " + Arrays.toString(each));
        }
        parameters.setPreviewFpsRange(fpsList.get(0)[0], fpsList.get(0)[1]);
        mCamera.setParameters(parameters);
    }
    public void setPreviewDisplay(SurfaceHolder holder) throws IOException{
        mCamera.setPreviewDisplay(holder);
    }
    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        mCamera.setPreviewCallback(previewCallback);
    }
    public void release() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
            // set mCameraInterfaces null so that initialize mCamera when called getInstance() second time
            mCameraInterfaces = null;
        }
    }
}
