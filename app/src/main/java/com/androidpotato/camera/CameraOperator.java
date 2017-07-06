package com.androidpotato.camera;

import com.androidpotato.mylibrary.util.UtilLog;

/**
 * Created by David on 2017/7/4 0004.
 */

public class CameraOperator {
    private static final String TAG = "CameraOperator";
    private static CameraOperator cameraOperator;

    private CameraPreview cameraPreview;
    private CameraInterfaces cameraInterfaces;

    private CameraOperator() {
        cameraInterfaces = CameraInterfaces.getInstance();
    }

    public static CameraOperator getInstance() {
        if (cameraOperator == null) {
            cameraOperator = new CameraOperator();
        }
        return cameraOperator;
    }
    public void startPreview() {
        UtilLog.i(TAG, "startPreview");
        cameraInterfaces.startPreview();
    }
    public void pausePreview() {
        UtilLog.i(TAG, "pausePreview");
        cameraInterfaces.stopPreview();
    }

    public void resumePreview() {
        UtilLog.i(TAG, "resumePreview");
        cameraInterfaces.startPreview();
    }

    public void stopPreview() {
        UtilLog.i(TAG, "stopPreview");
        cameraInterfaces.stopPreview();
    }
    public void releasePreview() {
        UtilLog.i(TAG, "releasePreview");
        cameraInterfaces.release();
    }

    public void setCameraPreview(CameraPreview cameraPreview) {
        this.cameraPreview = cameraPreview;
    }
}
