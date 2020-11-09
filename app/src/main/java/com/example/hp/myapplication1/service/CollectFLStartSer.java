package com.example.hp.myapplication1.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.hp.myapplication1.Utils.UseTimeDataManager;
import com.example.hp.myapplication1.db.DbHelper;
import com.example.hp.myapplication1.db.UsagePOJO;

import java.util.List;

public class CollectFLStartSer extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        DbHelper dbHelper = new DbHelper(this);
        if(dbHelper.queryAppFLRunALl(null).size() == 0)
            firstQuery(dbHelper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void firstQuery(DbHelper dbHelper){
        UseTimeDataManager mUseTimeDataManager  = UseTimeDataManager.getInstance(this);
        mUseTimeDataManager.refreshData(30);
        List<UsagePOJO> usagePOJOS = mUseTimeDataManager.getmPackageInfoListOrderByTime();
        for (int i = 0; i < usagePOJOS.size(); i++) {
            dbHelper.insertApp(usagePOJOS.get(i));
        }
    }
}
