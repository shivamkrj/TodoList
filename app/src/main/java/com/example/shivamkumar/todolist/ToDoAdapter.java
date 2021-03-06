package com.example.shivamkumar.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends ArrayAdapter {

    LayoutInflater layoutInflater;
    ArrayList<ToDo> items;

    public ToDoAdapter(@NonNull Context context, @NonNull ArrayList<ToDo> items) {
        super(context, 0, items);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output=convertView;
        if(output==null){
            output=layoutInflater.inflate(R.layout.row_layout,parent,false);

            ToDoViewHolder toDoViewHolder = new ToDoViewHolder();
            toDoViewHolder.textViewNote=output.findViewById(R.id.textViewNote);
            toDoViewHolder.textViewTopic=output.findViewById(R.id.textViewTopic);
            toDoViewHolder.textViewTimeSet=output.findViewById(R.id.textViewTimeSet);

            output.setTag(toDoViewHolder);
        }
        ToDoViewHolder viewHolder =(ToDoViewHolder)output.getTag();
        ToDo toDo = items.get(position);
        viewHolder.textViewTopic.setText(toDo.getTopic());
        viewHolder.textViewNote.setText(toDo.getTime());
        viewHolder.textViewTimeSet.setText(toDo.getTimeSet());
        return output;
    }
}
