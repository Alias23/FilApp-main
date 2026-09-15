package com.example.filapp.vista;

import static kotlin.io.ByteStreamsKt.readBytes;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.example.filapp.R;
import com.example.filapp.controlador.Gestor;
import com.example.filapp.controlador.GestorFichero;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    static final int SELECCIONAR_ARCHIVO_REQUEST_CODE = -1;
    Uri archivoUri;  // Variable para almacenar la URI del archivo seleccionado
    private ActivityResultLauncher<Intent> filePickerLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // El usuario ha seleccionado un archivo
                        processSelectedFile(result.getData().getData());
                    }
                });*/
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            // Ahora puedes leer los datos del archivo desde el inputStream
                            // Por ejemplo, si necesitas leer los bytes del archivo:
                            byte[] data = readBytes(inputStream);
                            Intent intentConvertir = new Intent(this, Convertir.class);
                            Gestor g = Gestor.getMiGestor();
                            String nom =obtenerNombreArchivo(uri);
                            ContentResolver resolver = getContentResolver();
                            MimeTypeMap mime = MimeTypeMap.getSingleton();

                            String form =   mime.getExtensionFromMimeType(resolver.getType(uri));
                            g.setFichero(nom,
                                    data,form);
                            intentConvertir.putExtra("nom",nom);
                            startActivity(intentConvertir);
                            // Procesa los datos del archivo según sea necesario
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Button convertirBoton = findViewById(R.id.convertirBoton);
        convertirBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Selecciona todos los tipos de archivos
                //String[] mimeTypes = {"*/*"};
                // Establecer los tipos MIME permitidos para la intención
                //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                filePickerLauncher.launch(intent);
            }
        });
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            // Obtiene la URI del archivo seleccionado
            archivoUri = data.getData();

            // Inicia la actividad Convertir
            Intent intentConvertir = new Intent(this, Convertir.class);
            Gestor g = Gestor.getMiGestor();
            try {
                g.setFichero(getFileNameFromUri(archivoUri),
                        getBitmap(archivoUri),"formato");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            startActivity(intentConvertir);
        }
    }*/
    private void processSelectedFile(Uri fileUri) {
        // Aquí puedes realizar acciones con la URI del archivo seleccionado
        // Por ejemplo, obtener el nombre del archivo, la ruta, abrir el archivo, etc.

        // Ejemplo: Obtener el nombre del archivo
        String fileName = getFileNameFromUri(fileUri);
        Log.d("SelectedFile", "Archivo seleccionado: " + fileName);
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    /*private Bitmap getBitmap (Uri ficheroUri) throws FileNotFoundException {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(ficheroUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }*/
    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();

        // Decodificar la InputStream en un Bitmap
    }

        private String obtenerNombreArchivo (Uri archivoUri){
            // Obtiene el nombre del archivo desde la URI
            String nombreArchivo = null;
            String[] projection = {OpenableColumns.DISPLAY_NAME};
            try (Cursor cursor = getContentResolver().query(archivoUri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nombreColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    nombreArchivo = cursor.getString(nombreColumnIndex);
                }
            }
            return nombreArchivo;
        }
        /*
        Button imprimirBoton = findViewById(R.id.imprimirBoton);
        imprimirBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf,image/jpeg,image/jpg,image/png"); // Selecciona todos los tipos de archivos
                startActivity(intent);
            }
        });
        Button unirBoton = findViewById(R.id.unirBoton);
        unirBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf"); // Selecciona todos los tipos de archivos
                startActivity(intent);
            }
        });*/

}