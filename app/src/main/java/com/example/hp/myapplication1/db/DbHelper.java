package com.example.hp.myapplication1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hp.myapplication1.Utils.DateTransUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "databaseName.db";
    public static final String TBL_NAME1 = "logUser";
    public static final String TBL_NAME1_COL1 = "ID";
    public static final String TBL_NAME1_COL2 = "PWD";
    public static final String TBL_NAME1_COL3 = "TYPE";
    public static final String TBL_NAME2 = "appInfo";
    public static final String TBL_NAME2_COL1 = "name";
    public static final String TBL_NAME2_COL2 = "firstRun";
    public static final String TBL_NAME2_COL3 = "lastRun";
    StringBuilder sql = new StringBuilder();

    public DbHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//         sqlExample: CREATE TABLE TABLE_NAME(ID INT PRIMARY KEY NOT NULL,
//         VAR_NAME CHAR(20) NOT NULL,
//         ...))
        sql.delete(0,sql.length());
        sql.append("CREATE TABLE ");
        sql.append(TBL_NAME1);
        sql.append(" (");
        sql.append(TBL_NAME1_COL1);
        sql.append(" CHAR(25) PRIMARY KEY,");
        sql.append(TBL_NAME1_COL2);
        sql.append(" TEXT,");
        sql.append(TBL_NAME1_COL3);
        sql.append(" INTEGER)");
        Log.i("sql",sql.toString());
        db.execSQL(sql.toString());

        sql.delete(0,sql.length());
        sql.append("CREATE TABLE ");
        sql.append(TBL_NAME2);
        sql.append(" (");
        sql.append(TBL_NAME2_COL1);
        sql.append(" CHAR(25) PRIMARY KEY,");
        sql.append(TBL_NAME2_COL2);
        sql.append(" INTEGER,");
        sql.append(TBL_NAME2_COL3);
        sql.append(" INTEGER)");
        Log.i("sql",sql.toString());
        db.execSQL(sql.toString());

        ContentValues cv = new ContentValues();
        cv.put(TBL_NAME1_COL1,"123");
        cv.put(TBL_NAME1_COL2,"123");
        cv.put(TBL_NAME1_COL3,1);
        if(db.insert(TBL_NAME1,null,cv)<0)
            Log.w("数据库初始化失败","初始账户插入失败");
        cv.clear();
        cv.put(TBL_NAME1_COL1,"1234");
        cv.put(TBL_NAME1_COL2,"1234");
        cv.put(TBL_NAME1_COL3,2);
        if(db.insert(TBL_NAME1,null,cv)<0)
            Log.w("数据库初始化失败","初始账户插入失败");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertUser(String ID, String string, Integer type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TBL_NAME1_COL1,ID);
        cv.put(TBL_NAME1_COL2,string);
        cv.put(TBL_NAME1_COL3,type);
        return db.insert(TBL_NAME1,null,cv)>0;
    }

    public UserPOJO queryUserByID(String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        sql.delete(0,sql.length());
        sql.append("SELECT * FROM ");
        sql.append(TBL_NAME1);
        sql.append(" where ");
        sql.append(TBL_NAME1_COL1);
        sql.append("=?");
        Cursor cur = db.rawQuery(sql.toString(), new String[]{ID});
        UserPOJO userPOJO = new UserPOJO();
        if(cur.moveToFirst()){
            userPOJO.setUserID(ID);
            userPOJO.setPwd(cur.getString(cur.getColumnIndex(TBL_NAME1_COL2)));
            userPOJO.setUserType(cur.getInt(cur.getColumnIndex(TBL_NAME1_COL3)));
        }
        if(!cur.isClosed())
            cur.close();
        return userPOJO;
    }

    public List<Map<String,Object>> queryUserALL(List<Map<String,Object>> mapList){
        SQLiteDatabase db = this.getReadableDatabase();
        sql.delete(0,sql.length());
        sql.append("SELECT * FROM ");
        sql.append(TBL_NAME1);
        Cursor cur = db.rawQuery(sql.toString(), null);
        while (cur.moveToNext()){
            Map<String,Object> map = new HashMap<>();
            map.put(TBL_NAME1_COL1,cur.getString(cur.getColumnIndex(TBL_NAME1_COL1)));
            map.put(TBL_NAME1_COL2,cur.getString(cur.getColumnIndex(TBL_NAME1_COL2)));
            map.put(TBL_NAME1_COL3,cur.getString(cur.getColumnIndex(TBL_NAME1_COL3)));
            mapList.add(map);
        }
        if(!cur.isClosed())
            cur.close();
        return mapList;
    }

    public List<Map<String,Object>> queryAppFLRunALl(List<Map<String,Object>> mapList){
        if (mapList==null)
            mapList = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        sql.delete(0,sql.length());
        sql.append("SELECT * FROM ");
        sql.append(TBL_NAME2);
        Cursor cur = db.rawQuery(sql.toString(), null);
        while (cur.moveToNext()){
            Map<String,Object> map = new HashMap<>();
            map.put(TBL_NAME2_COL1,cur.getString(cur.getColumnIndex(TBL_NAME2_COL1)));
            if(cur.getString(cur.getColumnIndex(TBL_NAME2_COL2)).equals("0")){
                map.put(TBL_NAME2_COL2,"未记录");
            }else{
                map.put(TBL_NAME2_COL2, DateTransUtils.stampToDate(cur.getString(cur.getColumnIndex(TBL_NAME2_COL2))));
            }
            map.put(TBL_NAME2_COL3,DateTransUtils.stampToDate(cur.getString(cur.getColumnIndex(TBL_NAME2_COL3))));
            mapList.add(map);
        }
        if(!cur.isClosed())
            cur.close();
        return mapList;
    }

    public boolean insertApp(UsagePOJO appFLRunPOJO){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TBL_NAME2_COL1,appFLRunPOJO.getmAppName());
        cv.put(TBL_NAME2_COL2,appFLRunPOJO.getFirstRun());
        cv.put(TBL_NAME2_COL3,appFLRunPOJO.getLastRun());
        return db.insert(TBL_NAME2,null,cv)>0;
    }

    public boolean updateApp(UsagePOJO appFLRunPOJO){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TBL_NAME2_COL3,appFLRunPOJO.getLastRun());
        return db.update(TBL_NAME2,cv,TBL_NAME1_COL1+"=?",new String[]{appFLRunPOJO.getmAppName()})>0;
    }

    public String[] getTBL1_all_name(){
        return new String[]{TBL_NAME1_COL1,TBL_NAME1_COL2,TBL_NAME1_COL3};
    }

    public Boolean delete(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        sql.delete(0,sql.length());
        sql.append(TBL_NAME1_COL1);
        sql.append("=?");
        return db.delete(TBL_NAME1,sql.toString(),new String[]{ID})>0;
    }
}
