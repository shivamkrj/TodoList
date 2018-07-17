package com.example.shivamkumar.todolist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
        if(toDo.getType()==1)
        {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.textViewTopic.setText(R.string.overdue);
            viewHolder.textViewTopic.setTextSize(16);
            viewHolder.textViewTopic.setGravity(View.TEXT_ALIGNMENT_CENTER);
            viewHolder.textViewTimeSet.setVisibility(View.GONE);
            viewHolder.textViewNote.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextColor(Color.parseColor("#303F9F"));
            CardView cardView = (CardView) viewHolder.itemView;
            cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
//            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT));
            viewHolder.textViewTopic.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(0);
            viewHolder.textViewTopic.setPadding(5,0,5,0);
            return;
        }else if(toDo.getType()==2)
        {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextSize(16);
            viewHolder.textViewTopic.setText(R.string.later);
            viewHolder.textViewTimeSet.setVisibility(View.GONE);
            viewHolder.textViewNote.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextColor(Color.parseColor("#303F9F"));
            CardView cardView = (CardView) viewHolder.itemView;
            cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT));
            viewHolder.textViewTopic.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(0);
            viewHolder.textViewTopic.setPadding(5,0,5,0);
            return;
        }else if(toDo.getType()==3)
        {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextSize(16);
            viewHolder.textViewTopic.setText(R.string.this_month);
            viewHolder.textViewTimeSet.setVisibility(View.GONE);
            viewHolder.textViewNote.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextColor(Color.parseColor("#303F9F"));
            CardView cardView = (CardView) viewHolder.itemView;
            cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT));
            viewHolder.textViewTopic.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(0);
            viewHolder.textViewTopic.setPadding(5,0,5,0);
            return;
        }else if(toDo.getType()==4)
        {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.textViewTopic.setText(R.string.this_week);
            viewHolder.textViewTopic.setTextSize(16);
            viewHolder.textViewTimeSet.setVisibility(View.GONE);
            viewHolder.textViewNote.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextColor(Color.parseColor("#303F9F"));
            CardView cardView = (CardView) viewHolder.itemView;
            cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.textViewTopic.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(0);
            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT));
            viewHolder.textViewTopic.setPadding(5,0,5,0);
            return;
        }else if(toDo.getType()==5)
        {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.textViewTopic.setText(R.string.today);
            viewHolder.textViewTopic.setTextSize(16);
            viewHolder.textViewTimeSet.setVisibility(View.GONE);
            viewHolder.textViewNote.setVisibility(View.GONE);
            viewHolder.textViewTopic.setTextColor(Color.parseColor("#303F9F"));
            CardView cardView = (CardView) viewHolder.itemView;
            cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
          //  cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT));
            viewHolder.textViewTopic.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(0);
            viewHolder.textViewTopic.setPadding(5,0,5,0);
            return;
        }else if(toDo.getType()==20){

        }else{
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.textViewTimeSet.setVisibility(View.VISIBLE);
            viewHolder.textViewNote.setVisibility(View.VISIBLE);
            viewHolder.textViewTopic.setTextColor(Color.parseColor("#090808"));
            CardView cardView = (CardView) viewHolder.itemView;
            cardView.setCardBackgroundColor(Color.parseColor("#d5ddc6"));
//            cardView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
//
//            CardView.LayoutParams layoutParams = (CardView.LayoutParams) cardView.getLayoutParams();
//            layoutParams.setMargins(0, 0, SystemHelper.dpToPx(1),);
//            cardView.setLayoutParams(layoutParams);



        }

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
