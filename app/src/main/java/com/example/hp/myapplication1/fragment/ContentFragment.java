package com.example.hp.myapplication1.fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

import com.example.hp.myapplication1.MyList.DropDownListView;
import com.example.hp.myapplication1.MyList.ToastUtils;
import com.example.hp.myapplication1.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;


public class ContentFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String First = "Building";
    public static final String SECOND = "Book";
    public static final String THIRD = "Paint";
    public static final String FOURTH = "Case";
    public static final String FIFTH = "Shop";
    public static final String SIXTH = "Party";
    public static final String SEVENTH = "Movie";

    private View containerView;
    protected ImageView mImageView;
    protected int res;
    private Bitmap bitmap;
    protected ListView mListView;
    private ListAdapter myAdapter;

    private LinkedList<String> listItems           = null;
    private DropDownListView listView            = null;
    public int flag = 0;
    private ArrayAdapter<String> adapter;
    private String[]             mStrings            =
            {"Aaaaaa", "Bbbbbb", "Cccccc", "Dddddd", "Eeeeee", "Ffffff",
                    "Gggggg", "Hhhhhh", "Iiiiii", "Jjjjjj", "Kkkkkk", "Llllll", "Mmmmmm", "Nnnnnn",};
    public static final int      MORE_DATA_MAX_COUNT = 3;
    public int                   moreDataCount       = 0;
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
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

                // should call onDropDownComplete function of DropDownListView at end of drop down complete.
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
            } else {
                moreDataCount++;
                listItems.add("Added after on bottom");
                adapter.notifyDataSetChanged();

                if (moreDataCount >= MORE_DATA_MAX_COUNT) {
                    listView.setHasMore(false);
                }

                // should call onBottomComplete function of DropDownListView at end of on bottom complete.
                listView.onBottomComplete();
            }

            super.onPostExecute(result);
        }
    }

    public static ContentFragment newInstance(int resId, ListAdapter adapter) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        contentFragment.setMyAdapter(adapter);
        return contentFragment;
    }

    public static ContentFragment newInstance(int resId, int type) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        contentFragment.flag = type;
        return contentFragment;
    }

    public static ContentFragment newInstance(int resId) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    public void setMyAdapter(ListAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getArguments().getInt(Integer.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.image_content);
        mImageView.setClickable(true);
        mImageView.setFocusable(true);
        mImageView.setImageResource(res);
        if(flag == 1){
            listView = (DropDownListView)rootView.findViewById(R.id.list_view);
            // set drop down listener
            listView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {

                @Override
                public void onDropDown() {
                    new ContentFragment.GetDataTask(true).execute();
                }
            });

            // set on bottom listener
            listView.setOnBottomListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new ContentFragment.GetDataTask(false).execute();
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ToastUtils.show(getActivity(), R.string.drop_down_tip);
                }
            });
            listView.setShowFooterWhenNoMore(true);

            listItems = new LinkedList<String>();
            listItems.addAll(Arrays.asList(mStrings));
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
            listView.setAdapter(adapter);
            return rootView;
        }
        if(myAdapter != null){
            mListView = rootView.findViewById(R.id.list_content);
            mListView.setAdapter(myAdapter);
        }
        return rootView;
    }

    @Override
    public void takeScreenShot() {
        Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                containerView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        containerView.draw(canvas);
        ContentFragment.this.bitmap = bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

}

