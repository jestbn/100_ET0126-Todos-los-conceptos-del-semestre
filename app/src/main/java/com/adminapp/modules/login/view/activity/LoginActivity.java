package com.adminapp.modules.login.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.adminapp.R;
import com.adminapp.dao.DAOImpl;
import com.adminapp.model.Usuario;
import com.adminapp.modules.main.view.activity.AdminActivity;
import com.adminapp.security.encrypt.aesb64.EncrypAESB64;
import com.adminapp.utilities.BaseActivity;

public class LoginActivity extends BaseActivity {

    private Toolbar toolbarMenu;

    private EditText txtUserLogin;
    private EditText txtPassLogin;
    private TextView lblBienvenido;
    private Usuario user;
    private DAOImpl dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
        menuConfig();
        validateLastLogin();
        dao = new DAOImpl(getApplicationContext());
    }

    private void validateLastLogin() {
        SharedPreferences preferences = getSharedPreferences("LastLogin", Context.MODE_PRIVATE);
        String cedula = preferences.getString("cedula","");
        if(cedula != ""){
            String nombre = preferences.getString("nombre","");
            user = new Usuario(cedula,nombre,null,0);
            txtUserLogin.setText(cedula);
            String msjBienvenido = getString(R.string.login_text_welcome) + " " + nombre;
            lblBienvenido.setText(msjBienvenido);
            txtUserLogin.setEnabled(false);
        }
    }

    private void initview() {
        txtUserLogin = findViewById(R.id.user_login_txt);
        txtPassLogin = findViewById(R.id.password_login_txt);
        lblBienvenido = findViewById(R.id.login_tv_bienvenido);
        toolbarMenu = findViewById(R.id.toolbar_menu_login);
    }

    private void menuConfig() {
        toolbarMenu.inflateMenu(R.menu.menu_login);
        setActionBar(toolbarMenu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login_menu_cerrar) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void loginUser(View view){
        if(validarUsuario()){
            if(user != null){
                user.setPass(EncrypAESB64.getInstance().encryptAES(txtPassLogin.getText().toString()));
                user = dao.doLogin(user.getCedula(),user.getPass());
            }else{
                String usuario = txtUserLogin.getText().toString();
                String pass = EncrypAESB64.getInstance().encryptAES(txtPassLogin.getText().toString());
                user = dao.doLogin(usuario,pass);
            }
            if (user != null){
                saveSharedPreferences();
                Intent admin = new Intent(LoginActivity.this, AdminActivity.class);
                admin.putExtra("USUARIO",user);
                startActivity(admin);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(getString(R.string.login_error))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Notificaci\u00f3n");
                alert.show();
            }
        }


    }

    private void saveSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("LastLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("cedula", user.getCedula());
        editor.putString("nombre", user.getNombre());
        boolean commit = editor.commit();
        if (!commit)
            Log.e(getString(R.string.save_user_SP_error_title),getString(R.string.save_user_SP_error_msg));
    }


    public  void loginOtherUser(View view){
        lblBienvenido.setText( getString(R.string.login_text_welcome));
        txtUserLogin.setText("");
        txtPassLogin.setText("");
        txtUserLogin.setEnabled(true);
        txtUserLogin.requestFocus();
        user =null;
    }


    private boolean validarUsuario() {
        if (txtUserLogin.getText().toString().equals("")){
            txtUserLogin.setError(getString(R.string.user_login_error));
            return false;
        }

        if (txtPassLogin.getText().toString().equals("")){
            txtPassLogin.setError(getString(R.string.pass_login_error));
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(getAlertDialog(this,R.string.menu_user_close,R.string.exit_confirm_message)){
            finishAndRemoveTask();
        }
    }
}
