package com.example.shivamkumar.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

public class ToDoRecyclerAdapter extends RecyclerView.Adapter<ToDoViewHolder> {

    ArrayList<ToDo> list;
    ToDoClickListener listener;
    Context context;
    CheckBoxListener checkBoxListener;

    public ToDoRecyclerAdapter(Context context, ArrayList<ToDo> list, ToDoClickListener listener,CheckBoxListener checkBoxListener) {
        this.list = list;
        this.listener = listener;
        this.context = context;
        this.checkBoxListener = checkBoxListener;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout = inflater.inflate(R.layout.row_layout,viewGroup,false);
        return new ToDoViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final ToDoViewHolder viewHolder, int i) {
        ToDo toDo = list.get(i);
        viewHolder.textViewTopic.setText(toDo.getTopic());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(toDo.getTimeInMillis());
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        viewHolder.textViewNote.setText(day+"/"+month+"/"+year);
        viewHolder.textViewTimeSet.setText(hour+":"+minute);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view,viewHolder.getAdapterPosition());
            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxListener.onClick(view,viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
