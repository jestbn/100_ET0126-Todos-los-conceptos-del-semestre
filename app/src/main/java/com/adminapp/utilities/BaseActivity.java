package com.adminapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adminapp.model.Usuario;


public class BaseActivity extends AppCompatActivity {

    protected Fragment lastFragmentOpen;
    protected FragmentManager fragmentManager;
    private boolean alertDialogLogOutResponse;
    protected Usuario usuarioLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
    }

    public void switchFragment(Fragment newFragment, int containerViewId){
        if(lastFragmentOpen == newFragment)
            return;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (newFragment.isAdded()){
            fragmentTransaction.show(newFragment);
        } else {
            fragmentTransaction.add(containerViewId,newFragment,newFragment.getClass().getName());
        }
        if(lastFragmentOpen != null && lastFragmentOpen.isAdded()){
            fragmentTransaction.hide(lastFragmentOpen);
        }
        lastFragmentOpen = newFragment;
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    protected boolean getAlertDialog(final Activity activity,final int title, final int idMessage) {

        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(getString(idMessage))
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogLogOutResponse = true;
                        handler.sendMessage(handler.obtainMessage());
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogLogOutResponse = false;
                        handler.sendMessage(handler.obtainMessage());
                    }
                });
        builder.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return alertDialogLogOutResponse;

    }
    public void hideKeyBoard(View view) {
        if(view!= null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean validateUsuarioLogin(String cedula) {
        return !usuarioLogin.getCedula().equals(cedula);
    }
}
