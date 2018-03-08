package com.androidpotato.page.wifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidpotato.R;
import com.davidzhou.library.util.WifiUtil;

import java.util.List;

/**
 * Created by David on 2017/6/5 0005.
 */

public class WifiItemAdapter extends BaseAdapter {
    private static final String TAG = "WifiItemAdapter";
    private LayoutInflater inflater;
    private List<ScanResultInfo> list;

    public WifiItemAdapter(Context context, List<ScanResultInfo> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_wifi, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.item_wifi_name);
            viewHolder.authTypeTV = (TextView) convertView.findViewById(R.id.item_wifi_authType);
            viewHolder.rssiTV = (TextView) convertView.findViewById(R.id.item_wifi_rssi);
            viewHolder.bssidTV = (TextView) convertView.findViewById(R.id.item_wifi_mac);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list.size() != 0) {
            ScanResultInfo result = list.get(position);
            if (list.get(position).isConnect()) {
                setConnectedItemView(convertView, viewHolder, result, convertView.getResources().getString(R.string.wifi_status_connected));
            } else if (list.get(position).isSaved()) {
                setOtherItemView(convertView, viewHolder, result, convertView.getResources().getString(R.string.wifi_status_saved));
            } else {
                setOtherItemView(convertView, viewHolder, result, convertView.getResources().getString(R.string.wifi_status_saved));
                viewHolder.nameTV.setText(convertView.getResources().getString(R.string.item_name, result.getScanResult().SSID));
                viewHolder.authTypeTV.setText(convertView.getResources().getString(R.string.item_auth_type_str,
                        convertView.getResources().getString(R.string.auth_type),
                        WifiUtil.getSupportEncryptType(convertView.getContext(), result.getScanResult().capabilities)));
                viewHolder.rssiTV.setText(convertView.getResources().getString(R.string.item_rssi_str, convertView.getResources().getString(R.string.rssi),
                        WifiUtil.getRssiLevel(convertView.getContext(), result.getScanResult().level)));
                viewHolder.bssidTV.setText(convertView.getResources().getString(R.string.item_mac,
                        result.getScanResult().BSSID.toUpperCase()));
            }

        }
        return convertView;
    }

    private void setConnectedItemView(View view, ViewHolder viewHolder, ScanResultInfo result, String str) {
        viewHolder.authTypeTV.setText(str);
        viewHolder.nameTV.setText(view.getResources().getString(R.string.item_name, result.getWifiInfo()
                .getSSID().substring(1, result.getWifiInfo().getSSID().length() - 1)));
        viewHolder.rssiTV.setText(view.getResources().getString(R.string.item_rssi_str, view.getResources().getString(R.string.rssi),
                WifiUtil.getRssiLevel(view.getContext(), result.getWifiInfo().getRssi())));
        viewHolder.bssidTV.setText(view.getResources().getString(R.string.item_mac,
                result.getWifiInfo().getBSSID().toUpperCase()));
    }
    private void setOtherItemView(View view, ViewHolder viewHolder, ScanResultInfo result, String str) {
        viewHolder.nameTV.setText(view.getResources().getString(R.string.item_name, result.getScanResult().SSID));
        viewHolder.authTypeTV.setText(str);
        viewHolder.rssiTV.setText(view.getResources().getString(R.string.item_rssi_str, view.getResources().getString(R.string.rssi),
                WifiUtil.getRssiLevel(view.getContext(), result.getScanResult().level)));
        viewHolder.bssidTV.setText(view.getResources().getString(R.string.item_mac,
                result.getScanResult().BSSID.toUpperCase()));
    }

    private class ViewHolder {
        public TextView nameTV;
        public TextView authTypeTV;
        public TextView rssiTV;
        public TextView bssidTV;
    }

}
