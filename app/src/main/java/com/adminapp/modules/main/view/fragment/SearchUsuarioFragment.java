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
import com.adminapp.utilities.Utilidades;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SearchUsuarioFragment extends Fragment {

    private AdminActivity mainActivity;
    private EditText txtCedula;
    private EditText txtNombre;
    private EditText txtPhone;
    private Button btnConsultar;
    private Button btnLimpiar;
    private Button btnCancelar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=  inflater.inflate(R.layout.fragment_search_usuario, container, false);
       initView(view);
       initListener();
       return view;
    }

    private void initView( View view) {
        txtCedula = view.findViewById(R.id.id_txt);
        txtNombre = view.findViewById(R.id.name_txt);
        txtPhone = view.findViewById(R.id.phone_txt);
        btnConsultar = view.findViewById(R.id.btn_consultar);
        btnLimpiar = view.findViewById(R.id.btn_limpiar);
        btnCancelar = view.findViewById(R.id.cancel_search_btn);
    }

    private void initListener() {
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampo()) {
                    DAOImpl dao = new DAOImpl(getContext());
                    Usuario user = dao.buscarUsuario(txtCedula.getText().toString(),"BUSCAR");
                    if (user != null){
                        txtNombre.setText(user.getNombre());
                        txtPhone.setText(String.valueOf(user.getTelefono()));
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
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
    }

    public void clearData() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtPhone.setText("");
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
            throw new RuntimeException(context.toString() + " must implements SearchUsuarioFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearData();
        mainActivity = null;
    }
}
