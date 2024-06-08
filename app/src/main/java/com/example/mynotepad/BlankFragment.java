package com.example.mynotepad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BlankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private String username;

    public BlankFragment() {
        // Required empty public constructor
    }
    public BlankFragment(String username) {
        // Required empty public constructor
        this.username=username;
    }
    private TextView usernameText,dateText,myText;
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameText = view.findViewById(R.id.usernametext);
        usernameText.setText("Hello！" + username);

        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始计数，所以需要加1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 格式化日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        //添加日期
        dateText = view.findViewById(R.id.datetext);
        String stu = formattedDate;
        dateText.setText(stu);

        //语录
        myText = view.findViewById(R.id.mytext);
        String stu1="呐，知道么，樱花飘落的速度，\n是每秒五厘米哦~\n你可以期待太阳从东方升起，\n而风却随心所欲地从四面八方吹来。";
        String stu2="不要等到明天再去幸福，\n" +
                "不要等到未来去实现某一个梦想，\n" +
                "因为当你说未来要实现某一个梦想的时候，\n" +
                "未来永远不来，所以你永远不会去实现那个梦想，\n" +
                "所以不要等到明天!不要等到未来！\n" +
                "就从现在开始，就从此时此刻开始，\n" +
                "让自己做一个幸福的人！\n" +
                "                                陈果";
        myText.setText(stu2);

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
}