package com.example.mynotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyData extends SQLiteOpenHelper {
    private Context mcontext;
    public MyData(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建立一个usertable来储存用户的用户名和密码
        String Usersql="create table usertable(name varchar(20) ,pass varchar(20))";
        //建立一个notetable来存储日记内容，包括用户名，日记日期和日记内容
        String Notesql="create table notetable(name varchar(20),notedata datetime,note text)";
        db.execSQL(Usersql);
        db.execSQL(Notesql);
        Toast.makeText(mcontext, "建表成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addUser(MyData myData,String name,String pass){


    }
}
