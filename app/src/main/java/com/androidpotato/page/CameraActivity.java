package com.androidpotato.page;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.androidpotato.R;
import com.androidpotato.camera.CameraOperator;
import com.androidpotato.camera.CameraPreview;
import com.androidpotato.mylibrary.util.UtilLog;


/**
 * Created by DavidSmith on 2016/6/18 0018.
 */
public class CameraActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "CameraActivity";
    private CameraPreview mCameraPreview;
    private CameraOperator cameraOperator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_camera);
        int cameras = Camera.getNumberOfCameras();
        UtilLog.i(TAG, "number of mCamera is: " + cameras);
        initViews();
    }
    private void initViews() {
        cameraOperator = CameraOperator.getInstance();
        mCameraPreview = (CameraPreview) this.findViewById(R.id.page_camera_cp);
        cameraOperator.setCameraPreview(mCameraPreview);
        mCameraPreview.setPreviewCallback(callback);
    }
    private Camera.PreviewCallback callback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            UtilLog.i(TAG, "go into onPreviewFrame");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_camera_startPhoto_Btn:
                cameraOperator.startPreview();
                break;
            case R.id.content_camera_stopCamera_Btn:
                cameraOperator.stopPreview();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // do not need startPreview because when the surface is visible, surface will call surface.create method
        // where startPreview automatic
//        cameraOperator.resumePreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraOperator.pausePreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraOperator.releasePreview();
    }
}
