package com.example.hp.myapplication1.info;

import android.app.Activity;
import android.app.AlertDialog;

import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.Utils.DateTransUtils;
import com.example.hp.myapplication1.db.UsagePOJO;
import com.example.hp.myapplication1.Utils.UseTimeDataManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUsageInfo implements ListItemsManager{
    private Activity act;
    private int recentDays;

    public AppUsageInfo(Activity act, int recentDays){
        this.recentDays = recentDays;
        this.act = act;
    }

    @Override
    public List<Map<String ,Object>> getItemList(List<Map<String ,Object>> mapList){
        mapList.clear();
        UseTimeDataManager mUseTimeDataManager  = UseTimeDataManager.getInstance(act);
        mUseTimeDataManager.refreshData2(recentDays);
        List<UsagePOJO> usagePOJOS = mUseTimeDataManager.getmPackageInfoListOrderByTime();
        for (int i = 0; i < usagePOJOS.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            if(usagePOJOS.get(i).getmUsedCount()<1)
                continue;
            map.put("count", "移到前台"+usagePOJOS.get(i).getmUsedCount()+"次");
            map.put("PackageName", usagePOJOS.get(i).getmPackageName());
            map.put("time", DateTransUtils.timeToHMS(usagePOJOS.get(i).getmUsedTime()));
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

    @Override
    public void itemOnClicked(List<Map<String,Object>> listItems, long id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.act);
//        builder.setIcon((Drawable) listItems.get((int)id).get("imageID"));
        builder.setTitle((String)listItems.get((int)id).get(dataFrom()[0]));
        builder.setMessage("使用次数："+listItems.get((int)id).get(dataFrom()[1])+
                "\n运行时间："+listItems.get((int)id).get(dataFrom()[2])+
                "\n包名："+listItems.get((int)id).get(dataFrom()[3]));
        builder.setPositiveButton("确定", null);
        builder.show();
    }
}
