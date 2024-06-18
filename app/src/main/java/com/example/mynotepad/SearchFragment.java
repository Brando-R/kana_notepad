package com.example.mynotepad;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private static final String DB_NAME = "database"; // 数据库名称
    private static final int DB_VERSION = 1; // 数据库版本号
    private MyData myData;
    private SQLiteDatabase db;
    private RecyclerView searchRecyclerView;
    private notepadAdapter notepadAdapter;
    private TextView noteText,dateText;
    private Button searchButton;
    private Spinner searchDataspinner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SearchFragment() {
        // Required empty public constructor
    }
    String username;
    String pass;
    public SearchFragment(String username,String pass,MyData myData) {    //用构造函数获取用户名username和数据库mydata
        // Required empty public constructor
        this.username=username;
        this.pass=pass;
        this.myData=myData;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // 设置应用为日间模式
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    private List<notepad> notepadList=new ArrayList<>();
    public void SearchNote(){
        //清空notepadList
        notepadList.clear();
        //搜索内容显示
        db=myData.getWritableDatabase();    //获取数据库实例
        String searchdata= (String) searchDataspinner.getSelectedItem();
        //利用sql语句寻找数据表notetable中name=username的内容
        String sql = "SELECT * FROM notetable where name=? and notedata=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{username,searchdata});   //username替换？的内容
        //如果找到的数据>0，则循环把每一行存储到List列表notepadList中，每下一条内容存储在0位置
        if (cursor.getCount() > 0)
            while (cursor.moveToNext()){
                @SuppressLint("Range")
                String date=cursor.getString(cursor.getColumnIndex("notedata")).toString();
                @SuppressLint("Range")
                String note=cursor.getString(cursor.getColumnIndex("note")).toString();
                notepad notepad=new notepad(date,note);
                notepadList.add(0,notepad);
            }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchDataspinner=view.findViewById(R.id.searchDataspinner);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dateRange);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchDataspinner.setAdapter(adapter);


        searchButton=view.findViewById(R.id.searchButton);
        searchRecyclerView=(RecyclerView) view.findViewById(R.id.searchRecyclerView);
        //使用布局管理器为RecyclerView 设置线性布局
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //跳转看到初始值
        SearchNote();
        // 创建一个 notepadAdapter 实例，并将 notepadList 作为参数传入
        notepadAdapter=new notepadAdapter(notepadList);
        //为 RecyclerView 设置适配器，将notepadAdapter 设置为其适配器
        searchRecyclerView.setAdapter(notepadAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchNote();
                // 创建一个 notepadAdapter 实例，并将 notepadList 作为参数传入
                notepadAdapter=new notepadAdapter(notepadList);
                //为 RecyclerView 设置适配器，将notepadAdapter 设置为其适配器
                searchRecyclerView.setAdapter(notepadAdapter);
                if (notepadList.isEmpty()){
                    Toast.makeText(getActivity(), "当日内容为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //判断日记列表是否为空
        if (!notepadList.isEmpty()){
            notepadAdapter.setOnClickListener(new notepadAdapter.OnClickListener(){
                @Override   //点击查看日记内容
                public void onClick(View itemView, int position) {

                    noteText=itemView.findViewById(R.id.NotetextView);
                    dateText=itemView.findViewById(R.id.DatetextView);
                    String note=noteText.getText().toString();
                    String notedate=dateText.getText().toString();

                    Intent RFToNote=new Intent(getActivity(), NoteActivity.class);
                    //带上日记内容和日记日期一起跳转
                    RFToNote.putExtra("note", note);
                    RFToNote.putExtra("notedate", notedate);
                    RFToNote.putExtra("username",username);
                    RFToNote.putExtra("pass",pass);
                    getActivity().startActivity(RFToNote);
                }

                @Override
                public void onLongClick(View itemView, int position) {

                    // 创建一个字符串数组，包含一个元素"delete"
                    String[] list={"delete"};
                    // 创建一个AlertDialog.Builder对象，用于构建对话框
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    // 设置对话框的多选项内容，传入list作为选项数据，null作为默认选中项，以及一个DialogInterface.OnMultiChoiceClickListener监听器
                    builder.setMultiChoiceItems(list, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            // 获取itemView中的NotetextView和DatetextView控件
                            TextView noteText = itemView.findViewById(R.id.NotetextView);
                            TextView dateText = itemView.findViewById(R.id.DatetextView);
                            // 获取noteText和dateText的文本内容
                            String note = noteText.getText().toString();
                            String notedate = dateText.getText().toString();
                            // 从数据库中删除符合条件的记录
                            db.delete("notetable","name=? and note=? and notedata=?",new String[]{username,note,notedate});
                            // 显示删除成功的提示信息
                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                            // 跳转到主页面
                            Intent NoteToHome = new Intent(getActivity(), homeActivity.class);
                            NoteToHome.putExtra("username", username);
                            NoteToHome.putExtra("pass", pass);
                            startActivity(NoteToHome);
                        }
                    });
                    // 显示对话框
                    builder.show();
                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}