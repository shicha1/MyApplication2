package com.example.hp.myapplication1.info;
import android.app.Activity;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Map;
import com.example.hp.myapplication1.R;


public class AppInstalledInfo implements ListItemsManager{
    private Activity act;

    public AppInstalledInfo(Activity act){
        this.act = act;
    }

    @Override
    public List<Map<String,Object>> getItemList(List<Map<String,Object>> mapList){
        mapList.clear();
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager packageManager = act.getPackageManager();
        List<PackageInfo> pkgLists = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : pkgLists) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {// 这是为了过滤系统应用
                apps.add(packageInfo);
                String str=applicationInfo.loadLabel(packageManager).toString();
//                Log.d("pin", "applicationInfo.packageName->" + applicationInfo.packageName);
            }
        }
        for(int i =0; i <apps.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("imageID",packageManager.getApplicationIcon(apps.get(i).applicationInfo));
            map.put("info",apps.get(i).applicationInfo.loadLabel(packageManager).toString());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public String[] dataFrom(){
        return new String[]{"imageID","info"};
    }

    @Override
    public int[] dataTo(){
        return new int[]{R.id.list_item_install_image,R.id.list_item_install_name};
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
    public void itemOnClicked(List<Map<String,Object>> listItems, long id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.act);
        builder.setIcon((Drawable) listItems.get((int)id).get("imageID"));
        builder.setTitle((String)listItems.get((int)id).get("info"));
//        builder.setMessage("您确定要切换账号吗？");
        builder.setPositiveButton("确定", null);
        builder.show();
    }
}
