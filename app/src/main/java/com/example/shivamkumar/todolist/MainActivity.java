package com.example.shivamkumar.todolist;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    RecyclerView listView;
    ToDoRecyclerAdapter adapter;
    ArrayList<ToDo> items;
    int current=0;
    public static int textSize = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });

        items = new ArrayList<ToDo>();
        listView=findViewById(R.id.recyclerView);
        adapter= new ToDoRecyclerAdapter(this, items, new ToDoClickListener() {
            @Override
            public void onClick(View view, int position) {
                openDescription(position);
            }
        }, new CheckBoxListener() {
            @Override
            public void onClick(View view, int position) {
                deleteFromButton(position,view);
            }
        });

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);
     //   listView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL|DividerItemDecoration.VERTICAL));

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromviewHolder, @NonNull RecyclerView.ViewHolder toviewHolder) {

                int from = fromviewHolder.getAdapterPosition();
                int to = toviewHolder.getAdapterPosition();

                ToDo toDo = items.get(from);
                if(toDo.getType()>0&&toDo.getType()<7){
                    return false;
                }
                items.remove(from);
                items.add(to,toDo);
                adapter.notifyItemMoved(from,to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//                items.remove(viewHolder.getAdapterPosition());
//                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                ToDo toDo = items.get(viewHolder.getAdapterPosition());
                if(toDo.getType()>0&&toDo.getType()<7){
                    fetchData(0);
                    return;
                }
                delete(viewHolder.getAdapterPosition(), viewHolder.itemView);
            }
        });
        touchHelper.attachToRecyclerView(listView);
//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(listView);

        fetchData(0);
    }

    private void fetchData(long idd) {
        int flag = 11;
        items.clear();
        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,null,null,null,null,Contract.ToDo.COLUMN_TIME);
        while (cursor.moveToNext()){
            String note = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
            String topic = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_ID));
            ToDo toDo = new ToDo(topic,note);
            long time = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
            toDo.setTimeInMillis(time);
            if(time==0){
                toDo.setType(20);
                if(flag==11){
                    flag=10;
                    ToDo toDo1 = new ToDo(6);
                    items.add(toDo1);
                    adapter.notifyDataSetChanged();
                }
                items.add(toDo);
                adapter.notifyDataSetChanged();
            }
            toDo.setId(id);
            long curTime = System.currentTimeMillis();
            long differenceInTime = time-curTime;
            if(time==0){

            }
            else if(differenceInTime<=0){
                if(flag==10){
                    flag = 6;
                    ToDo toDo1 = new ToDo(1);
                    items.add(toDo1);
                    adapter.notifyDataSetChanged();
                }
                items.add(toDo);
                adapter.notifyDataSetChanged();
            }else{
                Calendar calendar= Calendar.getInstance();
                calendar.setTimeInMillis(differenceInTime);
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                Log.d("timemachine",y+" "+m+" "+day+" " +h+" "+min);
                if(y>1970||m>0){//later
                    if(flag>2){
                        flag = 2;
                        ToDo toDo1 = new ToDo(2);
                        items.add(toDo1);
                        adapter.notifyDataSetChanged();
                    }
                }else if(day>7){//this month
                    if(flag>3){
                        flag=3;
                        ToDo toDo1 = new ToDo(3);
                        items.add(toDo1);
                        adapter.notifyDataSetChanged();
                    }
                }else if(day>1){//this week
                    if(flag>4){
                        flag=4;
                        ToDo toDo1 = new ToDo(4);
                        items.add(toDo1);
                        adapter.notifyDataSetChanged();
                    }
                }else {//today
                    if(flag>5){
                        flag=5;
                        ToDo toDo1 = new ToDo(5);
                        items.add(toDo1);
                        adapter.notifyDataSetChanged();
                    }
                }
                items.add(toDo);
                adapter.notifyDataSetChanged();
            }
        }
        if(idd>0){
            //setting alarm
            String arg[]= {idd+""};
            cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,Contract.ToDo.COLUMN_ID+" = ?",arg,null,null,null);
            while (cursor.moveToNext()){
                long time2 = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
                int iddd = (int)idd;
                AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(this,MyReceiver.class);
                intent.putExtra("ID",idd);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,iddd,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                manager.set(AlarmManager.RTC_WAKEUP,time2,pendingIntent);
            }
        }
    }

    public void newNote() {
        Intent intent = new Intent(this,AddNote.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String topic,note;
        long time;

        if(requestCode==1){
            if(resultCode==2){
                long id = data.getLongExtra("ID",-1);
                if(id>=0){
                    fetchData(id);
                }
            }
        }else if(requestCode==2){
            if(resultCode==4){
                long id = data.getLongExtra("ID",-1);

                //updating alarm
                String arg[]= {id+""};
                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
                SQLiteDatabase database = openHelper.getReadableDatabase();
                Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,Contract.ToDo.COLUMN_ID+" = ?",arg,null,null,null);
                while (cursor.moveToNext()){
                    long time2 = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
                    int t = (int)time2/1000;
                    AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(this,MyReceiver.class);
                    intent.putExtra("ID",id);
                    int idd =(int)id;
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this,idd,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                    manager.set(AlarmManager.RTC_WAKEUP,time2,pendingIntent);
                }

                fetchData(id);
              //  openDescription(current);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sizetwelve) {
            textSize=12;
            fetchData(0);
            return true;
        }else if (id == R.id.sizefourteen) {
            textSize=15;
            fetchData(0);
            return true;
        }else if (id == R.id.sizenine) {
            textSize=9;
            fetchData(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDescription(int i) {
        long id = items.get(i).getId();
        current = i;
        Intent intent = new Intent(this,Description2.class);
        intent.putExtra("ID",id);
        startActivityForResult(intent,2);
    }

    public void deleteFromButton(int position,View view){
        Toast.makeText(MainActivity.this,"Task Completed",Toast.LENGTH_SHORT).show();
        long id= items.get(position).getId();
        int k=items.get(position).getPosition();
        items.remove(position);
        adapter.notifyItemRemoved(position);

        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(MainActivity.this);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        String arg[]={id+""};
        database.delete(Contract.ToDo.TODO_TABLE_NAME,Contract.ToDo.COLUMN_ID+ " = ?",arg);

        items.clear();
        adapter.notifyDataSetChanged();
        fetchData(0);

    }

    private void delete(int i, final View view) {
        final int position= i;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Do you really want to delete this note");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                long id= items.get(position).getId();
                int k=items.get(position).getPosition();
                items.remove(position);
                adapter.notifyItemRemoved(position);

                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(MainActivity.this);
                SQLiteDatabase database = openHelper.getWritableDatabase();
                String arg[]={id+""};
                database.delete(Contract.ToDo.TODO_TABLE_NAME,Contract.ToDo.COLUMN_ID+ " = ?",arg);
                items.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Task Completed",Toast.LENGTH_SHORT).show();
                fetchData(0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fetchData(0);
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
    }
}
