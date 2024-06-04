package com.example.mynotepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class notepadAdapter extends RecyclerView.Adapter<notepadAdapter.ViewHolder>{
    private List<notepad> notepadList;

    /**
     * adapter 中item点击事件的监听对象.
     */
    private OnClickListener onClickListener;

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public notepadAdapter(List<notepad> notepad){
        notepadList=notepad;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView note;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.DatetextView);
            note=itemView.findViewById(R.id.NotetextView);
        }
        public TextView getNote(){
            return note;
        }
        public TextView getDate(){
            return date;
        }
    }


    @NonNull
    @Override
    public notepadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout,parent,false);
//        View itemView = View.inflate(parent.getContext(), R.layout.layout, null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull notepadAdapter.ViewHolder holder, int position) {
        notepad notepad=notepadList.get(position);
        holder.note.setText(notepad.getNote());
        holder.date.setText(notepad.getDate());

        final View itemView = holder.itemView;
        TextView notetextView=itemView.findViewById(R.id.NotetextView);

        //设置点击事件
        notetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(itemView, holder.getLayoutPosition());
                }
            }
        });
        //设置长按事件
        notetextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 设置长按事件并回调给页面
                if (onClickListener != null) {
                    onClickListener.onLongClick(itemView, holder.getLayoutPosition());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notepadList.size();
    }
    interface OnClickListener {
        /**
         * 点击事件.
         */
        void onClick(View itemView, int position);

        /**
         * 长按事件.
         */
        void onLongClick(View itemView, int position);
    }

}