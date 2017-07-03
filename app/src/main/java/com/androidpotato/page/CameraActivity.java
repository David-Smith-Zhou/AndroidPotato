package com.androidpotato.page;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


import com.androidpotato.R;
import com.androidpotato.camera.CameraInterfaces;
import com.androidpotato.camera.CameraPreview;
import com.androidpotato.mylibrary.util.UtilLog;


/**
 * Created by DavidSmith on 2016/6/18 0018.
 */
public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private CameraPreview mCameraPreview;
    private FrameLayout mFrameLayout;
    private CameraInterfaces mCameraInterfaces;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_camera_activity);
        int cameras = Camera.getNumberOfCameras();
        UtilLog.i(TAG, "number of mCamera is: " + cameras);
        initViews();
    }
    private void initViews() {
        mFrameLayout = (FrameLayout) this.findViewById(R.id.page_camera_fl);
        mCameraInterfaces = CameraInterfaces.getInstance();
        mCameraInterfaces.setCameraParameters(mFrameLayout);
        mCameraPreview = new CameraPreview(CameraActivity.this, mCameraInterfaces.getCamera(), callback);
    }
    private Camera.PreviewCallback callback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            UtilLog.i(TAG, "go into onPreviewFrame");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mCameraInterfaces.startPreview();
        mFrameLayout.addView(mCameraPreview);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraInterfaces.stopPreview();
        mCameraPreview.release();
        mCameraInterfaces.release();
        mFrameLayout.removeView(mCameraPreview);

    }

    @Override
    public void finish() {
        super.finish();
//        if (mCamera != null) {
//            mCamera.release();
//            mCamera = null;
//        }
    }
}
