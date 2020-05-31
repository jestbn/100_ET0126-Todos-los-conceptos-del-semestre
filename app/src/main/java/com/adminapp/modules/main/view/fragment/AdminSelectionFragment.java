package com.adminapp.modules.main.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adminapp.R;
import com.adminapp.model.Opcion;
import com.adminapp.modules.main.view.activity.AdminActivity;
import com.adminapp.modules.main.view.adapter.ListaOpcionesAdapterRecycler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AdminSelectionFragment extends Fragment {
    private AdminActivity mainActivity;
    private RecyclerView recyclerOpciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_selection, container, false);
        initView(view);
        callAdapter(mainActivity.getOpciones());
        return view;
    }


    private void initView(View view) { recyclerOpciones = view.findViewById(R.id.recyclerOptions);}

    private void callAdapter(ArrayList<Opcion> opciones) {
        try {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            recyclerOpciones.setLayoutManager(linearLayoutManager);
            ListaOpcionesAdapterRecycler opcionesAdapterRecycler = new ListaOpcionesAdapterRecycler(getContext(), opciones, new ListaOpcionesAdapterRecycler.OpcionItemListener() {
                @Override
                public void onItemClick(View view, Opcion item) {
                    mainActivity.setOpcion(item);
                    mainActivity.openOptionScreen();
                }
            });
            recyclerOpciones.setAdapter(opcionesAdapterRecycler);
            opcionesAdapterRecycler.notifyItemChanged(0);

        }catch (Exception e){
            Log.e(e.getCause().toString(),e.getMessage(),e);
            throw e;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AdminActivity){
            mainActivity = (AdminActivity) context;
        } else {
            throw new RuntimeException(context.toString() + " must implements AdminSelectionFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}
