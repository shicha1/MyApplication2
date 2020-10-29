package com.example.hp.myapplication1.info;

import android.app.Activity;

import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.db.UsagePOJO;
import com.example.hp.myapplication1.Utils.UseTimeDataManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AppUsageInfo {
    private Activity act;

    public AppUsageInfo(Activity act){
        this.act = act;
    }

    public List<Map<String ,Object>> getAppUsage(){
        List<Map<String ,Object>> mapList = new LinkedList<>();
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

    public String[] dataFrom(){
        return new String[]{"AppName","count","time","PackageName"};
    }

    public int[] dataTo(){
        return new int[]{R.id.list_item_usage_name,
        R.id.list_item_usage_count,
        R.id.list_item_usage_time,
        R.id.list_item_usage_package_name,
        };
    }
}
