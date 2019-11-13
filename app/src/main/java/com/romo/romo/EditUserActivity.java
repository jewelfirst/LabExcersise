package com.romo.romo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class EditUserActivity extends AppCompatActivity {
    EditText et_username, et_fullname;
    String username, fullname;
    int success , userid;
    DbHelper db;
    ArrayList<HashMap<String,String>>select_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        db = new DbHelper(this);
        et_username = (EditText) findViewById(R.id.ET_username);
        et_fullname = (EditText) findViewById(R.id.ET_fullname);

        Intent intent = getIntent();
        userid = intent.getIntExtra(db.TBL_USER_ID,0);
        select_user = db.getSelectedUserData(userid);
        et_username.setText(select_user.get(0).get(db.TBL_USER_USERNAME));
        et_fullname.setText(select_user.get(0).get(db.TBL_USER_FULLNAME));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_cancel,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSave:
                success = 2;
                username = et_username.getText().toString();
                fullname = et_fullname.getText().toString();


                if (username.equals("")) {
                    et_username.setError("This field is required");
                    success--;
                }

                if (fullname.equals("")) {
                    et_fullname.setError("This field is required");
                    success--;
                }


                if (success == 2) {
                    HashMap<String, String> map_user = new HashMap();
                    map_user.put(db.TBL_USER_ID,String.valueOf(userid));
                    map_user.put(db.TBL_USER_USERNAME, username);
                    map_user.put(db.TBL_USER_FULLNAME, fullname);
                    db.updateUser(map_user);
                    Toast.makeText(this, "Data Successfully Updated",
                            Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
            case R.id.btnCancel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
