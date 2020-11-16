package com.example.hp.myapplication1.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.info.AllUserInfo;
import com.example.hp.myapplication1.info.AppInstalledInfo;
import com.example.hp.myapplication1.Utils.DropDownListView;
import com.example.hp.myapplication1.Utils.ToastUtils;
import com.example.hp.myapplication1.info.AppUsageFLRunInfo;
import com.example.hp.myapplication1.info.AppUsageInfo;
import com.example.hp.myapplication1.info.AppUsageQueueInfo;
import com.example.hp.myapplication1.info.ListItemsManager;
import com.example.hp.myapplication1.service.GetPredict;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyDropDownListView {
    private final Activity                      act;
    private final DropDownListView              listView;
    private ListItemsManager                    listItemsManager;
    private final List<Map<String,Object>>      listItems = new ArrayList<>();
    private final List<Map<String,Object>>      listItems_all = new ArrayList<>();
    private  SimpleAdapter                      adapter;
    public Integer                              MAX = 0;
    public Integer                              Current = 20;
    public Integer                              Load = 20;
    private String                              style;
    private int                                 day;

    public MyDropDownListView(Activity act, ListView myListView, String flag, int recentDays){
        this.day = recentDays;
        this.act = act;
        this.style = flag;
        listView = (DropDownListView)myListView;
        //show different fragment here
        int layout_id;
        TextView tv = this.act.findViewById(R.id.fragment_label);
//        Log.e("day", String.valueOf(recentDays));
        switch (this.style){
            case ContentFragment.FIRST:
                listItemsManager = new AppInstalledInfo(this.act);
                layout_id = R.layout.list_app_install;
                tv.setText(">所有安装程序");
                break;
            case ContentFragment.SECOND:
                listItemsManager = new AppUsageInfo(this.act,recentDays);
                layout_id = R.layout.list_app_usage;
                tv.setText(">程序使用次数和时间");
                break;
            case ContentFragment.THIRD:
                listItemsManager = new AppUsageQueueInfo(this.act,recentDays);
                layout_id = R.layout.list_app_install;
                tv.setText(">程序打开顺序");
                break;
            case ContentFragment.FOURTH:
                listItemsManager = new AppUsageFLRunInfo(this.act);
                layout_id = R.layout.list_app_usage;
                tv.setText(">程序首末打开时间");
                break;
            case ContentFragment.SEVENTH:
                listItemsManager = new AllUserInfo(this.act);
                layout_id =  R.layout.list_user_all;
                tv.setText(">用户管理");
                break;
            default:
                tv.setText(" ");
                return;
        }
//        listItemsManager.getItemList(listItems);
        listItemsManager.getItemList(listItems_all);
        if(listItems_all.size()<Current){
            listItems.addAll(listItems_all);
        }
        else {
            for(int i=0;i<Current;i++){
                listItems.add(listItems_all.get(i));
            }
        }
        MAX = listItems_all.size();
        adapter = new SimpleAdapter(
                this.act,
                listItems,
                layout_id,
                listItemsManager.dataFrom(),
                listItemsManager.dataTo()
        );

        listView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {
            @Override
            public void onDropDown() {
                new MyDropDownListView.GetDataTask(true).execute();
            }
        });
        listView.setOnBottomListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MyDropDownListView.GetDataTask(false).execute();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItemsManager.itemOnClicked(listItems,id);
            }
        });
        listView.setShowFooterWhenNoMore(true);
        //allow SimpleAdapter to load drawable resources
        adapter.setViewBinder(new SimpleAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Drawable){
                    ImageView iv = (ImageView)view;
                    iv.setImageDrawable((Drawable)data);
                    return true;
                }else{
                    return false;
                }
            }
        });
        listView.setAdapter(adapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String> {

        private final boolean isDropDown;

        public GetDataTask(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                ;
            }
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            if(MyDropDownListView.this.style.equals(ContentFragment.THIRD)){
                if(day==30){
                    GetPredict.doTrain(MyDropDownListView.this.act);
                }else {
                    GetPredict.getPredict(MyDropDownListView.this.act);
                }
            }
            if (isDropDown) {
                listItemsManager.itemListUpdate(listItems);
                adapter.notifyDataSetChanged();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(act.getString(R.string.update_at) + dateFormat.format(new Date()));
            } else {
//                listItems.add("Added after on bottom");
                for(int i=Current;i<MAX&&i<Current+Load;i++){
                    listItems.add(listItems_all.get(i));
                }
                Current +=Load;
                adapter.notifyDataSetChanged();

                if (Current >= MAX) {
                    listView.setHasMore(false);
                }
                listView.onBottomComplete();
            }

            super.onPostExecute(result);
        }
    }
}
