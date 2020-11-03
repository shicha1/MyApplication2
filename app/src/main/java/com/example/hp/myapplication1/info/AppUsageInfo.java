package com.example.hp.myapplication1.info;

import android.app.Activity;

import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.db.UsagePOJO;
import com.example.hp.myapplication1.Utils.UseTimeDataManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUsageInfo implements ListItemsManager{
    private Activity act;

    public AppUsageInfo(Activity act){
        this.act = act;
    }

    @Override
    public List<Map<String ,Object>> getItemList(List<Map<String ,Object>> mapList){
        mapList.clear();
        UseTimeDataManager mUseTimeDataManager  = UseTimeDataManager.getInstance(act);
        mUseTimeDataManager .refreshData(0);
        List<UsagePOJO> usagePOJOS = mUseTimeDataManager.getmPackageInfoListOrderByTime();
        for (int i = 0; i < usagePOJOS.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("count", usagePOJOS.get(i).getmUsedCount());
            map.put("PackageName", usagePOJOS.get(i).getmPackageName());
            map.put("time", usagePOJOS.get(i).getmUsedTime());
            map.put("AppName", usagePOJOS.get(i).getmAppName());
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
        return new String[]{"AppName","count","time","PackageName"};
    }

    @Override
    public int[] dataTo(){
        return new int[]{R.id.list_item_usage_name,
        R.id.list_item_usage_count,
        R.id.list_item_usage_time,
        R.id.list_item_usage_package_name,
        };
    }
}
