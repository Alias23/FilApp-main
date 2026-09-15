package com.example.filapp.vista;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filapp.R;
import com.example.filapp.controlador.Gestor;
import com.example.filapp.controlador.GestorFichero;
import com.example.filapp.controlador.SGBD;
import com.example.filapp.modelo.Fichero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Adapter_ver_archivo extends RecyclerView.Adapter<Adapter_ver_archivo.VerArchivoHolder> {
    private final JSONArray listaArchivos;
    private final Intent parent;

    public Adapter_ver_archivo(JSONArray listaArchivos, Intent parent) {
        this.listaArchivos = listaArchivos;
        this.parent = parent;
    }


    @NonNull
    @Override
    public VerArchivoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ver_archivo, parent, false);
        return new VerArchivoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerArchivoHolder holder, int position) {
        try {
            JSONObject j = listaArchivos.getJSONObject(position);
            Fichero fichero = (Fichero) j.get("fichero");
            holder.nomTarjeta.setText(fichero.getNom());
            String form = fichero.getFormato();
            //Imágenes
            if (form.equals("pdf")) {
                holder.imgTarjeta.setImageResource(R.drawable.pdf_file_icon);
            } else if (form.equals("docx")) {
                holder.imgTarjeta.setImageResource(R.drawable._496487);
            } else if (form.equals("pptx")) {
                holder.imgTarjeta.setImageResource(R.drawable.microsoft_powerpoint_2013_2019_logo);
            } else {
                holder.imgTarjeta.setImageResource(R.drawable._60_f_460013622_6xf8un6ubmvlx0tajecbhfkponor5cra);
            }
            if (parent.getBooleanExtra("spinner", false)) {
                holder.convTarjeta.setVisibility(View.VISIBLE);
                SGBD sgbd = new SGBD(holder.itemView.getContext());
                JSONArray listaConv = sgbd.getConvertibles(fichero.getFormato());
                if (listaConv.length()==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("No está disponible la conversión de archivos " +
                            fichero.getFormato());
                    builder.setItems(
                            new CharSequence[]{"Vale"},
                            (dialog, which) -> {
                                switch (which) {
                                    case 0:
                                        Intent e = new Intent(holder.itemView.getContext(),MainActivity.class);
                                        //Adapter_ver_archivo.this.parent.startActivity(e);
                                        holder.itemView.getContext().startActivity(e);
                                        break;
                                }
                            });
                    builder.show();
                }
                List<String> lAdapter = new ArrayList<>();
                for (int i = 0; i < listaConv.length(); i++) {
                    JSONObject jl = listaConv.getJSONObject(i);
                    lAdapter.add(jl.getString("aNom"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.itemView.getContext(),
                        android.R.layout.simple_spinner_item, lAdapter);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.convTarjeta.setAdapter(adapter);

            }
            if (parent.getStringExtra("modo").equals("convertir")) {
                holder.ejecutarTarjeta.setVisibility(View.VISIBLE);
                holder.ejecutarTarjeta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Gestor g = Gestor.getMiGestor();
                        Fichero f = g.getFichero(parent.getStringExtra("nom"));

                        try {
                            g.convertirFichero(f, holder.convTarjeta.getSelectedItem().toString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Se ha completado la conversión de " + f.getFormato() +
                                " a " + holder.convTarjeta.getSelectedItem().toString());
                        builder.setItems(
                                new CharSequence[]{"Vale"},
                                (dialog, which) -> {
                                    switch (which) {
                                        case 0:
                                            Intent e = new Intent(holder.itemView.getContext(),MainActivity.class);
                                            //Adapter_ver_archivo.this.parent.startActivity(e);
                                            holder.itemView.getContext().startActivity(e);
                                            break;
                                    }
                                });
                        builder.show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaArchivos.length();
    }


    public static class VerArchivoHolder extends RecyclerView.ViewHolder{
        TextView nomTarjeta;
        ImageView imgTarjeta;
        Spinner convTarjeta;
        Button ejecutarTarjeta;

        public VerArchivoHolder(@NonNull View itemView) {
            super(itemView);
            nomTarjeta = itemView.findViewById(R.id.nombreArchivo);
            imgTarjeta = itemView.findViewById(R.id.imagenTarjeta);
            convTarjeta = itemView.findViewById(R.id.spinnerArchivo);
            ejecutarTarjeta = itemView.findViewById(R.id.ejecutarArchivo);

        }

    }
}
