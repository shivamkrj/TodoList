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
    ArrayList<ToDo> items;
    ToDoRecyclerAdapter adapter;
    int current=0;
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
                delete(position,view);
            }
        });

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);
     //   listView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL|DividerItemDecoration.VERTICAL));

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromviewHolder, @NonNull RecyclerView.ViewHolder toviewHolder) {
                int from = fromviewHolder.getAdapterPosition();
                int to = toviewHolder.getAdapterPosition();

                ToDo toDo = items.get(from);
                items.remove(from);
                items.add(to,toDo);
                adapter.notifyItemMoved(from,to
                );
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                items.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        touchHelper.attachToRecyclerView(listView);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(listView);

        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,null,null,null,null,Contract.ToDo.COLUMN_ID);
        while (cursor.moveToNext()){
            String note = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
            String topic = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_ID));
            ToDo toDo = new ToDo(topic,note);
            long time = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
            toDo.setTimeInMillis(time);
            toDo.setId(id);
            items.add(toDo);
            adapter.notifyDataSetChanged();
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

                   // Toast.makeText(this,"inside rese code 2",Toast.LENGTH_SHORT).show();
                    ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
                    SQLiteDatabase database = openHelper.getReadableDatabase();
                    String arg[]= {id+""};
                    Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,Contract.ToDo.COLUMN_ID+" = ?",arg,null,null,null);
                    while (cursor.moveToNext()){
                       // Toast.makeText(this,"inside while cursor",Toast.LENGTH_SHORT).show();
                        note = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
                        topic = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
                        time = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
                        ToDo toDo = new ToDo(topic,note);
                        toDo.setId(id);
                        toDo.setTimeInMillis(time);
                       // Toast.makeText(this,"icur "+topic,Toast.LENGTH_SHORT).show();
                        items.add(toDo);
                        adapter.notifyDataSetChanged();

                        //setting alarm
                        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                        Intent intent = new Intent(this,MyReceiver.class);
                        intent.putExtra("ID",id);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,2,intent,0);

                        manager.set(AlarmManager.RTC_WAKEUP,time,pendingIntent);
                    }
                }
            }
        }else if(requestCode==2){
            if(resultCode==4){
                long id = data.getLongExtra("ID",-1);
               // Toast.makeText(this,"id   "+id,Toast.LENGTH_SHORT).show();
                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
                SQLiteDatabase database = openHelper.getReadableDatabase();
                //id-=1;
                String arg[]= {id +""};
                Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,Contract.ToDo.COLUMN_ID+" = ?",arg,null,null,null);
                while (cursor.moveToNext()){
                  //  Toast.makeText(this,"inside while cursor",Toast.LENGTH_SHORT).show();
                    note = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
                    topic = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
                    time = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
                    ToDo toDo = new ToDo(topic,note);
                    toDo.setId(id);
                 //   Toast.makeText(this,topic+" "+note,Toast.LENGTH_SHORT).show();
                    items.get(current).setTopic(topic);
                    items.get(current).setNote(note);
                    items.get(current).setId(id);
                    items.get(current).setTimeInMillis(time);
                    adapter.notifyDataSetChanged();
                }
                openDescription(current);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
          //  textView.setText("Hello Brother");
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

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                CheckBox checkBox = (CheckBox)view;
                checkBox.setChecked(false);
            }
        });

        AlertDialog dialog =builder.create();
        dialog.show();
    }
}
