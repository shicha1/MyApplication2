package com.example.hp.myapplication1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;


public class MainActivity extends Activity {

    private HashMap<String,String> stuMap = new HashMap<String, String>();
    private HashMap<String,String> manMap = new HashMap<String, String>();
    private RadioGroup rGroup ;
    private EditText user_id_editT;
    private EditText user_pwd_editT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login_btn = findViewById(R.id.loginbtn);
        Button sign_btn = findViewById(R.id.signbtn);
        login_btn.setOnClickListener(new Login_btn_listener());
        sign_btn.setOnClickListener(new sign_btn_listener());
        rGroup = findViewById(R.id.rGroup);
        user_id_editT = findViewById(R.id.text_userid);
        user_pwd_editT = findViewById(R.id.text_userpwd);
        stuMap.put("123","123");
        manMap.put("123","123");
    }

    private class Login_btn_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            HashMap<String,String> logmap;
            if(rGroup.getCheckedRadioButtonId() == R.id.stu_rbtn){
                logmap = stuMap;
            }else {
                logmap = manMap;
            }
            String user_id_str = user_id_editT.getText().toString();
            String user_pwd_str = user_pwd_editT.getText().toString();
            if(user_id_str.isEmpty() || user_pwd_str.isEmpty()){
                quickToast("账户密码不能为空");
                return;
            }
            if(!logmap.containsKey(user_id_str)){
                quickToast("账户不存在");
                return;
            }
            if(!logmap.get(user_id_str).equals(user_pwd_str)){
                quickToast("密码错误");
                return;
            }
            quickToast("登陆成功");
        }
    }

    private class sign_btn_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String user_id_str = user_id_editT.getText().toString();
            String user_pwd_str = user_pwd_editT.getText().toString();
            if(user_id_str.isEmpty() || user_pwd_str.isEmpty()){
                quickToast("账户密码不能为空");
                return;
            }
            if(rGroup.getCheckedRadioButtonId() == R.id.stu_rbtn){
                stuMap.put(user_id_str,user_pwd_str);
            }else {
                manMap.put(user_id_str,user_pwd_str);
            }
        }
    }

    private void quickToast(String str){
        Toast.makeText(MainActivity.this,str, Toast.LENGTH_SHORT).show();
    }
}
