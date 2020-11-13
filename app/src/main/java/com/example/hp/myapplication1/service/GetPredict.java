package com.example.hp.myapplication1.service;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.util.Log;

import com.example.hp.myapplication1.Utils.ToastUtils;
import com.example.hp.myapplication1.Utils.UseTimeDataManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GetPredict {
    private String url = "http://10.0.2.2:8000/mysite/getPredict?username=1&password=2&putData=[1,%2223%22,3,4,5]&data=";
    private String urltrain = "http://10.0.2.2:8000/mysite/postTrain";

    private void predict(Context context){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        JSONObject jsonObject = new JSONObject();
        try{
//            String username = URLEncoder.encode("123", "utf-8");
//            String password = URLEncoder.encode("123", "utf-8");
//            url += "username=" + username + "&password" + password;
            jsonObject.put("list",new int[]{1,2,3,4});
            String str = jsonObject.toString()
                    .replace("\"","%22")
                    .replace("{","%7b")
                    .replace("}","%7d");
            URL url = new URL(this.url+str);
            Log.e("pre:url:",this.url+str);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine())!=null){
                result.append(line);
            }
            Log.e("pre:get:",result.toString());
            ToastUtils.show(context,  result.toString());
        }catch (Exception e){
            Log.e("pre:err:",e.toString());
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(connection != null){
                connection.disconnect();
            }
        }
    }

    private void train(Context context){
        UseTimeDataManager usageQueue  = UseTimeDataManager.getInstance(context);
        usageQueue.refreshData(0);
        List<UsageEvents.Event> mList = usageQueue.getmEventListChecked();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        JSONArray jsonArray = new JSONArray();
        try{
            for(UsageEvents.Event event:mList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",usageQueue.getApplicationNameByPackageName(context,event.getPackageName()));
                jsonObject.put("type",event.getEventType());
                jsonObject.put("time",event.getTimeStamp());
                jsonArray.put(jsonObject);
            }
            URL url = new URL(this.urltrain);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Language","zh-CN");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            String content =  jsonArray.toString();

            os.write(content.getBytes());
            os.flush();
            os.close();
            StringBuilder result = new StringBuilder();
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStreamReader in = new InputStreamReader(connection.getInputStream(),"utf-8");
                BufferedReader bf = new BufferedReader(in);
                String line;
                while((line = bf.readLine())!=null){
                    line = new String(line.getBytes(),"utf-8");
                    result.append(line);
                }
                Log.e("pre:post:",result.toString());
                in.close();
                connection.disconnect();
            }
            ToastUtils.show(context,  result.toString());
        }catch (Exception e){
            Log.e("pre:err:",e.toString());
        }
    }

    public static void getPredict(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetPredict getPredict = new GetPredict();
                getPredict.predict(context);
            }
        }).start();
    }

    public static void doTrain(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetPredict getPredict = new GetPredict();
                getPredict.train(context);
            }
        }).start();
    }
}
