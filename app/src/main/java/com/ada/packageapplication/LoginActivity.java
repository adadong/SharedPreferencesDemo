package com.ada.packageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private CheckBox cbxRemember;
    private EditText etUserName;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.loginBtn);
        btnLogin.setOnClickListener(this);
        cbxRemember = (CheckBox) findViewById(R.id.remember_checkbox);
        etPassword = (EditText) findViewById(R.id.ed_PassWord);
        etUserName = (EditText) findViewById(R.id.ed_UserName);
//        SP_Util.clearData(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                String userName = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                    if(cbxRemember.isChecked()){
                        SP_Util.saveData(this,"isRemember",cbxRemember.isChecked());
                        SP_Util.saveData(this,"userName",userName);
                        SP_Util.saveData(this,"password",password);
                    }
                    login(userName, password);
                } else {
                    Toast.makeText(this, "请将用户名和密码填写完整",Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    public void login(String userName,String password){
        if(userName.equals("Ada") && password.equals("1234")){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        }else{
            Toast.makeText(this, "用户名和密码错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isRemember= (boolean) SP_Util.getData(this,"isRemember",false);
        String userName= (String) SP_Util.getData(this,"userName","");
        String password= (String) SP_Util.getData(this,"password","");
        if (isRemember){
            etPassword.setText(password);
            etUserName.setText(userName);
            cbxRemember.setChecked(isRemember);
        }
    }
}
