package com.example.shivamkumar.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class ToDoOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todoList_db";
    public static final int VERSION = 1;

    private static ToDoOpenHelper instance;

    public static ToDoOpenHelper getInstance(Context context){
        if(instance==null)
            instance = new ToDoOpenHelper(context.getApplicationContext());
        return instance;
    }

    private ToDoOpenHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String todoSQL = "CREATE TABLE "+Contract.ToDo.TODO_TABLE_NAME+
                " ( "+Contract.ToDo.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+
                Contract.ToDo.COLUMN_TOPIC+" TEXT , "+
                Contract.ToDo.COLUMN_TIME+" LONG , "+
                Contract.ToDo.COLUMN_NOTE+" TEXT )";

        sqLiteDatabase.execSQL(todoSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
