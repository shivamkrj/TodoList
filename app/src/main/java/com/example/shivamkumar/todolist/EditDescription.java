package com.example.shivamkumar.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class EditDescription extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText t;
    EditText n;
    TextView timer;
    TextView timerSet;
    String topic;
    String note;
    long time;
    long id;
    int h,m,d,mon,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);

        Intent data= getIntent();
        id = data.getLongExtra("ID",-1);
        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        String[] arg = {id+""};
        Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,Contract.ToDo.COLUMN_ID+ " = ?",arg,null,null,null);
        while (cursor.moveToNext()){
            topic = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
            note = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
            time = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
        }
        cursor.close();

        t=findViewById(R.id.editText);
        n=findViewById(R.id.editText2);
        timer=findViewById(R.id.editText3);
        timerSet=findViewById(R.id.editText4);
        t.setText(topic);
        n.setText(note);
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

    public void button(View view) {
        topic=t.getText().toString();
        note=n.getText().toString();

        Intent data = getIntent();

        Calendar calendar = Calendar.getInstance();
        calendar.set(y,mon,d,h,m);
        time=calendar.getTimeInMillis();

        if(topic!=null) {

            ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
            SQLiteDatabase database = openHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.ToDo.COLUMN_TOPIC,topic);
            contentValues.put(Contract.ToDo.COLUMN_NOTE,note);
            contentValues.put(Contract.ToDo.COLUMN_TIME,time);



            long preId=id;
            String arg[]={id+""};
            long id = database.update(Contract.ToDo.TODO_TABLE_NAME,contentValues,Contract.ToDo.COLUMN_ID+" = ?",arg);
            if(id>-1L){
                data.putExtra("ID",preId);
                setResult(3,data);
                finish();
            }
            else {
                data.putExtra("ID", id);
                setResult(2, data);
                finish();
            }
        }
    }
    public void timer(View view){
        DialogFragment dataPickerr = new DatePickerFragment();
        dataPickerr.show(getSupportFragmentManager(),"date picker");
    }
    public void timerSet(View view){
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(),"time picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        y=i;
        mon=i1;
        d=i2;
        String s= i2+"/";
        s+=i1+"/";
        s+=i+"";
        timer.setText(s);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String s;
        h=hour;
        m=minute;

        if(DateFormat.is24HourFormat(this)){
            s = hour+":";
            s+=minute+"";
        }else{
            if(hour<=12){
                if(hour>=1) {
                    s = hour + ":";
                    s += minute + " am";
                }else{
                    s = hour+12 + ":";
                    s += minute + " am";
                }
            }else{
                s = hour-12+":";
                s+=minute+" pm";
            }
        }

        timerSet.setText(s);
    }
}
