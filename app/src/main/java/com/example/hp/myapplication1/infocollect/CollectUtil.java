package com.example.hp.myapplication1.infocollect;
import android.app.Activity;
import java.util.List;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.util.ArrayList;
import com.example.hp.myapplication1.db.ImageItem;


public class CollectUtil {

    public List<ImageItem> getAppInstalled(Activity act){

        List<PackageInfo> apps = new ArrayList<PackageInfo>();

        PackageManager packageManager = act.getPackageManager();

        List<PackageInfo> pkgLists = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : pkgLists) {

            ApplicationInfo applicationInfo = packageInfo.applicationInfo;

            if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {// 这是为了过滤系统应用

                apps.add(packageInfo);

                String str=applicationInfo.loadLabel(packageManager).toString();

                Log.d("pin", "applicationInfo.packageName->" + applicationInfo.packageName);

            }
        }


        Drawable[] imageID=new Drawable[apps.size()];
        for(int i =0; i <apps.size();i++){
            imageID[i]=(packageManager.getApplicationIcon(apps.get(i).applicationInfo));
        }

        List<String> info1 = new ArrayList<>();
        for(int i =0; i <apps.size();i++){
            info1.add(apps.get(i).applicationInfo.loadLabel(packageManager).toString());
        }

        return ImageItem.initInfo(info1,imageID);

    }


}
