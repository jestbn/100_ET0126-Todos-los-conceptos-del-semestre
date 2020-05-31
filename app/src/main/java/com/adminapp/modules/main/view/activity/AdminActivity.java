package com.adminapp.modules.main.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.adminapp.R;
import com.google.android.material.navigation.NavigationView;
import com.adminapp.model.Opcion;
import com.adminapp.model.Usuario;
import com.adminapp.modules.login.view.activity.LoginActivity;
import com.adminapp.modules.main.view.fragment.AdminSelectionFragment;
import com.adminapp.modules.main.view.fragment.DeleteUsuarioFragment;
import com.adminapp.modules.main.view.fragment.EditUsuarioFragment;
import com.adminapp.modules.main.view.fragment.InsertUsuarioFragment;
import com.adminapp.modules.main.view.fragment.SearchUsuarioFragment;
import com.adminapp.utilities.BaseActivity;

import java.util.ArrayList;

public class AdminActivity extends BaseActivity {

    private Toolbar toolbaMenu;
    private DrawerLayout drawerLayout;
    private AdminSelectionFragment adminSelectionFragment;
    private InsertUsuarioFragment insertUsuarioFragment;
    private EditUsuarioFragment editUsuarioFragment;
    private DeleteUsuarioFragment deleteUsuarioFragment;
    private SearchUsuarioFragment searchUsuarioFragment;
    private Opcion opcion;
    private ArrayList<Opcion> opciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initView();
        getExtras();
        setOpciones(new ArrayList<Opcion>());
        initActivity();
    }

    private void initView() {
        toolbaMenu = findViewById(R.id.toolbar_menu_admin);
        drawerLayout = findViewById(R.id.drawer_layout);
    }
    private void getExtras() {
        if(null != getIntent().getExtras())
            usuarioLogin = (Usuario) getIntent().getExtras().getSerializable("USUARIO");
    }

    private void initActivity() {
        if(lastFragmentOpen == null){
            menuConfig();
            OpenAdminSelectionFragment();
        }
    }

    private void menuConfig() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbaMenu,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.admin_menu_logout:
                        if(getAlertDialog(getActivity(),R.string.menu_user_logout,R.string.logout_confirm_message)) {
                            finish();
                            OpenSignInActivity();
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.admin_menu_ppal:
                        if (lastFragmentOpen != adminSelectionFragment){
                            OpenAdminSelectionFragment();
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        drawerLayout.closeDrawers();
                        return AdminActivity.super.onOptionsItemSelected(item);
                }
            }
        });
    }

    private void OpenSignInActivity() {
        Intent logIn = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(logIn);
    }

    private Activity getActivity() {
        return this;
    }


    private void OpenAdminSelectionFragment() {
        if (adminSelectionFragment == null)
            adminSelectionFragment = new AdminSelectionFragment();
        switchFragment(adminSelectionFragment,R.id.main_container);
    }
    public void openOptionScreen() {
        
        switch (getOpcion().getIdOpcion()){
            case 1:
                openInsertUsuarioFragment();
                break;
            case 2:
                openEditUsuarioFragment();
                break;
            case 3:
                openSearchUsuariosFragment();
                break;
            case 4:
                openDeleteUsuarioFragment();
                break;
        }
    }

    private void openInsertUsuarioFragment() {
        if (insertUsuarioFragment == null)
            insertUsuarioFragment = new InsertUsuarioFragment();
        else
            insertUsuarioFragment.clearData();
        switchFragment(insertUsuarioFragment,R.id.main_container);
    }

    private void openEditUsuarioFragment() {
        if (editUsuarioFragment == null)
            editUsuarioFragment = new EditUsuarioFragment();
        else
            editUsuarioFragment.clearData();
        switchFragment(editUsuarioFragment,R.id.main_container);
    }

    private void openSearchUsuariosFragment() {

        if (searchUsuarioFragment == null)
            searchUsuarioFragment = new SearchUsuarioFragment();
        else
            searchUsuarioFragment.clearData();
        switchFragment(searchUsuarioFragment,R.id.main_container);
    }

    private void openDeleteUsuarioFragment() {
        if (deleteUsuarioFragment == null)
            deleteUsuarioFragment = new DeleteUsuarioFragment();
        else
            deleteUsuarioFragment.clearData();
        switchFragment(deleteUsuarioFragment,R.id.main_container);
    }

    @Override
    public void onBackPressed() {
        if (lastFragmentOpen instanceof AdminSelectionFragment){
            if(getAlertDialog(getActivity(),R.string.menu_user_logout,R.string.logout_confirm_message)) {
                finish();
                OpenSignInActivity();
            }
        }else if (lastFragmentOpen instanceof InsertUsuarioFragment ||
                lastFragmentOpen instanceof EditUsuarioFragment ||
                lastFragmentOpen instanceof SearchUsuarioFragment ||
                lastFragmentOpen instanceof DeleteUsuarioFragment) {
            OpenAdminSelectionFragment();
        }

    }



    public void setOpcion(Opcion item) {
        opcion = item;
    }

    public Opcion getOpcion() {
        return opcion;
    }

    public ArrayList<Opcion> getOpciones() {
        return opciones;
    }

    public void setOpciones(ArrayList<Opcion> opciones) {
        opciones.add(new Opcion(1,"INSERTAR USUARIO"));
        opciones.add(new Opcion(2,"MODIFICAR USUARIO"));
        opciones.add(new Opcion(3,"BUSCAR USUARIOS"));
        opciones.add(new Opcion(4,"ELIMINAR USUARIO"));
        this.opciones = opciones;
    }
}
