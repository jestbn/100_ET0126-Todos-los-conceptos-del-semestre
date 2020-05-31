package com.adminapp.modules.main.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.adminapp.R;
import com.adminapp.dao.DAOImpl;
import com.adminapp.model.Usuario;
import com.adminapp.modules.main.view.activity.AdminActivity;
import com.adminapp.security.encrypt.aesb64.EncrypAESB64;
import com.adminapp.utilities.Utilidades;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUsuarioFragment extends Fragment {
    private AdminActivity mainActivity;
    Usuario user;
    private EditText txtCedula;
    private EditText txtNombre;
    private EditText txtPass;
    private EditText txtPhone;
    private Button btnConsultar;
    private Button btnUpdate;
    private Button btnCancelar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_usuario, container, false);
        initView(view);
        initListener();
        return view;
    }
    private void initView( View view) {
        txtCedula = view.findViewById(R.id.id_txt);
        txtNombre = view.findViewById(R.id.name_txt);
        txtPass = view.findViewById(R.id.pass_txt);
        txtPhone = view.findViewById(R.id.phone_txt);
        btnConsultar = view.findViewById(R.id.btn_consultar);
        btnUpdate = view.findViewById(R.id.update_btn);
        btnCancelar = view.findViewById(R.id.cancel_update_btn);
    }

    private void initListener() {
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampo()) {
                    DAOImpl dao = new DAOImpl(getContext());
                    user = dao.buscarUsuario(txtCedula.getText().toString(),"UPDATE");
                    if (user != null){
                        txtNombre.setText(user.getNombre());
                        txtPass.setText(EncrypAESB64.getInstance().decryptAES(user.getPass()));
                        txtPhone.setText(String.valueOf(user.getTelefono()));
                        setEnables(true);
                    }
                    else {
                        String msj = getString(R.string.search_not_found)+ " "+txtCedula.getText().toString();
                        Utilidades.getInstance().showMessage(msj,getContext());
                        clearData();
                    }
                }
                mainActivity.hideKeyBoard(getView());
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msj ="";
                if(mainActivity.validateUsuarioLogin(user.getCedula())){
                    DAOImpl dao = new DAOImpl(getContext());
                    user.setNombre(txtNombre.getText().toString());
                    user.setPass(EncrypAESB64.getInstance().encryptAES(txtPass.getText().toString()));
                    user.setTelefono(Long.parseLong(txtPhone.getText().toString()));
                    int rowCount = dao.modificarUsuario(user);
                    if (rowCount > 0){
                      msj = getString(R.string.update_success);
                      clearData();
                    }else{
                      msj = getString(R.string.update_failed);
                    }
                }else {
                    msj = getString(R.string.err_operation_login);
                }
                Utilidades.getInstance().showMessage(msj,getContext());
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
    }

    private void setEnables(boolean b) {
        txtCedula.setEnabled(!b);
        txtNombre.setEnabled(b);
        txtPass.setEnabled(b);
        txtPhone.setEnabled(b);
        btnUpdate.setEnabled(b);
    }

    public void clearData() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtPass.setText("");
        txtPhone.setText("");
        user = null;
        setEnables(false);
    }

    private boolean validarCampo() {
        if (txtCedula.getText().toString().equals("")){
            txtCedula.setError(getString(R.string.validate_cedula_error));
            return false;
        }
        return true;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AdminActivity){
            mainActivity = (AdminActivity) context;
        } else {
            throw new RuntimeException(context.toString() + " must implements EditUsuarioFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearData();
        mainActivity = null;
    }
}
