package com.example.hp.myapplication1.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import com.example.hp.myapplication1.R;


public class ContentFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String FIRST = "Building";
    public static final String SECOND = "Book";
    public static final String THIRD = "Paint";
    public static final String FOURTH = "Case";
    public static final String FIFTH = "Shop";
    public static final String SIXTH = "Party";
    public static final String SEVENTH = "Movie";

    private View               containerView;
    protected ImageView        mImageView;
    protected int              res;
    private Bitmap             bitmap;
    protected ListView         mListView;
    private int                recentDays;

    private String type;

    public static ContentFragment newInstance(int resId, String type, int recentDays) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        contentFragment.setType(type);
        contentFragment.setRecentDays(recentDays);
        return contentFragment;
    }

    public static ContentFragment newInstance(int resId) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Integer.class.getName(), resId);
        contentFragment.setArguments(bundle);
        return contentFragment;
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
        mImageView = rootView.findViewById(R.id.image_content);
        mImageView.setClickable(true);
        mImageView.setFocusable(true);
        mImageView.setImageResource(res);
        if(type != null){
            mListView = rootView.findViewById(R.id.list_dropdownview);
            new MyDropDownListView(this.getActivity(),mListView, type, recentDays);
            return rootView;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setRecentDays(int recentDays) {
        this.recentDays = recentDays;
    }
}

