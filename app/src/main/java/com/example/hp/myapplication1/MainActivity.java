package com.example.hp.myapplication1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hp.myapplication1.db.DbHelper;

import java.util.HashMap;


public class MainActivity extends Activity {

    private RadioGroup rGroup ;
    private EditText user_id_editT;
    private EditText user_pwd_editT;
    private DbHelper myDbHelper;

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
        myDbHelper = new DbHelper(this);
    }

    private class Login_btn_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            HashMap<String,Object> rsMap;
            Integer type;
            if(rGroup.getCheckedRadioButtonId() == R.id.stu_rbtn){
                type = 1;
            }else {
                type = 2;
            }
            String user_id_str = user_id_editT.getText().toString();
            String user_pwd_str = user_pwd_editT.getText().toString();
            if(user_id_str.isEmpty() || user_pwd_str.isEmpty()){
                quickToast("账户密码不能为空");
                return;
            }
            rsMap = myDbHelper.query(user_id_str);
            String rsPWD = (String) rsMap.get(DbHelper.TBL_NAME1_COL2);
            Integer rsType = (Integer) rsMap.get(DbHelper.TBL_NAME1_COL3);
            if(rsPWD != null && rsType!=null){
                if(rsPWD.equals(user_pwd_str)&&rsType.equals(type)){
                    quickToast("登陆成功");
                    Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    return;
                }
                quickToast("密码或权限错误");
            }
        }
    }

    private class sign_btn_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            HashMap<String,Object> rsMap;
            String user_id_str = user_id_editT.getText().toString();
            String user_pwd_str = user_pwd_editT.getText().toString();
            if(user_id_str.isEmpty() || user_pwd_str.isEmpty()){
                quickToast("账户密码不能为空");
                return;
            }
            if(rGroup.getCheckedRadioButtonId() == R.id.stu_rbtn){
                rsMap = myDbHelper.query(user_id_str);
                if(rsMap.get(DbHelper.TBL_NAME1_COL2)!=null){
                    quickToast("账户已存在");
                }else{
                    myDbHelper.insert(user_id_str,user_pwd_str,1);
                    quickToast("学生用户注册成功");
                }
            }else {
                quickToast("管理员不能直接注册");
            }
        }
    }

    private void quickToast(String str){
        Toast.makeText(MainActivity.this,str, Toast.LENGTH_SHORT).show();
    }
}
