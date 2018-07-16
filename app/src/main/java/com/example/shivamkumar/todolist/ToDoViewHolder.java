package com.example.shivamkumar.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ToDoViewHolder extends RecyclerView.ViewHolder{
    TextView textViewTopic;
    TextView textViewNote;
    TextView textViewTimeSet;
    CheckBox checkBox;
    View itemView;

    public ToDoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        textViewTopic=itemView.findViewById(R.id.textViewTopic);
        textViewNote= itemView.findViewById(R.id.textViewNote);
        textViewTimeSet= itemView.findViewById(R.id.textViewTimeSet);
        checkBox = itemView.findViewById(R.id.checkbox);
    }
}
