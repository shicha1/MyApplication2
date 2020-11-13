package com.example.hp.myapplication1.info;

import android.app.Activity;
import android.app.AlertDialog;

import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.db.DbHelper;
import java.util.List;
import java.util.Map;

public class AppUsageFLRunInfo implements ListItemsManager {
    private Activity act;

    public AppUsageFLRunInfo(Activity act){
        this.act = act;
    }

    @Override
    public List<Map<String ,Object>> getItemList(List<Map<String ,Object>> mapList){
        mapList.clear();
        return new DbHelper(act).queryAppFLRunALl(mapList);
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
        return new String[]{DbHelper.TBL_NAME2_COL1,DbHelper.TBL_NAME2_COL2,DbHelper.TBL_NAME2_COL3};
    }

    @Override
    public int[] dataTo(){
        return new int[]{R.id.list_item_usage_name,
                R.id.list_item_usage_count,
                R.id.list_item_usage_time,
        };
    }

    @Override
    public void itemOnClicked(List<Map<String,Object>> listItems, long id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.act);
        builder.setTitle((String)listItems.get((int)id).get(dataFrom()[0]));
        builder.setMessage("可知最早启动时间："+listItems.get((int)id).get(dataFrom()[1])+
                "\n最后一次运行时间："+listItems.get((int)id).get(dataFrom()[2]));
        builder.setPositiveButton("确定", null);
        builder.show();
    }
}
