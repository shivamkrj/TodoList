package com.example.shivamkumar.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddNote extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    EditText editTextTopic;
    EditText editTextNote;
    String time;
    boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextNote = findViewById(R.id.editTextNote);
        editTextTopic = findViewById(R.id.editTextTopic);
        Intent intent = getIntent();
        String num= intent.getStringExtra("num");
        String action = intent.getAction();
        if(num!=null){
            editTextTopic.setText(num);
            String text = intent.getStringExtra("message");
            editTextNote.setText(text);
            flag=true;
        }else if(action==Intent.ACTION_SEND){
            flag=true;
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            if(text!=null) {
                editTextNote.setText(text);
                int i;
                for (i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == ' ') {
                        break;
                    }
                }
                text = text.substring(0, i);
                editTextTopic.setText(text);
            }else{
                editTextTopic.setText("gta 5");
                editTextNote.setText("got from outside");
            }
        }
    }

    public void button(View view) {
        Intent data = getIntent();
        String topic = editTextTopic.getText().toString();
        String note= editTextNote.getText().toString();
        if(topic!=null) {

            ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
            SQLiteDatabase database = openHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.ToDo.COLUMN_TOPIC,topic);
            contentValues.put(Contract.ToDo.COLUMN_NOTE,note);
            contentValues.put(Contract.ToDo.COLUMN_TIME,time);

            long id = database.insert(Contract.ToDo.TODO_TABLE_NAME,null,contentValues);
            Toast.makeText(this,id+" inside "+topic, Toast.LENGTH_SHORT).show();
            if(flag){
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra("ID",id);
                startActivity(intent);
            }
            if(id>-1L){
                data.putExtra("ID",id);
                setResult(2,data);
                finish();
            }else{

                //code for new entry
                setResult(5,data);
                finish();
            }
        }
    }
    public void timeSet(View view){
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(),"time picker");
    }
    public void timer(View view){
        DialogFragment dataPicker = new DatePickerFragment();
        dataPicker.show(getSupportFragmentManager(),"date picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String s= i2+"/";
        s+=i1+"/";
        s+=i+"";
        TextView textView =findViewById(R.id.timerText);
        textView.setText(s);
        time = s;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String s;
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
        TextView t = findViewById(R.id.textView4);
        t.setText(s);
    }
}