package com.example.shivamkumar.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Description2 extends Activity {

    TextView t;
    TextView n;
    TextView timer;
    TextView timerSet;
    Long time=0L;
    String topic;
    String note;
    long id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                edit();
            }
        });

        Intent data= getIntent();
        id = data.getLongExtra("ID",-1);
        if(id==-1)
            return;
        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        String[] arg = {id+""};
        Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,Contract.ToDo.COLUMN_ID+ " = ?",arg,null,null,null);
        while (cursor.moveToNext()){
            topic = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
            note = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
            time = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
        }
        t=findViewById(R.id.textView);
        n=findViewById(R.id.textView2);
        timer=findViewById(R.id.textView3);
        timerSet=findViewById(R.id.textViewTims);

        if(time==0){
            timerSet.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
        }else{
            Calendar calendar =Calendar.getInstance();
            calendar.setTimeInMillis(time);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            if(hour==23&&minute==59&&second==59)
                timerSet.setVisibility(View.GONE);
        }
        if(note==null||note.length()==0){
            n.setVisibility(View.GONE);
        }

        t.setText(topic);
        n.setText(note);

        int h,m,d,mon,y;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        h=calendar.get(Calendar.HOUR_OF_DAY);
        m=calendar.get(Calendar.MINUTE);
        d=calendar.get(Calendar.DAY_OF_MONTH);
        mon=calendar.get(Calendar.MONTH);
        y=calendar.get(Calendar.YEAR);
        timerSet.setText(h+":"+m);
        timer.setText(d + "/"  + mon + "/" + y);

    }

    private void edit() {
        Intent intent = new Intent(this,EditDescription.class);
        intent.putExtra("ID",id);
        startActivityForResult(intent,2);
    }

    public void edit(View view) {
        Intent intent = new Intent(this,EditDescription.class);
        intent.putExtra("ID",id);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==3){
            long id = data.getLongExtra("ID",-1);
            if(id>=0){

                // Toast.makeText(this,"inside rese code 2",Toast.LENGTH_SHORT).show();
//                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
//                SQLiteDatabase database = openHelper.getReadableDatabase();
//                String arg[]= {id+""};
//                Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,Contract.ToDo.COLUMN_ID+" = ?",arg,null,null,null);
//                while (cursor.moveToNext()){
//                 //   Toast.makeText(this,"inside while cursor",Toast.LENGTH_SHORT).show();
//                    note = cursor.getColumnName(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
//                    topic = cursor.getColumnName(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
//                }
                data.putExtra("ID",id);
                setResult(4,data);
                finish();
            }

        }
    }
}
