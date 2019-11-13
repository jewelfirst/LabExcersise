package com.romo.romo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    public static final String TBL_USERS = "users",
            TBL_USER_ID = "id",
            TBL_USER_USERNAME = "username",
            TBL_USER_PASSWORD = "password",
            TBL_USER_FULLNAME = "fullname";

    SQLiteDatabase dbReadable = getReadableDatabase();
    SQLiteDatabase dbWriteable = getWritableDatabase();


    public DbHelper(Context context ){

        super(context, "DBSTI", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_create_user_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)",
                TBL_USERS, TBL_USER_ID, TBL_USER_USERNAME, TBL_USER_PASSWORD, TBL_USER_FULLNAME);

        db.execSQL(sql_create_user_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public int createUser(HashMap<String,String> map_user) {

        int userid = 0;
        String sql_check_username = String.format("SELECT * FROM %s WHERE %s= '%s'",
                TBL_USERS, TBL_USER_USERNAME, map_user.get(TBL_USER_USERNAME));
        Cursor cur = dbReadable.rawQuery(sql_check_username,null);


        if (cur.moveToNext()){
            userid = cur.getInt(cur.getColumnIndex(TBL_USER_ID));
        }
        else{
            ContentValues val = new ContentValues();
            val.put(TBL_USER_USERNAME, map_user.get(TBL_USER_USERNAME));
            val.put(TBL_USER_PASSWORD, map_user.get(TBL_USER_PASSWORD));
            val.put(TBL_USER_FULLNAME, map_user.get(TBL_USER_FULLNAME));
            dbWriteable.insert(TBL_USERS,null, val);
        }

        return userid;

    }

    public int checkUser(String username, String password) {
        int userid = 0;
        String sql_check_user = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'",
                TBL_USERS, TBL_USER_USERNAME,username, TBL_USER_PASSWORD,password);
        Cursor cur = dbReadable.rawQuery(sql_check_user,null);
        if (cur.moveToNext()){
            userid = cur.getInt(cur.getColumnIndex(TBL_USER_ID));
        }
        return userid;
    }

    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> all_users = new ArrayList();
        String sql_get_all_users = String.format("SELECT * FROM %s ORDER BY %s ASC", TBL_USERS, TBL_USER_FULLNAME);
        Cursor cur = dbReadable.rawQuery(sql_get_all_users, null);

        while (cur.moveToNext()){
            HashMap<String,String> map_user = new HashMap();
            map_user.put(TBL_USER_ID, cur.getString(cur.getColumnIndex(TBL_USER_ID)));
            map_user.put(TBL_USER_FULLNAME, cur.getString(cur.getColumnIndex(TBL_USER_FULLNAME)));
            map_user.put(TBL_USER_USERNAME, cur.getString(cur.getColumnIndex(TBL_USER_USERNAME)));
            all_users.add(map_user);
        }
        cur.close();
        return all_users;
    }

    public void deleteUser(int userid) {
        dbWriteable.delete(TBL_USERS, TBL_USER_ID + "=" + userid, null);
    }

    public ArrayList<HashMap<String, String>> getSelectedUserData(int userid) {
       String sql = "SELECT * FROM " + TBL_USERS + " WHERE "+
               TBL_USER_ID + " = " + userid;
       Cursor cur = dbReadable.rawQuery(sql,null);
       ArrayList<HashMap<String,String>>select_user = new ArrayList();
       while (cur.moveToNext()){
           HashMap<String,String> map_user = new HashMap();
           map_user.put(TBL_USER_ID, cur.getString(cur.getColumnIndex(TBL_USER_ID)));
           map_user.put(TBL_USER_FULLNAME, cur.getString(cur.getColumnIndex(TBL_USER_FULLNAME)));
           map_user.put(TBL_USER_USERNAME, cur.getString(cur.getColumnIndex(TBL_USER_USERNAME)));
           select_user.add(map_user);
       }
       cur.close();

       return select_user;
    }

    public void updateUser(HashMap<String, String> map_user) {
        ContentValues val = new ContentValues();
        val.put(TBL_USER_USERNAME,map_user.get(TBL_USER_USERNAME));
        val.put(TBL_USER_PASSWORD,map_user.get(TBL_USER_PASSWORD));
        val.put(TBL_USER_FULLNAME,map_user.get(TBL_USER_FULLNAME));
        dbWriteable.update(TBL_USERS,val,TBL_USER_ID +
                "=" + map_user.get(TBL_USER_ID),null);
    }
}
