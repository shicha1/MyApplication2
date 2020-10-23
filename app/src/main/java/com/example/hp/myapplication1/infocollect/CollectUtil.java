package com.example.hp.myapplication1.infocollect;

import android.app.ActivityManager;
import android.content.Context;

import com.example.hp.myapplication1.MainActivity;

import java.util.List;

public class CollectUtil {
    public void getAppInstalled(MainActivity act){
        ActivityManager actManager = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);
        List runningAppList = actManager.getRunningAppProcesses();

    }
}
