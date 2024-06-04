package com.example.mynotepad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private static final String DB_NAME = "database"; // 数据库名称
    private static final int DB_VERSION = 1; // 数据库版本号
    private MyData myData;
    private SQLiteDatabase db;
    private EditText noteTaxt;
    private Button saveButton,returnButton;
    private Spinner Dataspinner;
    private List<notepad> notepadList=new ArrayList();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        returnButton=findViewById(R.id.returnButton);
        saveButton=findViewById(R.id.saveButton);

        noteTaxt=findViewById(R.id.noteText);
        //spinner设置
        Dataspinner=findViewById(R.id.Dataspinner);
        /*  建立一个数据库
             DB_NAME = "database"; // 数据库名称
             DB_VERSION = 1; // 数据库版本号
         */
        myData = new MyData(this, DB_NAME, null, DB_VERSION);
        db=myData.getWritableDatabase();    //获取数据库实例

        //使程序保持日间模式，避免系统影响APP外观
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //接收传过来的用户名，用于后续数据存储
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String pass=intent.getStringExtra("pass");
        // 创建日期范围列表
        List<String> dateRange = new ArrayList<>();
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //设置时间表示格式
        dateRange.add(sdf.format(currentDate));     //将最新的日期添加入dataRange列表
        // 设置起始日期为当前日期的前一天，如果设置为当天会报错
        calendar.set(currentDate.getYear()+1900,currentDate.getMonth(),currentDate.getDate()-1);
        //可选择日期为 当前日期 到 一月前之间 的日期
        int i=31;
        //循环添加当前日期到一个月前之间的日期，例：2024-6-1 到 2024-5-1
        while (calendar.getTime().before(currentDate)&&i!=0) {
            dateRange.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DATE, -1);    //减少一天
            i--;    //减少一天
        }

        // 创建适配器，把dataRange中的内容添加到spinner中
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateRange);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Dataspinner.setAdapter(adapter);

        //返回主页
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddtoHome=new Intent(AddActivity.this,homeActivity.class);
                AddtoHome.putExtra("username", username);
                AddtoHome.putExtra("pass",pass);
                startActivity(AddtoHome);
            }
        });
        //添加日记功能
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取Dataspinner中的日期和noteTaxt中的日记内容
                String notedata=(String) Dataspinner.getSelectedItem();
                String note=noteTaxt.getText().toString();

                //添加日记内容到数据表notetable内
                if (notedata.equals("")||note.equals("")){  //判断日记内容或者是日期内容是否为空
                    Toast.makeText(AddActivity.this, "日记内容为空！", Toast.LENGTH_SHORT).show();
                }
                else {
                    //通过键值对把用户名name，日记日期notetable，日记内容note对应放入values中
                    ContentValues values = new ContentValues();
                    values.put("name",username);
                    values.put("notedata", notedata);
                    values.put("note", note);
                    //代码插入语句，notetable是表名，null是没有数据时插入的内容，values是插入的内容
                    db.insert("notetable", null, values);
                    Toast.makeText(AddActivity.this, "日记添加成功", Toast.LENGTH_SHORT).show();
                    //跳转到主页面
                    Intent AddtoHome=new Intent(AddActivity.this,homeActivity.class);
                    AddtoHome.putExtra("username", username);
                    AddtoHome.putExtra("pass",pass);
                    startActivity(AddtoHome);
                }

            }
        });
    }
}