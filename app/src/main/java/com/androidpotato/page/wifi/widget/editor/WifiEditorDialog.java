package com.androidpotato.page.wifi.widget.editor;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidpotato.R;
import com.androidpotato.page.wifi.adapter.ScanResultInfo;
import com.davidzhou.library.util.WifiUtil;


/**
 * Created by David on 2017/12/12 0012.
 */

public class WifiEditorDialog extends Dialog {
    private static final String TAG = "WifiEditorDialog";
    private ScanResultInfo info;
    private OnWifiEditorDialogCallback onWifiEditorDialogCallback;


    public WifiEditorDialog(Context context, ScanResultInfo info) {
        super(context);
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editor);
        init();
    }

    private void init() {
        TextView firstLine = (TextView) findViewById(R.id.dialog_editor_first_line_tv);
        TextView secondLine = (TextView) findViewById(R.id.dialog_editor_second_line_tv);
        Button confirmBtn = (Button) this.findViewById(R.id.dialog_editor_confirm_btn);
        Button noSavingBtn = (Button) this.findViewById(R.id.dialog_editor_no_saving_btn);
        Button cancelBtn = (Button) this.findViewById(R.id.dialog_editor_cancel_btn);
        if (info.isConnect()) {
            //强度和速度
            this.setTitle(info.getWifiInfo().getSSID().substring(1, info.getWifiInfo().getSSID().length() - 1));
            WifiInfo info = this.info.getWifiInfo();
            confirmBtn.setText(getContext().getString(R.string.disconnect));
            firstLine.setText(this.getContext().getString(R.string.item_rssi_str,
                    this.getContext().getString(R.string.rssi),
                    WifiUtil.getRssiLevel(this.getContext(), info.getRssi())));
            secondLine.setText(this.getContext().getString(R.string.wifi_deitor_speed,
                    info.getLinkSpeed()));
        } else {
            // 强度和安全性
            this.setTitle(info.getScanResult().SSID);
            confirmBtn.setText(getContext().getString(R.string.connect));
            firstLine.setText(this.getContext().getString(R.string.item_rssi_str,
                    this.getContext().getString(R.string.rssi),
                    WifiUtil.getRssiLevel(this.getContext(), this.info.getScanResult().level)));
            secondLine.setText(this.getContext().getString(R.string.item_auth_type_str,
                    this.getContext().getString(R.string.auth_type),
                    WifiUtil.getSupportEncryptType(this.getContext(), this.info.getScanResult().capabilities)));
        }
        noSavingBtn.setText(getContext().getString(R.string.wifi_editor_no_saving));
        cancelBtn.setText(getContext().getString(R.string.cancel));

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onWifiEditorDialogCallback != null) {
                    onWifiEditorDialogCallback.onConfirm(info);
                }
            }
        });
        noSavingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onWifiEditorDialogCallback != null) {
                    onWifiEditorDialogCallback.onNoSaving(info);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onWifiEditorDialogCallback != null) {
                    onWifiEditorDialogCallback.onCancel();
                }
            }
        });
    }

    public void setOnWifiEditorDialogCallback(OnWifiEditorDialogCallback onWifiEditorDialogCallback) {
        this.onWifiEditorDialogCallback = onWifiEditorDialogCallback;
    }

    public interface OnWifiEditorDialogCallback {
        void onConfirm(ScanResultInfo info);

        void onNoSaving(ScanResultInfo info);

        void onCancel();
    }
}
