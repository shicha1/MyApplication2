package com.example.hp.myapplication1.fragment;

import com.example.hp.myapplication1.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ImageItem {
    private String name;
    private int imageId;

    public ImageItem(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return  imageId;
    }

    public static List<ImageItem> initialExample(){
        List<ImageItem> l = new ArrayList<>();
        l.add(new ImageItem("aaa",R.drawable.black));
        l.add(new ImageItem("bbb",R.drawable.green));
        l.add(new ImageItem("ccc",R.drawable.white));
        l.add(new ImageItem("ddd",R.drawable.yellow));
        l.add(new ImageItem("eee",R.drawable.blue));
        return l;
    }
}
