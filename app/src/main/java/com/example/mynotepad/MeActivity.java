package com.example.mynotepad;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MeActivity extends AppCompatActivity {
    private TextView usernameText,userpassText;
    private RelativeLayout returnHome,returnMain,ModifyAccount,AboutWe;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_me);
        usernameText=findViewById(R.id.meusername);
        userpassText=findViewById(R.id.userpass);
        returnHome=findViewById(R.id.returnHome);
        returnMain=findViewById(R.id.returnMain);
        ModifyAccount=findViewById(R.id.ModifyAccount);
        AboutWe=findViewById(R.id.AboutWe);
        //使程序保持日间模式
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Intent intent=getIntent();
        String username= intent.getStringExtra("username");
        String pass=intent.getStringExtra("pass");
        usernameText.setText(username);
        userpassText.setText(pass);

        ModifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MetoAccout=new Intent(MeActivity.this,AccountActivity.class);
                MetoAccout.putExtra("username", username);
                MetoAccout.putExtra("pass",pass);
                startActivity(MetoAccout);

            }
        });
        returnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MetoMain=new Intent(MeActivity.this,MainActivity.class);
                startActivity(MetoMain);
                Toast.makeText(MeActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
            }
        });
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MetoHome=new Intent(MeActivity.this,homeActivity.class);
                MetoHome.putExtra("username", username);
                MetoHome.putExtra("pass", pass);
                startActivity(MetoHome);
                Toast.makeText(MeActivity.this, "返回主页成功", Toast.LENGTH_SHORT).show();
            }
        });
        AboutWe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str="      本产品作为移动开发作业被开发出来，尽管作为一个操作简单的APP，我仍想为一些需要的人提供一份功能介绍：" +"\n"
                        +"1.添加日记：点击主页下方‘+’圆型按钮即可添加新日记"+"\n"
                        +"2.查看或修改日记：点击主页任意一篇日记，即可进入查看或修改页面"+"\n"
                        +"3.删除日记：长按主页任意一篇想要删除的日记，即会跳出‘delete’弹窗，点击即可删除"+"\n"
                        +"4.修改用户名或密码：在主页点击进入Me页面，点击\"修改密码或用户名\"进入修改页面，输入正确的旧用户名和旧密码"+"\n"+"\n"
                        +"      最后，希望该介绍对您有用！感谢您的使用！";
                // 创建一个AlertDialog.Builder对象，用于构建对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MeActivity.this);
                // 设置对话框的标题
                builder.setTitle("功能介绍");
                // 设置对话框的内容，str为要显示的文本内容
                builder.setMessage(str);
                // 设置对话框的正面按钮（即“收到”按钮），点击后不执行任何操作（传入null）
                builder.setPositiveButton("收到", null);
                // 显示对话框
                builder.show();

            }
        });
    }
}