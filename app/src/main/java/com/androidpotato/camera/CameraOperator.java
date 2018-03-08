package com.androidpotato.camera;


import com.davidzhou.library.util.LogUtil;

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
        LogUtil.i(TAG, "startPreview");
        cameraInterfaces.startPreview();
    }
    public void pausePreview() {
        LogUtil.i(TAG, "pausePreview");
        cameraInterfaces.stopPreview();
    }

    public void resumePreview() {
        LogUtil.i(TAG, "resumePreview");
        cameraInterfaces.startPreview();
    }

    public void stopPreview() {
        LogUtil.i(TAG, "stopPreview");
        cameraInterfaces.stopPreview();
    }
    public void releasePreview() {
        LogUtil.i(TAG, "releasePreview");
        cameraInterfaces.release();
    }

    public void setCameraPreview(CameraPreview cameraPreview) {
        this.cameraPreview = cameraPreview;
    }
}
