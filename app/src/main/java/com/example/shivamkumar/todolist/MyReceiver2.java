package com.example.shivamkumar.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //
            ToDoOpenHelper openhelper = ToDoOpenHelper.getInstance(context);
            SQLiteDatabase database = openhelper.getReadableDatabase();
            Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME, null, null, null, null, null, Contract.ToDo.COLUMN_TIME);
            while (cursor.moveToNext()) {
                long currenttime = System.currentTimeMillis();
                long alarm = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
                long id = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_ID));
                int idd = (int) id;
                if (alarm > currenttime) {
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent2 = new Intent(context, MyReceiver.class);
                    intent.putExtra("ID", id);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idd, intent2, 0);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm, pendingIntent);
                    //
                }
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
    }
    }
