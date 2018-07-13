package com.androidpotato.utils;

import android.content.Context;

import com.davidzhou.library.util.ULog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HashMapToFileUtil {
    private static final String TAG = "HashMapToFileUtil";
    private static final String CODED_FORMAT = "GBK";
    private static final int BUFFER_SIZE_READ_BYTE = 4 * 1024;
    private static HashMapToFileUtil mHashMapToFileUtil;

    private HashMapToFileUtil() {

    }

    public synchronized static HashMapToFileUtil getInstance() {
        if (mHashMapToFileUtil == null) {
            mHashMapToFileUtil = new HashMapToFileUtil();
        }
        return mHashMapToFileUtil;
    }

    private String getCacheDirPath(Context context) {
        return context.getExternalCacheDir().getPath();
    }

    public void writeToFile(Context context, HashMap<String, Boolean> hashMap) {
        if (hashMap == null || hashMap.isEmpty()) {
            ULog.e(TAG, "hashMap is null or empty");
            return;
        }
        File file = new File(getRecordFilePath(context));
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    ULog.d(TAG, "create file success");
                } else {
                    ULog.e(TAG, "create file failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                Iterator<Map.Entry<String, Boolean>> iterator = hashMap.entrySet().iterator();
                List<RecordInfo> recordInfos = new ArrayList<>();
                while (iterator.hasNext()) {
                    RecordInfo info = new RecordInfo();
                    Map.Entry<String, Boolean> entry = iterator.next();
                    info.setKey(entry.getKey());
                    info.setValue(entry.getValue());
                    recordInfos.add(info);
                }
                Gson gson = new Gson();
                String writeData = gson.toJson(recordInfos);
                ULog.i(TAG, "writeData: " + writeData);
                fos.write(writeData.getBytes(CODED_FORMAT));
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getRecordFilePath(Context context) {
        return getCacheDirPath(context) + "/record";
    }

    public HashMap<String, Boolean> getHashMap(Context context) {
        HashMap<String, Boolean> hashMap = new HashMap<>();
        File record = new File(getRecordFilePath(context));
        byte[] buffer = new byte[BUFFER_SIZE_READ_BYTE];
        if (record.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(record);
                int readCount = fis.read(buffer);
                if (readCount <= BUFFER_SIZE_READ_BYTE) {
                    byte[] dataBytes = new byte[readCount];
                    System.arraycopy(buffer, 0, dataBytes, 0, readCount);
                    String rstStr = new String(dataBytes, CODED_FORMAT);
                    Gson gson = new Gson();
                    List<RecordInfo> records = gson.fromJson(rstStr, new TypeToken<List<RecordInfo>>() {
                    }.getType());
                    Iterator iterator = records.iterator();
                    while (iterator.hasNext()) {
                        RecordInfo info = (RecordInfo) iterator.next();
                        hashMap.put(info.key, info.value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return hashMap;
    }

    private class RecordInfo {
        private String key;
        private boolean value;

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public boolean getValue() {
            return value;
        }
    }
}
