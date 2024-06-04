package com.example.mynotepad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;

import java.util.Map;

public class registerActivity extends AppCompatActivity {
    private static final String DB_NAME = "database"; // 数据库名称
    private static final int DB_VERSION = 1; // 数据库版本号

    private MyData myData;
    private SQLiteDatabase db;
    private Button registerButton;
    private EditText editName,editPass,editPasstoo;

    private Map<String,?> allUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        //使程序保持日间模式
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        editName=findViewById(R.id.editName);
        editPass=findViewById(R.id.editPassword);
        editPasstoo=findViewById(R.id.editPasswordtoo);

        myData = new MyData(this, DB_NAME, null, DB_VERSION);
        db=myData.getWritableDatabase();

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                boolean flag = true;      //判断用户名是否存在
                String name = editName.getText().toString();
                String pass01 = editPass.getText().toString();
                String pass02 = editPasstoo.getText().toString();

                //数据库存储
                if (name.equals("") || pass01.equals("") || pass02.equals(""))
                    Toast.makeText(registerActivity.this, "用户名或密码不能为空！！！", Toast.LENGTH_LONG).show();
                else {
                    //获得循环数据表中的行
                    String sql = "SELECT * FROM usertable";
                    Cursor cursor = db.rawQuery(sql, null);
                    //如果数据库中有数据，查看该用户名是否被注册使用
                    if (cursor.getCount() > 0)
                        while (cursor.moveToNext()) {
                            if (cursor.getString(cursor.getColumnIndex("name")).equals(name)){
                                flag=false; //如果该用户名被注册则把flag置false，后续弹窗提示该用户名已被使用
                                break;
                            }
                        }
                    if (flag) {
                        //确认两次密码是否一致，一致则把用户存储到数据库中
                        if (pass01.equals(pass02)) {
                            //通过键值对把name和pass01对应放入values中
                            ContentValues values = new ContentValues();
                            values.put("name", name);
                            values.put("pass", pass01);
                            //代码插入语句，usertable是表名，null是没有数据时插入的内容，values是插入的内容
                            db.insert("usertable", null, values);
                            //利用键值对存储，把最新的注册信息存储到usertable_sql表中，后续跳转到登录页面时直接显示账号名和密码
                            SharedPreferences.Editor editor = getSharedPreferences("usertable_sql", MODE_PRIVATE).edit();
                            editor.putString("name",name);
                            editor.putString("pass",pass01);
                            editor.commit();

                            //跳转到登录页面
                            Intent intent = new Intent();
                            intent.setClass(registerActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(registerActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(registerActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(registerActivity.this, "该用户名已被使用", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

/*
        //键值对存储
                if (name.equals("") || pass01.equals("") || pass02.equals("")){
                    Toast.makeText(registerActivity.this, "用户名或密码不能为空！！！", Toast.LENGTH_LONG).show();
                }
                else{
                    SharedPreferences sharedPreferences=getSharedPreferences("usertable",MODE_PRIVATE);

                    allUser=sharedPreferences.getAll();
                    for (Map.Entry<String, ?> user : allUser.entrySet()){
                        if (user.getKey().equals(name)){
                            flag=false;
                            break;
                        }
                    }
                    if (flag){
                        if (pass01.equals(pass02)){
                            SharedPreferences.Editor editor = getSharedPreferences("usertable", MODE_PRIVATE).edit();
                            editor.putString(name, pass01);
                            editor.apply();
                            //跳转到登录页面
                            Intent intent = new Intent();
                            intent.setClass(registerActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(registerActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(registerActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(registerActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                }
*/
