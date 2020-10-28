package com.example.hp.myapplication1.MyList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hp.myapplication1.R;
import com.example.hp.myapplication1.db.UserPOJO;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserPOJO> {
    private int resourceId;

    public UserAdapter(Context context, int textViewResourceId, List<UserPOJO> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        UserPOJO userPOJO = getItem(position);
        View view;
        UserAdapter.ViewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new UserAdapter.ViewHolder();
            viewHolder.tv_id = view.findViewById(R.id.textView_user);
            viewHolder.tv_pwd = view.findViewById(R.id.textView_pwd);
            viewHolder.tv_type = view.findViewById(R.id.textView_type);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(UserAdapter.ViewHolder) view.getTag();
        }
        viewHolder.tv_id.setText(userPOJO.getUserID());
        viewHolder.tv_pwd.setText(userPOJO.getPwd());
        if(userPOJO.getUserType() == 1)
            viewHolder.tv_type.setText("学生");
        else if (userPOJO.getUserType() == 2)
            viewHolder.tv_type.setText("管理员");
        return view;
    }

    class ViewHolder{
        TextView tv_id;
        TextView tv_pwd;
        TextView tv_type;
    }
}
