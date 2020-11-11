package com.example.hp.myapplication1;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.example.hp.myapplication1.Utils.ToastUtils;
import com.example.hp.myapplication1.db.DbHelper;
import com.example.hp.myapplication1.db.UserPOJO;
import java.util.List;


public class MainActivity extends Activity {

    private RadioGroup rGroup ;
    private EditText   user_id_editT;
    private EditText   user_pwd_editT;
    private DbHelper   myDbHelper;

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
       //check permission
        PackageManager packageManager = getApplicationContext()
                .getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if(list.size() > 0){
            long ts = System.currentTimeMillis();
            UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext()
                    .getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                    UsageStatsManager.INTERVAL_BEST, 0, ts);
            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                Intent in = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(in);
            }
        }
    }

    private class Login_btn_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            UserPOJO userPOJO;
            Integer type;
            if(rGroup.getCheckedRadioButtonId() == R.id.stu_rbtn){
                type = 1;
            }else {
                type = 2;
            }
            String user_id_str = user_id_editT.getText().toString();
            String user_pwd_str = user_pwd_editT.getText().toString();
            if(user_id_str.isEmpty() || user_pwd_str.isEmpty()){
                ToastUtils.show(MainActivity.this,"账户密码不能为空");
                return;
            }
            userPOJO = myDbHelper.queryUserByID(user_id_str);
            String rsPWD = userPOJO.getPwd();
            Integer rsType = userPOJO.getUserType();
            if(rsPWD != null && rsType!=null){
                if(rsPWD.equals(user_pwd_str)&&rsType.equals(type)){
                    ToastUtils.show(MainActivity.this,"登陆成功");
//                    Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
//                    intent.putExtra("type",type);
//                    startActivity(intent);
              //      Intent intent3 = new Intent(MainActivity.this, AppStatisticsList.class);
                      Intent intent3 = new Intent(MainActivity.this, AppStatisticsList.class);
                      intent3.putExtra("type",type);
                     startActivity(intent3);
                    MainActivity.this.finish();
                    return;
                }
                ToastUtils.show(MainActivity.this,"密码或权限错误");
            }
        }
    }

    private class sign_btn_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String user_id_str = user_id_editT.getText().toString();
            String user_pwd_str = user_pwd_editT.getText().toString();
            if(user_id_str.isEmpty() || user_pwd_str.isEmpty()){
                ToastUtils.show(MainActivity.this,"账户密码不能为空");
                return;
            }
            if(rGroup.getCheckedRadioButtonId() == R.id.stu_rbtn){
                if(myDbHelper.queryUserByID(user_id_str).getPwd()!=null){
                    ToastUtils.show(MainActivity.this,"账户已存在");
                }else{
                    myDbHelper.insertUser(user_id_str,user_pwd_str,1);
                    ToastUtils.show(MainActivity.this,"学生用户注册成功");
                }
            }else {
                ToastUtils.show(MainActivity.this,"管理员不能直接注册");
            }
        }
    }
}
