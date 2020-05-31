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
 * create an instance of this fragment.
 */
public class InsertUsuarioFragment extends Fragment {

    private AdminActivity mainActivity;
    private EditText txtIdentificacion;
    private EditText txtNombre;
    private EditText txtPass;
    private EditText txtPhone;
    private Button btnInsert;
    private Button btnCancel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_usuario, container, false);
        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        txtIdentificacion = view.findViewById(R.id.id_txt);
        txtNombre = view.findViewById(R.id.name_txt);
        txtPass = view.findViewById(R.id.pass_txt);
        txtPhone = view.findViewById(R.id.phone_txt);
        btnInsert = view.findViewById(R.id.insert_btn);
        btnCancel = view.findViewById(R.id.cancel_insert_btn);
    }

    private void initListener(){
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validarCampos()){
                        Usuario user = new Usuario();
                        user.setCedula(txtIdentificacion.getText().toString());
                        user.setNombre(txtNombre.getText().toString());
                        user.setPass(EncrypAESB64.getInstance().encryptAES(txtPass.getText().toString()));
                        user.setTelefono(Long.parseLong(txtPhone.getText().toString()));
                        DAOImpl dao = new DAOImpl(getContext());
                        String responseCode = dao.insertarUsuario(user);
                        String msj ="";
                        switch (responseCode){
                            case "00":
                                msj = getString(R.string.insert_success);
                                clearData();
                                break;
                            case "01":
                                msj = getString(R.string.insert_failed);
                            case "02":
                                msj = getString(R.string.dup_on_value) + " "+ user.getCedula();

                        }
                        Utilidades.getInstance().showMessage(msj,getContext());
                    }
                }catch (Exception e){
                    throw e;
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
    }

    public void clearData() {
        txtIdentificacion.setText("");
        txtNombre.setText("");
        txtPass.setText("");
        txtPhone.setText("");
    }

    private boolean validarCampos() {
        if (txtIdentificacion.getText().toString().equals("")){
            txtIdentificacion.setError(getString(R.string.validate_cedula_error));
            return false;
        }

        if (txtNombre.getText().toString().equals("")){
            txtNombre.setError(getString(R.string.validate_name_error));
            return false;
        }

        if (txtPass.getText().toString().equals("")){
            txtPass.setError(getString(R.string.validate_pass_error));
            return false;
        }

        if (txtPhone.getText().toString().equals("")){
            txtPhone.setError(getString(R.string.validate_phone_error));
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
            throw new RuntimeException(context.toString() + " must implements InsertUsuarioFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearData();
        mainActivity = null;
    }

}
