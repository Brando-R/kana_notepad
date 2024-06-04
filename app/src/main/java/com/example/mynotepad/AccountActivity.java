package com.example.mynotepad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AccountActivity extends AppCompatActivity {
    private static final String DB_NAME = "database"; // 数据库名称
    private static final int DB_VERSION = 1; // 数据库版本号
    private MyData myData;
    private SQLiteDatabase db;
    private Button returnMe,alterAccout;
    private EditText usernameText,passText,newpassText,newusernameText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        //使程序保持日间模式
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        usernameText=findViewById(R.id.AccountName);
        passText=findViewById(R.id.AccountPass);
        newpassText=findViewById(R.id.newAccountPass);
        returnMe=findViewById(R.id.AccoutreturnButton);
        alterAccout=findViewById(R.id.alterAccout);
        newusernameText=findViewById(R.id.newAccountName);
        myData=new MyData(this,DB_NAME,null,DB_VERSION);
        db=myData.getWritableDatabase();    //获取数据库实例

        Intent intent = getIntent();
        String pass=intent.getStringExtra("pass");
        String username=intent.getStringExtra("username");
        usernameText.setText(username);
        newusernameText.setText(username);

        alterAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname=newusernameText.getText().toString();
                String newpass=newpassText.getText().toString();
                String newoldpass=passText.getText().toString();
                String newoldusername=usernameText.getText().toString();

                //利用sql语句寻找数据表notetable中name=username的内容
                String sql = "SELECT * FROM usertable where name=? and pass=?";
                Cursor cursor = db.rawQuery(sql, new String[]{newoldusername,newoldpass});   //username替换？的内容
                if (cursor.getCount()!=0){
                    if (newname.equals("")||newpass.equals("")){
                        Toast.makeText(AccountActivity.this, "新用户名或密码为空！", Toast.LENGTH_SHORT).show();
                    }else {
                        ContentValues values=new ContentValues();
                        values.put("name",newname);
                        values.put("pass",newpass);
                        db.update("usertable",values,"name=? and pass=?",new String[]{username,pass});

                        ContentValues values1=new ContentValues();
                        values1.put("name",newname);
                        db.update("notetable",values1,"name=?",new String[]{username});
                        Toast.makeText(AccountActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                        //更新usertable-sql
                        SharedPreferences.Editor editor=getSharedPreferences("usertable_sql",MODE_PRIVATE).edit();
                        editor.putString("name",newname);
                        editor.putString("pass",newpass);
                        editor.apply();

                        Intent AccoutToMe=new Intent(AccountActivity.this,MeActivity.class);
                        AccoutToMe.putExtra("username",newname);
                        AccoutToMe.putExtra("pass",newpass);
                        startActivity(AccoutToMe);
                    }
                }else
                    Toast.makeText(AccountActivity.this, "原用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

        returnMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AccoutToMe=new Intent(AccountActivity.this,MeActivity.class);
                AccoutToMe.putExtra("username",username);
                AccoutToMe.putExtra("pass",pass);
                startActivity(AccoutToMe);
            }
        });

    }
}