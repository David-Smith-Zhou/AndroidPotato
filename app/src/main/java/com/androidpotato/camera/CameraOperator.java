package com.androidpotato.camera;


import com.davidzhou.library.util.ULog;

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
        ULog.i(TAG, "startPreview");
        cameraInterfaces.startPreview();
    }
    public void pausePreview() {
        ULog.i(TAG, "pausePreview");
        cameraInterfaces.stopPreview();
    }

    public void resumePreview() {
        ULog.i(TAG, "resumePreview");
        cameraInterfaces.startPreview();
    }

    public void stopPreview() {
        ULog.i(TAG, "stopPreview");
        cameraInterfaces.stopPreview();
    }
    public void releasePreview() {
        ULog.i(TAG, "releasePreview");
        cameraInterfaces.release();
    }

    public void setCameraPreview(CameraPreview cameraPreview) {
        this.cameraPreview = cameraPreview;
    }
}
