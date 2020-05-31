package com.adminapp.modules.main.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adminapp.R;
import com.adminapp.model.Opcion;

import java.util.ArrayList;

public class ListaOpcionesAdapterRecycler extends RecyclerView.Adapter<ListaOpcionesAdapterRecycler.ViewHolder> {

    private Context context;
    private ArrayList<Opcion> opciones;
    private OpcionItemListener itemListener;

    public ListaOpcionesAdapterRecycler(Context context, ArrayList<Opcion> opciones, OpcionItemListener listener){
        this.context = context;
        this.opciones = opciones;
        this.itemListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_opciones_adapter_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(opciones.get(position));
    }

    @Override
    public int getItemCount() {return opciones.size();}

    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvOpcion;
        private Opcion opcion;
        public ViewHolder(View itemView){
            super(itemView);
            tvOpcion = itemView.findViewById(R.id.tv_opcion);
            itemView.setOnClickListener(this);
        }

        public void setData(Opcion opcion){
            this.opcion = opcion;
            tvOpcion.setText(opcion.getDescripcionOpcion().trim());
        }

        @Override
        public void onClick(View view) {itemListener.onItemClick(view,opcion);}
    }

    public interface OpcionItemListener{
        void onItemClick(View view, Opcion item);
    }
}
