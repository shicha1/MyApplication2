package com.example.hp.myapplication1.info;

import android.app.Activity;
import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.db.DbHelper;
import java.util.List;
import java.util.Map;

public class AllUserInfo {
    private Activity act;
    private DbHelper dbHelper;

    public AllUserInfo(Activity act){
        this.act = act;
        this.dbHelper = new DbHelper(act);
    }

    public List<Map<String,Object>> getAllUser(){
        return dbHelper.queryALL();
    }

    public String[] dataFrom(){
        return dbHelper.getTBL1_all_name();
    }

    public int[] dataTo(){
        return new int[]{R.id.list_item_user_id,R.id.list_item_user_pwd,R.id.list_item_user_type};
    }
}
