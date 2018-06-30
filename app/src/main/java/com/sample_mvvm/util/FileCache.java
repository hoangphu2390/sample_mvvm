package com.sample_mvvm.util;

import android.content.Context;

import java.io.File;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        final String appName = context.getString(stringId);
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "/." + appName);
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getCacheDir() {
        return cacheDir;
    }
}