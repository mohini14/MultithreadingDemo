package com.example.mohini.multithreadingdemo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mohini on 29/11/17.
 */

public class MessagePrinting {

    public static void logMessage(String message){
        Log.d("VIVZ", message);
    }

    public static void displayToast(Context context, String message){

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
