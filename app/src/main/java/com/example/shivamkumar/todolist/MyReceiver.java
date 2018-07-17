package com.example.shivamkumar.todolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle bundle=intent.getExtras();
        Toast.makeText(context,"onReceive",Toast.LENGTH_SHORT).show();
        Log.i("notify","onReceive");
        try {
            if (bundle != null) {
                Log.i("notify","bundle!=null");

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i],format);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    //setting notification
                    NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        NotificationChannel channel = new NotificationChannel("mychannelid","Expenses Channel",NotificationManager.IMPORTANCE_HIGH);
                        manager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"mychannelid");
                    builder.setContentTitle("Create ToDo for:"+senderNum);
                    builder.setContentText(message);
                    builder.setSmallIcon(R.drawable.time_icon);
                    Intent intent1 = new Intent(context,AddNote.class);
                    intent1.putExtra("num",senderNum);
                    intent1.putExtra("message",message);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context,(int)System.currentTimeMillis(),intent1,0);

                    builder.setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    manager.notify(1,notification);
                }
            }

        } catch (Exception e) {

            Log.i("notify","in else");
            long id= intent.getLongExtra("ID",-1);
            long time=1;
            if(id==-1)
                return;
            String topic="";
            ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(context);
            SQLiteDatabase database = openHelper.getReadableDatabase();
            String[] arg = {id+""};
            Cursor cursor = database.query(Contract.ToDo.TODO_TABLE_NAME,null,Contract.ToDo.COLUMN_ID+ " = ?",arg,null,null,null);
            while (cursor.moveToNext()){
                topic = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TOPIC));
//                note = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_NOTE));
                time = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_TIME));
            }

            //setting notification
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("mychannelid","Expenses Channel",NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"mychannleid");
            builder.setContentTitle("Todo:"+topic);
            builder.setContentText("Incomplete task in ToDo list");
            builder.setSmallIcon(R.drawable.time_icon);
            Intent intent1 = new Intent(context,Description2.class);
            intent1.putExtra("ID",id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,(int)time,intent1,0);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            Notification notification = builder.build();
            manager.notify(1,notification);
        }

    }
}
