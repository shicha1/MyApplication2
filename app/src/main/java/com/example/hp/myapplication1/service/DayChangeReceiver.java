package com.example.hp.myapplication1.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.hp.myapplication1.Utils.UseTimeDataManager;
import com.example.hp.myapplication1.db.DbHelper;
import com.example.hp.myapplication1.db.UsagePOJO;

import java.util.List;

public class DayChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DbHelper dbHelper = new DbHelper(context);
        UseTimeDataManager mUseTimeDataManager  = UseTimeDataManager.getInstance(context);
        mUseTimeDataManager.refreshData(0);
        List<UsagePOJO> usagePOJOS = mUseTimeDataManager.getmPackageInfoListOrderByTime();
        Log.e("ser","receiver");
        for(UsagePOJO usagePOJO:usagePOJOS){
            if(!usagePOJO.getmAppName().equals(""))
                dbHelper.updateApp(usagePOJO);
        }
    }
}
