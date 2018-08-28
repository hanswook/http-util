package com.common.utils.http_utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MetaDataUtils {
    private static final String STRING_PRE = "string:";

    public static String getString(Context context, String name) throws PackageManager.NameNotFoundException {
        Object obj = getObject(context, name);
        if (obj == null) {
            return null;
        }
        String res = String.valueOf(obj);
        if (res.startsWith(STRING_PRE)) {
            res = res.substring(STRING_PRE.length(), res.length());
        }
        return res;
    }

    public static long getLong(Context context, String name) throws PackageManager.NameNotFoundException {
        String metaData = getString(context, name);
        if (metaData == null) {
            return 0L;
        }
        return Long.valueOf(metaData);
    }

    public static int getInt(Context context, String name) throws PackageManager.NameNotFoundException {
        String metaData = getString(context, name);
        if (metaData == null) {
            return 0;
        }
        return Integer.valueOf(metaData);
    }

    public static boolean getBoolean(Context context, String name) throws PackageManager.NameNotFoundException {
        String metaData = getString(context, name);
        if (metaData == null) {
            return false;
        }
        return Boolean.valueOf(metaData);
    }

    public static Object getObject(Context context, String name) throws PackageManager.NameNotFoundException {
        Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
        return metaData.get(name);
    }

}