package com.example.filapp.vista;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filapp.R;
import com.example.filapp.controlador.Gestor;
import com.example.filapp.modelo.Fichero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Convertir extends AppCompatActivity {

    private RecyclerView recyclerViewFicheros;
    private Adapter_ver_archivo ficheroAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convertir_archivo);
        recyclerViewFicheros = findViewById(R.id.recyclerViewFicheros);
        Gestor g = Gestor.getMiGestor();
        Fichero f = g.getFichero(getIntent().getStringExtra("nom"));
        JSONObject j= new JSONObject();
        try {
            //Mandamos el fichero al adapter
            j.put("fichero",f);
            JSONArray listaFicheros = new JSONArray();
            listaFicheros.put(j);
            recyclerViewFicheros.setLayoutManager(new LinearLayoutManager(Convertir.this));
            //EN LOS DEMÁS CASOS PONER FALSE
            getIntent().putExtra("spinner", true);
            //PONER EN QUE MODO ESTÁN EN LOS DEMÁS CASOS (IMPRIMIR Y UNIR)
            getIntent().putExtra("modo","convertir");
            getIntent().putExtra("nom",getIntent().getStringExtra("nom"));
            ficheroAdapter = new Adapter_ver_archivo(listaFicheros,getIntent());
            recyclerViewFicheros.setAdapter(ficheroAdapter);
        } catch (JSONException e) {

        }

        Button back = findViewById(R.id.backConvertir);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(Convertir.this, MainActivity.class);
                Convertir.this.startActivity(e);

            }
        });



    }


}

