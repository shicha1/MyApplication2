package com.example.hp.myapplication1.info;

import java.util.List;
import java.util.Map;

public interface ListItemsManager {
    public List<Map<String,Object>> getItemList(List<Map<String,Object>> mapList);

    public List<Map<String,Object>> itemListUpdate(List<Map<String,Object>> mapList);

    public List<Map<String,Object>> itemListLoadMore(List<Map<String,Object>> mapList);

    public String[] dataFrom();

    public int[] dataTo();

    public void itemOnClicked(List<Map<String,Object>> listItems, long id);
}
