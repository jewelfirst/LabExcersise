package com.romo.romo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button bt_login;
    TextView textViewcreate;
    EditText et_username, et_password;
    String username, password;
    int formsuccess, userid;
    SharedPreferences shared;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DbHelper(this);
        bt_login = (Button) findViewById(R.id.btn_login);
        textViewcreate = (TextView) findViewById(R.id.tv_create);
        et_username = (EditText) findViewById(R.id.ET_logusername);
        et_password = (EditText) findViewById(R.id.ET_logpassword);
        textViewcreate.setOnClickListener(this);
        bt_login.setOnClickListener(this);

        shared = getSharedPreferences("Pangalan", Context.MODE_PRIVATE);

        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                formsuccess = 2;

                if(username.equals("")){
                    et_username.setError("This field is required");
                    formsuccess --;
                }
                if(password.equals("")){
                    et_password.setError("This field is required");
                    formsuccess --;
                }
                if(formsuccess == 2){

                    userid = db.checkUser(username,password);
                    if(userid >=1 ) {
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putInt(db.TBL_USER_ID,userid).commit();
                        this.finish();
                        startActivity(new Intent(this, HomeActivity.class));

                    }
                    else {
                        et_username.setError("invalid credentials");
                        et_password.setText("");
                    }

                    //Intent create = new Intent(LoginActivity.this, MainActivity.class);
                    // startActivity(create);
                }
                break;

            case R.id.tv_create:
                Intent create = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(create);
                break;

        }
    }
    public void onResume() {
        if(shared.contains(db.TBL_USER_ID)){
            this.finish();
            startActivity(new Intent(this,HomeActivity.class));
        }
        super.onResume();
    }
}
