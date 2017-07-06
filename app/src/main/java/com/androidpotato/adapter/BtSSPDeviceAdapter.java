package com.androidpotato.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidpotato.R;

import java.util.List;

/**
 * Created by David on 2017/7/6 0006.
 */

public class BtSSPDeviceAdapter extends BaseAdapter {
    private List<BluetoothDevice> devices;
    private Context context;

    public BtSSPDeviceAdapter(Context context, List<BluetoothDevice> devices) {
        this.context = context;
        this.devices = devices;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView deviceName, deviceMac;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_item_bt_ssp_device, null);
            deviceName = (TextView) convertView.findViewById(R.id.adapter_bt_ssp_device_name);
            deviceMac = (TextView) convertView.findViewById(R.id.adapter_bt_ssp_device_mac);
            deviceName.setText(devices.get(position).getName());
            deviceMac.setText(devices.get(position).getAddress());
            viewHolder = new ViewHolder();
            viewHolder.setDeviceMacTv(deviceMac);
            viewHolder.setDeviceNameTv(deviceName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            deviceName = viewHolder.getDeviceNameTv();
            deviceMac = viewHolder.getDeviceMacTv();
        }
        deviceName.setText(devices.get(position).getName());
        deviceMac.setText(devices.get(position).getAddress());
        return null;
    }

    private class ViewHolder {
        private TextView deviceNameTv;
        private TextView deviceMacTv;

        public void setDeviceMacTv(TextView deviceMacTv) {
            this.deviceMacTv = deviceMacTv;
        }

        public void setDeviceNameTv(TextView deviceNameTv) {
            this.deviceNameTv = deviceNameTv;
        }

        public TextView getDeviceMacTv() {
            return deviceMacTv;
        }

        public TextView getDeviceNameTv() {
            return deviceNameTv;
        }
    }
}
