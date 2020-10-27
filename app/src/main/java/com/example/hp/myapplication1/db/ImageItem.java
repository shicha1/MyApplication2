package com.example.hp.myapplication1.db;
import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
public class ImageItem {
    //  private static Object MainActivity;
    private String name;
    private Drawable imageId;

    public ImageItem(String name,Drawable imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public String getName(){
        return name;
    }

    public Drawable getImageId(){            //ImageID是图像资源的编号，代表需要展示的是哪张图片
        return  imageId;
    }

    public static List<ImageItem> initInfo(List<String> str,Drawable[] draw){
        List<ImageItem> str1 = new ArrayList<>();

        for(int i =0; i <str.size();i++){
            str1.add(new ImageItem(str.get(i),draw[i]));
        }
        return str1;
    }

}
