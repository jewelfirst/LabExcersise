package com.romo.romo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText et_username, et_password , et_fullname;
    String username, password, fullname;
    int success , userid;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DbHelper(this);
        et_username = (EditText) findViewById(R.id.ET_username);
        et_password = (EditText) findViewById(R.id.ET_password);
        et_fullname = (EditText) findViewById(R.id.ET_fullname);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.save_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnSave:
            username = et_username.getText().toString();
            password = et_password.getText().toString();
            fullname = et_fullname.getText().toString();
            success = 3;

            if(username.equals("")){
                et_username.setError("This field is required");
                success --;
            }

            if(password.equals("")){
                et_password.setError("This field is required");
                success --;
            }

            if(fullname.equals("")){
                et_fullname.setError("This field is required");
                success --;
            }

            if(success == 3) {
                HashMap<String, String> map_user = new HashMap();
                map_user.put(db.TBL_USER_USERNAME, username);
                map_user.put(db.TBL_USER_PASSWORD, password);
                map_user.put(db.TBL_USER_FULLNAME, fullname);
                userid = db.createUser(map_user);

                if (userid < 1){
                    Toast.makeText(this, "User Successfully Created",
                            Toast.LENGTH_SHORT).show();
                }
                else{

                    et_username.setError("Username Already Taken");
                }

            }

            break;

            case R.id.btnCancel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
