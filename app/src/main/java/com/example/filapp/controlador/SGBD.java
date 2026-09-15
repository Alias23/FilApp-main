package com.example.filapp.controlador;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SGBD extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DATOS = "fileapp.db";
    private static final int VERSION_BASE_DATOS = 1;

    public SGBD(Context c){
        super(c,NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try{
            String queryFormatoTabla = "CREATE TABLE IF NOT EXISTS Formato (" +
                    "nom VARCHAR(255) PRIMARY KEY," +
                    "imprimible BOOLEAN" +
                    ")";
            db.execSQL(queryFormatoTabla);
            String queryConvertibleTabla = "CREATE TABLE IF NOT EXISTS Convertible (" +
                    "deNom VARCHAR(255)," +
                    "aNom VARCHAR(255)" +
                    ")";
            db.execSQL(queryConvertibleTabla);
            String queryInsertarPDF= "INSERT OR IGNORE INTO Formato " +
                    "(nom, imprimible)" +
                    "VALUES ('pdf', true)";
            db.execSQL(queryInsertarPDF);
            String[] arg = new String[]{"jpg","png","jpeg","docx","pptx"};
            for (String form:arg){
                String queryInsertarForm= "INSERT OR IGNORE INTO Formato " +
                        "(nom, imprimible)" +
                        "VALUES (?, false)";
                db.execSQL(queryInsertarForm, new Object[]{form});
            }
            String[] dePdf = new String[]{"jpg","jpeg","png","docx","pptx"};
            for (String form:dePdf){
                String queryInsertarConv = "INSERT INTO Convertible " +
                        "(deNom, aNom)" +
                        "VALUES ('pdf', ?)";
                db.execSQL(queryInsertarConv, new Object[]{form});
            }
            String[] deJpg = new String[]{"pdf","png","jpeg"};
            for (String form:deJpg){
                String queryInsertarConv = "INSERT INTO Convertible " +
                        "(deNom, aNom)" +
                        "VALUES ('jpg', ?)";
                db.execSQL(queryInsertarConv, new Object[]{form});
            }
            String[] deJpeg = new String[]{"pdf","png","jpg"};
            for (String form:deJpeg){
                String queryInsertarConv = "INSERT INTO Convertible " +
                        "(deNom, aNom)" +
                        "VALUES ('jpeg', ?)";
                db.execSQL(queryInsertarConv, new Object[]{form});
            }
            String[] dePng = new String[]{"pdf","jpeg","jpg"};
            for (String form:dePng){
                String queryInsertarConv = "INSERT INTO Convertible " +
                        "(deNom, aNom)" +
                        "VALUES ('png', ?)";
                db.execSQL(queryInsertarConv, new Object[]{form});
            }
            String[] aPDF = new String[]{"docx","pptx"};
            for(String form:aPDF) {
                String queryInsertarConv = "INSERT INTO Convertible " +
                        "(deNom, aNom)" +
                        "VALUES (?, 'pdf')";
                db.execSQL(queryInsertarConv, new Object[]{form});
            }
            db.setTransactionSuccessful(); // Marcar la transacción como exitosa
        } finally {
            db.endTransaction(); // Finalizar la transacción
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public JSONArray getConvertibles(String deNom) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase(); //formato JSONArray
        JSONArray js = new JSONArray();
        Cursor c = db.rawQuery("SELECT aNom FROM Convertible WHERE deNom=?",new String[]{deNom});
        while (c.moveToNext()){
            JSONObject j = new JSONObject();
            j.put("aNom",c.getString(0));
            js.put(j);
        }
        c.close();
        db.close();
        return js;
    }


}
