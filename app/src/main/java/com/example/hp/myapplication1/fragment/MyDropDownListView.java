package com.example.hp.myapplication1.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.info.AllUserInfo;
import com.example.hp.myapplication1.info.AppInstalledInfo;
import com.example.hp.myapplication1.Utils.DropDownListView;
import com.example.hp.myapplication1.Utils.ToastUtils;
import com.example.hp.myapplication1.info.AppUsageInfo;
import com.example.hp.myapplication1.info.AppUsageQueueInfo;
import com.example.hp.myapplication1.info.ListItemsManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyDropDownListView {
    private final Activity                      act;
    private final DropDownListView              listView;
    private ListItemsManager                    listItemsManager;
    private final List<Map<String,Object>>      listItems = new LinkedList<>();
    private  SimpleAdapter                      adapter;
    public Integer                              MORE_DATA_MAX_COUNT = 0;
    public Integer                              moreDataCount       = 0;

    public MyDropDownListView(Activity act, ListView myListView, String flag){
        this.act = act;
        listView = (DropDownListView)myListView;
        //show different fragment here
        int layout_id;
        switch (flag){
            case ContentFragment.FIRST:
                listItemsManager = new AppInstalledInfo(this.act);
                layout_id = R.layout.list_app_install;
                break;
            case ContentFragment.SECOND:
                listItemsManager = new AppUsageInfo(this.act);
                layout_id = R.layout.list_app_usage;
                break;
            case ContentFragment.THIRD:
                listItemsManager = new AppUsageQueueInfo(this.act);
                layout_id = R.layout.list_app_install;
                break;
            case ContentFragment.SEVENTH:
                listItemsManager = new AllUserInfo(this.act);
                layout_id =  R.layout.list_user_all;
                break;
            default: return;
        }
        listItemsManager.getItemList(listItems);
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
                ToastUtils.show(MyDropDownListView.this.act, R.string.drop_down_tip);
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

            if (isDropDown) {
                listItemsManager.itemListUpdate(listItems);
                adapter.notifyDataSetChanged();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(act.getString(R.string.update_at) + dateFormat.format(new Date()));
            } else {
                moreDataCount++;
//                listItems.add("Added after on bottom");
                adapter.notifyDataSetChanged();

                if (moreDataCount >= MORE_DATA_MAX_COUNT) {
                    listView.setHasMore(false);
                }
                listView.onBottomComplete();
            }

            super.onPostExecute(result);
        }
    }
}