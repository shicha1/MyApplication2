package com.example.hp.myapplication1.info;

import android.app.Activity;
import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.db.DbHelper;
import java.util.List;
import java.util.Map;

public class AllUserInfo implements ListItemsManager{
    private Activity act;
    private DbHelper dbHelper;

    public AllUserInfo(Activity act){
        this.act = act;
        this.dbHelper = new DbHelper(act);
    }

    @Override
    public List<Map<String,Object>> getItemList(List<Map<String,Object>> userList){
        userList.clear();
        return dbHelper.queryALL(userList);
    }

    @Override
    public List<Map<String, Object>> itemListLoadMore(List<Map<String, Object>> mapList) {
        return mapList;
    }

    @Override
    public List<Map<String, Object>> itemListUpdate(List<Map<String, Object>> mapList) {
        getItemList(mapList);
        return mapList;
    }

    @Override
    public String[] dataFrom(){
        return dbHelper.getTBL1_all_name();
    }

    @Override
    public int[] dataTo(){
        return new int[]{R.id.list_item_user_id,R.id.list_item_user_pwd,R.id.list_item_user_type};
    }
}
