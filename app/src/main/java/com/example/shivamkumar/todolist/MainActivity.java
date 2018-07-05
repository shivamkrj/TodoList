package com.example.shivamkumar.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    ListView listView;
    ArrayList<ToDo> items;
    ToDoAdapter adapter;
    SharedPreferences sharedPreferences;
    int current=0;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                newNote();
            }
        });
        items = new ArrayList<ToDo>();
        listView=findViewById(R.id.list_item);
        adapter= new ToDoAdapter(this,items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
//        sharedPreferences= getSharedPreferences("my_shared_pref",MODE_PRIVATE);
//        pos=sharedPreferences.getInt("POS",-1);
//        if(pos!=-1)
//            setFromSharedPreference();

        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,null,null,null,null,Contract.ToDo.COLUMN_ID);
        while (cursor.moveToNext()){
           // Toast.makeText(this,"inside while cursor",Toast.LENGTH_SHORT).show();
            String note = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
            String topic = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_ID));
            ToDo toDo = new ToDo(topic,note);
            String time = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
            toDo.setTime(time);
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
        String topic,note,time;

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
                        time = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
                        ToDo toDo = new ToDo(topic,note);
                        toDo.setId(id);
                        toDo.setTime(time);
                       // Toast.makeText(this,"icur "+topic,Toast.LENGTH_SHORT).show();
                        items.add(toDo);
                        adapter.notifyDataSetChanged();
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
                    time = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
                    ToDo toDo = new ToDo(topic,note);
                    toDo.setId(id);
                 //   Toast.makeText(this,topic+" "+note,Toast.LENGTH_SHORT).show();
                    items.get(current).setTopic(topic);
                    items.get(current).setNote(note);
                    items.get(current).setId(id);
                    items.get(current).setTime(time);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        openDescription(i);
    }

    private void openDescription(int i) {

        long id = items.get(i).getId();
        current = i;
        Intent intent = new Intent(this,Description2.class);
        intent.putExtra("ID",id);
        startActivityForResult(intent,2);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        delete(i);
        return true;
    }

    private void delete(int i) {

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
                adapter.notifyDataSetChanged();

                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(MainActivity.this);
                SQLiteDatabase database = openHelper.getWritableDatabase();
                String arg[]={id+""};
                database.delete(Contract.ToDo.TODO_TABLE_NAME,Contract.ToDo.COLUMN_ID+ " = ?",arg);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
    }
}
