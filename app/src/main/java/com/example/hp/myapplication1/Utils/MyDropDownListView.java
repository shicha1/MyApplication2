package com.example.hp.myapplication1.Utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hp.myapplication1.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class MyDropDownListView {
    private final Activity act;
    private final LinkedList<String>   listItems;
    private final DropDownListView     listView;
    private final ArrayAdapter<String> adapter;
    private final String[]             mStrings            =
            {"Aaaaaa", "Bbbbbb", "Cccccc", "Dddddd", "Eeeeee", "Ffffff",
                    "Gggggg", "Hhhhhh", "Iiiiii", "Jjjjjj", "Kkkkkk", "Llllll", "Mmmmmm", "Nnnnnn",};
    public static final int      MORE_DATA_MAX_COUNT = 3;
    public int                   moreDataCount       = 0;

    public MyDropDownListView(Activity act, ListView myListView){
        this.act = act;
        listView = (DropDownListView)myListView;
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
        listItems = new LinkedList<>();
        listItems.addAll(Arrays.asList(mStrings));
        adapter = new ArrayAdapter<>(MyDropDownListView.this.act, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        private final boolean isDropDown;

        public GetDataTask(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {

            if (isDropDown) {
                listItems.addFirst("Added after drop down");
                adapter.notifyDataSetChanged();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(act.getString(R.string.update_at) + dateFormat.format(new Date()));
            } else {
                moreDataCount++;
                listItems.add("Added after on bottom");
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
