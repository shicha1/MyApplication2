package com.example.hp.myapplication1.info;

import android.app.Activity;
import android.app.usage.UsageEvents;

import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.Utils.DateTransUtils;
import com.example.hp.myapplication1.Utils.UseTimeDataManager;
import com.example.hp.myapplication1.db.UsagePOJO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUsageQueueInfo implements ListItemsManager{
    private Activity act;
    private int recentDays;

    public AppUsageQueueInfo(Activity act, int recentDays){
        this.act = act;
        this.recentDays = recentDays;
    }

    @Override
    public List<Map<String ,Object>> getItemList(List<Map<String ,Object>> mapList){
        mapList.clear();
        UseTimeDataManager usageQueue  = UseTimeDataManager.getInstance(act);
        usageQueue.refreshData(recentDays);
        for(UsageEvents.Event event : usageQueue.getmEventListChecked()){
            Map<String,Object> map = new HashMap<>();
            map.put("imageID",usageQueue.getAppIconByPackageName(act,event.getPackageName()));
            map.put("info",usageQueue.getApplicationNameByPackageName(act,event.getPackageName()));
            map.put("openTime", DateTransUtils.stampToDate(event.getTimeStamp()));
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public List<Map<String, Object>> itemListUpdate(List<Map<String, Object>> mapList) {
        getItemList(mapList);
        return mapList;
    }

    @Override
    public List<Map<String, Object>> itemListLoadMore(List<Map<String, Object>> mapList) {
        return mapList;
    }

    @Override
    public String[] dataFrom(){
        return new String[]{"imageID","info","openTime"};
    }

    @Override
    public int[] dataTo(){
        return new int[]{R.id.list_item_install_image,R.id.list_item_install_name,R.id.list_item_install_openTime};
    }
}
