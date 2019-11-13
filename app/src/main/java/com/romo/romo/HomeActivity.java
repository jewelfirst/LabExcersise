package com.romo.romo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    DbHelper db;
    ListView lvUsers;
    ArrayList<HashMap<String,String>> all_users;
    ListViewAdapter adapter;
    SharedPreferences shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        shared = getSharedPreferences("Pangalan", Context.MODE_PRIVATE);

        db = new DbHelper(this);
        lvUsers = findViewById(R.id.lvusers);
        fetch_user();
    }

    private void fetch_user() {
        all_users = db.getAllUsers();
        adapter = new ListViewAdapter(this, R.layout.adapter_user, all_users);
        lvUsers.setAdapter(adapter);
    }

    private class ListViewAdapter extends ArrayAdapter {

        LayoutInflater inflater;
        ArrayList<HashMap<String, String>> all_users;
        TextView tvfullname, tvusername;
        ImageView ivedit, ivdelete;

        public ListViewAdapter(Context context, int resource, ArrayList<HashMap<String, String>> all_users) {
            super(context, resource, all_users);
            inflater = LayoutInflater.from(context);
            this.all_users = all_users;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.adapter_user, parent,false);
            tvfullname = convertView.findViewById(R.id.tvfullname);
            tvusername = convertView.findViewById(R.id.tvusername);
            ivedit = convertView.findViewById(R.id.ivedit);
            ivdelete = convertView.findViewById(R.id.ivdelete);

            tvfullname.setText(all_users.get(position).get(db.TBL_USER_FULLNAME));
            tvusername.setText(all_users.get(position).get(db.TBL_USER_USERNAME));

            ivdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userid = Integer.parseInt(all_users.get(position).get(db.TBL_USER_ID));
                    db.deleteUser(userid);
                    Toast.makeText(HomeActivity.this, "USER SUCCESSFULLY DELETED", Toast.LENGTH_SHORT).show();  
                    fetch_user();
                }
            });
            ivedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int userid = Integer.parseInt(all_users.get(position).get(db.TBL_USER_ID));
                    Intent intent = new Intent(getContext(),EditUserActivity.class);
                    intent.putExtra(db.TBL_USER_ID,userid);
                    startActivity(intent);
                }
            });

            return (convertView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_logout:
                new AlertDialog.Builder(this).setTitle("Logout").setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                shared.edit().remove(db.TBL_USER_ID).commit();
                                Toast.makeText(HomeActivity.this,"You've been successfully logout",
                                        Toast.LENGTH_SHORT).show();
                                startActivity( new Intent(getBaseContext(),LoginActivity.class));
                                HomeActivity.this.finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
   public void onResume() {
        fetch_user();
        if(!shared.contains(db.TBL_USER_ID)){
            this.finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        super.onResume();
   }
}
