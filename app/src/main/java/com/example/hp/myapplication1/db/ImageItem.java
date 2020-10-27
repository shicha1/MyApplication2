package com.example.hp.myapplication1.db;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.graphics.drawable.Drawable;
public class ImageItem {
    //  private static Object MainActivity;
    private String name;
    private Drawable imageIdByDraw;
    private int imageIdByInt;

    private ImageItem(String name,Integer imageIdByInt){
        this.name=name;
        this.imageIdByInt = imageIdByInt;
    }

    private ImageItem(String name,Drawable imageIdByDraw){
        this.name=name;
        this.imageIdByDraw = imageIdByDraw;
    }

    public int getImageIdByInt() {
        return imageIdByInt;
    }

    public String getName(){
        return name;
    }

    public Drawable getImageIdByDraw(){            //ImageID是图像资源的编号，代表需要展示的是哪张图片
        return imageIdByDraw;
    }

    public static List<ImageItem> initInfo(List<String> str,List imageID){
        List<ImageItem> str1 = new LinkedList<>();

        if(imageID.isEmpty())
            throw new NullPointerException("imageID can not be Empty");
        if(imageID.size()!=str.size())
            throw new IllegalArgumentException("size of str must be equal to the size of imageID");
        for(int i =0; i <str.size();i++){
            if(imageID.get(i) instanceof Drawable)
                str1.add(new ImageItem(str.get(i),(Drawable) imageID.get(i)));
            else if(imageID.get(i) instanceof Integer)
                str1.add(new ImageItem(str.get(i),(Integer) imageID.get(i)));
            else
                throw new IllegalArgumentException("Lis imageID has no supported type");
        }
        return str1;
    }

}
