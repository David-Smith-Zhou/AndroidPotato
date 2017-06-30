package com.androidpotato.androidpotato.camera;

import android.hardware.Camera;
import android.view.View;

import com.androidpotato.mylibrary.util.UtilLog;

import java.util.Arrays;
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
    public void setCameraParameters(View view) {
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        int width = view.getWidth();
        int height = view.getHeight();


        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        // show the support FPS range on this device
        List<int[]> fpsList = parameters.getSupportedPreviewFpsRange();
        for (int[] each : fpsList) {
            UtilLog.i(TAG, "fpsList: " + Arrays.toString(each));
        }
        mCamera.setParameters(parameters);
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
