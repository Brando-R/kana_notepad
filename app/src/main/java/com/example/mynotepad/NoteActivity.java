package com.example.mynotepad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
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

public class NoteActivity extends AppCompatActivity {
    private static final String DB_NAME = "database"; // 数据库名称
    private static final int DB_VERSION = 1; // 数据库版本号
    private MyData myData;
    private SQLiteDatabase db;
    private Button returnButton,alterButton;
    private TextView dateText;
    private EditText noteText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);
        //使程序保持日间模式
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        noteText=findViewById(R.id.Notetext);
        dateText=findViewById(R.id.Datetext);
        returnButton=findViewById(R.id.returnButton);
        alterButton=findViewById(R.id.alterButton);

        myData=new MyData(this,DB_NAME,null,DB_VERSION);
        db=myData.getWritableDatabase();    //获取数据库实例

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String notedate=intent.getStringExtra("notedate");
        String username=intent.getStringExtra("username");
        String pass=intent.getStringExtra("pass");
        noteText.setText(note);
        dateText.setText(notedate);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NoteToHome=new Intent(NoteActivity.this,homeActivity.class);
                NoteToHome.putExtra("username",username);
                NoteToHome.putExtra("pass",pass);
                startActivity(NoteToHome);
            }
        });
        alterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newnote=noteText.getText().toString();
                if (!newnote.equals("")){
                    ContentValues values=new ContentValues();
                    values.put("note",newnote);
                    db.update("notetable",values,"name=? and note=? and notedata=?",new String[]{username,note,notedate});

                    //跳转到主页面
                    Intent NoteToHome=new Intent(NoteActivity.this,homeActivity.class);
                    NoteToHome.putExtra("username",username);
                    NoteToHome.putExtra("pass",pass);
                    startActivity(NoteToHome);
                }else
                    Toast.makeText(NoteActivity.this, "日记内容为空", Toast.LENGTH_SHORT).show();


            }
        });
    }
}