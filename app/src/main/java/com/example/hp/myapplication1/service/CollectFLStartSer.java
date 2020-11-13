package com.example.hp.myapplication1.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.hp.myapplication1.Utils.UseTimeDataManager;
import com.example.hp.myapplication1.db.DbHelper;
import com.example.hp.myapplication1.db.UsagePOJO;

import java.util.List;

public class CollectFLStartSer extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        final DbHelper dbHelper = new DbHelper(this);
        if(dbHelper.queryAppFLRunALl(null).size() == 0)
            new Thread(){
                @Override
                public void run() {
                    firstQuery(dbHelper);
                }
            }.start();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        DayChangeReceiver dayChangeReceiver = new DayChangeReceiver();
        registerReceiver(dayChangeReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ser","done");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void firstQuery(DbHelper dbHelper){
        UseTimeDataManager mUseTimeDataManager  = UseTimeDataManager.getInstance(this);
        mUseTimeDataManager.refreshData2(30);
        List<UsagePOJO> usagePOJOS = mUseTimeDataManager.getmUsagePOJOList();
        for (int i = 0; i < usagePOJOS.size(); i++) {
            if (!usagePOJOS.get(i).getmAppName().equals(""))
                dbHelper.insertApp(usagePOJOS.get(i));
        }
    }
}
