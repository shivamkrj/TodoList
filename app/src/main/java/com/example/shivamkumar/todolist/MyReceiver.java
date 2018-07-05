package com.example.shivamkumar.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i],format);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
//                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
//                     Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
             //       toast.show();
//                    Intent open = new Intent(context, MyReceiver.class);
//                    open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(open);
                    Intent intent1 = new Intent(context,AddNote.class);
                    intent1.putExtra("num",senderNum);
                    intent1.putExtra("message",message);
                    context.startActivity(intent1);
                }
            }

        } catch (Exception e) {
           // Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
        //throw new UnsupportedOperationException("Not yet implemented");

    }
}
