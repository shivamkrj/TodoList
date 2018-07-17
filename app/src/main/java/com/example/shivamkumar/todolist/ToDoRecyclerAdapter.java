package com.example.shivamkumar.todolist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
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
    public int spToPx(float sp, Context context) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
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
        if(toDo.getType()<7&&toDo.getType()>0)
        {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextSize(16);
            viewHolder.textViewTopic.setGravity(View.TEXT_ALIGNMENT_CENTER);
            viewHolder.textViewTimeSet.setVisibility(View.GONE);
            viewHolder.textViewNote.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextColor(Color.parseColor("#303F9F"));
            CardView cardView = (CardView) viewHolder.itemView;
            cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
//            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT));
            viewHolder.textViewTopic.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(0);
            viewHolder.textViewTopic.setPadding(5,0,5,0);

            if(toDo.getType()==1){
                viewHolder.textViewTopic.setText(R.string.overdue);
            }else if(toDo.getType()==2){
                viewHolder.textViewTopic.setText(R.string.later);
            }else if(toDo.getType()==3){
                viewHolder.textViewTopic.setText(R.string.this_month);
            }else if(toDo.getType()==4){
                viewHolder.textViewTopic.setText(R.string.this_week);
            }else if(toDo.getType()==5){
                viewHolder.textViewTopic.setText(R.string.today);
            }else if(toDo.getType()==6){
                viewHolder.textViewTopic.setText(R.string.noDate);
            }
            return;
        }
        if(toDo.getType()!=20){
            viewHolder.textViewTimeSet.setVisibility(View.VISIBLE);
            viewHolder.textViewNote.setVisibility(View.VISIBLE);
        }else{
            viewHolder.textViewTimeSet.setVisibility(View.GONE);
            viewHolder.textViewNote.setVisibility(View.GONE);
        }
        viewHolder.checkBox.setVisibility(View.VISIBLE);
        viewHolder.textViewTopic.setTextColor(Color.parseColor("#090808"));
        CardView cardView = (CardView) viewHolder.itemView;
        cardView.setCardBackgroundColor(Color.parseColor("#d5ddc6"));
        viewHolder.textViewTopic.setTextSize(spToPx(12,context));

        viewHolder.textViewTopic.setText(toDo.getTopic());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(toDo.getTimeInMillis());
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int second = calendar.get(Calendar.SECOND);
        if(hour==23&&minute==59&&second==59){
            viewHolder.textViewNote.setText(day+"/"+month+"/"+year);
            viewHolder.textViewTimeSet.setText("");
        }else{
            viewHolder.textViewNote.setText(day+"/"+month+"/"+year);
            viewHolder.textViewTimeSet.setText(hour+":"+minute);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view,viewHolder.getAdapterPosition());
            }
        });
        viewHolder.checkBox.setChecked(false);
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
