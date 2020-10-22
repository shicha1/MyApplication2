package com.example.hp.myapplication1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DnHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "databaseName";
    private static final String TBL_NAME1 = "logUser";
    private static final String TBL_NAME1_COL1 = "ID";
    private static final String TBL_NAME1_COL2 = "PWD";
    private static final String TBL_NAME1_COL3 = "TYPE";
    StringBuilder sql = new StringBuilder();

    DnHelper(Context context){
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
        sql.append("(");
        sql.append(TBL_NAME1_COL1);
        sql.append(" CHAR(25) PRIMARY KEY NOT NULL,");
        sql.append(TBL_NAME1_COL2);
        sql.append(" TEXT NOT NULL,");
        sql.append(TBL_NAME1_COL3);
        sql.append(" INT NOT NULL)");
        insert("123","123",1);
        insert("123","123",2);
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insert(String ID, String string, Integer type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TBL_NAME1_COL1,ID);
        cv.put(TBL_NAME1_COL2,string);
        cv.put(TBL_NAME1_COL3,type);
        return db.insert(TBL_NAME1,null,cv)>0;
    }

    public Cursor query(String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        sql.delete(0,sql.length());
        sql.append("SELECT * FROM ");
        sql.append(TBL_NAME1);
        sql.append(" where ");
        sql.append(TBL_NAME1_COL1);
        sql.append("=?");
        Cursor cur = db.rawQuery(sql.toString(), new String[]{ID});
        if(!cur.isClosed())
            cur.close();
        return cur;
    }

    public Boolean delete(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        sql.delete(0,sql.length());
        sql.append(TBL_NAME1_COL1);
        sql.append("=?");
        return db.delete(TBL_NAME1,sql.toString(),new String[]{ID})>0;
    }
}
