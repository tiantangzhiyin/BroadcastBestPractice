package com.example.broadcastbestpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountEdit=(EditText)findViewById(R.id.account);
        passwordEdit=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        //获取SharedPreferences的数据
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember=preferences.getBoolean("remember_password",false);
        if(isRemember){
            //设置账号密码到文本框中
            String account=preferences.getString("account",null);
            String password=preferences.getString("password",null);
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);//勾选记住密码复选框
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=accountEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                //如果账号密码正确，登录成功
                if(account.equals("admin")&&password.equals("123456")){
                    editor=preferences.edit();
                    //检查复选框是否选中
                    if(rememberPass.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",password);
                    }else{
                        editor.clear();
                    }
                    editor.apply();

                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"account or password is invalid",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
