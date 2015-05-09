package com.mehdiii.duelgame.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by omid on 4/25/2015.
 */
public class DeviceManager {
    public static String getDeviceId(Context context) {
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        final String deviceId, simSerialNumber;
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        deviceId = "" + teleManager.getDeviceId();
        simSerialNumber = "" + teleManager.getSimSerialNumber();
        return android_id + deviceId + simSerialNumber;
    }
}
