package com.example.mynotepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.mynotepad.R.id;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String DB_NAME = "database"; // 数据库名称
    private static final int DB_VERSION = 1; // 数据库版本号
    private MyData myData;
    private SQLiteDatabase db;
    private Button loginButton,button;
    private TextView regiterText;
    private EditText Username,Userpass;
    private CheckBox rememberPass;
    private Map<String,?> allUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //使程序保持日间模式
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        loginButton = findViewById(R.id.login_button);
        button=findViewById(R.id.button2);
        Username=findViewById(R.id.username);
        Userpass=findViewById(id.Password);
        regiterText=findViewById(id.registerText);
        regiterText.setPaintFlags(regiterText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        rememberPass=findViewById(id.remember_password);
        rememberPass.setChecked(true);


        //使用SharedPreferences获取账号和密码
        SharedPreferences sq=getSharedPreferences("usertable_sql",MODE_PRIVATE);
        //自动显示上一次记住的账号，或者是最近一次注册的账号
        Username.setText(sq.getString("name",null));
        Userpass.setText(sq.getString("pass",null));

        myData=new MyData(this,DB_NAME,null,DB_VERSION);
        db=myData.getWritableDatabase();    //获取数据库实例


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=true;
                //获得用户名，密码，是否保存账户密码
                String name=Username.getText().toString();
                String pass=Userpass.getText().toString();
                boolean flagremeber=rememberPass.isChecked();

                //登录功能
                String sql="SELECT * FROM usertable where name=? and pass=?";
                Cursor cursor=db.rawQuery(sql,new String[]{name,pass});
                if (cursor.getCount()!=0){
                    //如果选择记住账号密码，则更新usertable-sql
                    if (flagremeber){
                        //更新usertable-sql
                        SharedPreferences.Editor editor=getSharedPreferences("usertable_sql",MODE_PRIVATE).edit();
                        editor.putString("name",name);
                        editor.putString("pass",pass);
                        editor.apply();
                    }

                    //跳转
                    String username=Username.getText().toString();
                    //每次跳转都跳出Toast
                    Toast.makeText(MainActivity.this, "登录成功！你好，亲爱的"+username, Toast.LENGTH_SHORT).show();
                    Intent MainToHome=new Intent(MainActivity.this, homeActivity.class);
                    MainToHome.putExtra("username", username);  //带上用户名一起跳转
                    MainToHome.putExtra("pass",pass);
                    startActivity(MainToHome);

                }else
                    Toast.makeText(MainActivity.this, "密码或用户名错误！", Toast.LENGTH_SHORT).show();

            }
        });

        regiterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "正在跳转到注册页面", Toast.LENGTH_SHORT).show();
                Intent MainToRegister=new Intent(MainActivity.this, registerActivity.class);
                startActivity(MainToRegister);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("今天的心情怎么样？");
                final String[] list={"开心","难过","郁闷","生气"};
                builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(list[which]=="开心")
                            Toast.makeText(MainActivity.this,"希望你以后的每天也天天开心！",Toast.LENGTH_LONG).show();
                        if(list[which]=="难过")
                            Toast.makeText(MainActivity.this,"安慰安慰难过的你",Toast.LENGTH_LONG).show();
                        if(list[which]=="郁闷")
                            Toast.makeText(MainActivity.this,"别担心，一切都会好起来的！",Toast.LENGTH_LONG).show();
                        if(list[which]=="生气")
                            Toast.makeText(MainActivity.this,"给自己一点时间冷静下来吧！",Toast.LENGTH_LONG).show();

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });



    }
}