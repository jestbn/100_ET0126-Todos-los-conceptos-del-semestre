package com.adminapp.utilities;

import android.content.Context;
import android.widget.Toast;

public class Utilidades {
    public static Utilidades singleton= null;


    public static Utilidades getInstance(){
        if (singleton == null){
            singleton = new Utilidades();
        }
        return singleton;
    }

    public void showMessage(String message, Context context){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context,message,duration);
        toast.show();
    }


}
