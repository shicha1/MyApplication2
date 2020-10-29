package com.example.hp.myapplication1.info;
import android.app.Activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.Map;
import com.example.hp.myapplication1.R;


public class AppInstalledInfo {
    private Activity act;

    public AppInstalledInfo(Activity act){
        this.act = act;
    }

    public List<Map<String,Object>> getAppInstalled(){

        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        List<Map<String,Object>> appList = new LinkedList<>();
        PackageManager packageManager = act.getPackageManager();
        List<PackageInfo> pkgLists = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : pkgLists) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0 || true) {// 这是为了过滤系统应用
                apps.add(packageInfo);
                String str=applicationInfo.loadLabel(packageManager).toString();
//                Log.d("pin", "applicationInfo.packageName->" + applicationInfo.packageName);
            }
        }
        for(int i =0; i <apps.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("imageID",packageManager.getApplicationIcon(apps.get(i).applicationInfo));
            map.put("info",apps.get(i).applicationInfo.loadLabel(packageManager).toString());
            appList.add(map);
        }
        return appList;
    }

    public String[] dataFrom(){
        return new String[]{"imageID","info"};
    }

    public int[] dataTo(){
        return new int[]{R.id.list_item_install_image,R.id.list_item_install_name};
    }

}
