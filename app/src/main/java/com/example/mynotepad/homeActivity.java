package com.example.mynotepad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotepad.R.id;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class homeActivity extends FragmentActivity {
    private static final String DB_NAME = "database"; // 数据库名称
    private static final int DB_VERSION = 1; // 数据库版本号

    //private List<notepad> notepadList=new ArrayList<>();
    private FloatingActionButton AddButton;
    private Button ToMeButton,homeButton,noteButton;

    @SuppressLint("MissingInflatedId")
    private Button CountdownButton,NotepadButton;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ToMeButton=findViewById(id.ToMeButton);
        //接收跳转数据，main传来username和pass
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String pass=intent.getStringExtra("pass");

        BlankFragment blankFragment=new BlankFragment(username);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayout,blankFragment);
        fragmentTransaction.commit();



        homeButton=findViewById(id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlankFragment blankFragment=new BlankFragment(username);
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FrameLayout,blankFragment);
                fragmentTransaction.commit();
            }
        });
        MyData myData=new MyData(this, DB_NAME, null, DB_VERSION);
        noteButton=findViewById(id.noteButton);
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个RecyclerFragment实例，传入用户名、密码和MyData对象
                RecyclerFragment recyclerFragment = new RecyclerFragment(username, pass, myData);
                // 获取FragmentManager实例
                FragmentManager fragmentManager = getSupportFragmentManager();
                // 开始一个FragmentTransaction事务
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // 将RecyclerFragment替换到FrameLayout中
                fragmentTransaction.replace(id.FrameLayout, recyclerFragment);
                // 提交事务
                fragmentTransaction.commit();
            }
        });

        AddButton=findViewById(id.floatingActionButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent HometoAdd=new Intent(homeActivity.this, AddActivity.class);
                HometoAdd.putExtra("username", username);
                HometoAdd.putExtra("pass",pass);
                startActivity(HometoAdd);
            }
        });
        ToMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HomeToMe=new Intent(homeActivity.this, MeActivity.class);
                HomeToMe.putExtra("username", username);  //带上用户名一起跳转
                HomeToMe.putExtra("pass",pass);
                startActivity(HomeToMe);
            }
        });

   }


}
