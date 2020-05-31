package com.adminapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.adminapp.model.Usuario;


public class DAOImpl extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "create table usuarios(cedula varchar2(15) UNIQUE, nombre text, password text, telefono number)";
    private static final String INSERT_ADMIN = "INSERT INTO usuarios(cedula,nombre,password,telefono) VALUES (25283,'ADMIN','LaFG40DZO2kLoMFL9NUkMw==',0);";
    private static final int VERSION_TABLE = 1;
    private static final String TABLE_NAME = "basedatos.db";
    public DAOImpl(@Nullable Context context) {
        super(context, TABLE_NAME, null, VERSION_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(INSERT_ADMIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE);
        onCreate(db);
    }


    public String insertarUsuario(Usuario user){
        String responseCode = "";
        SQLiteDatabase db = getWritableDatabase();
        if (db != null){
            ContentValues values = new ContentValues();
            values.put("cedula",user.getCedula());
            values.put("nombre",user.getNombre());
            values.put("password",user.getPass());
            values.put("telefono",user.getTelefono());
            try {
                long response = db.insertOrThrow("usuarios",null,values);
                if (response != -1L){
                    responseCode = "00";
                }else {
                    responseCode = "01";
                }
            } catch (SQLiteConstraintException DUP_e){
                Log.e("Error insertando",DUP_e.getMessage(),DUP_e);
                responseCode = "02";
            } catch (Exception e) {
                Log.e("Error insertando",e.getMessage(),e);
                responseCode = "01";
            } finally {
                db.close();
            }
        }
        return responseCode;
    }

    public int modificarUsuario(Usuario user){
        SQLiteDatabase db = getWritableDatabase();
        int rowCount = 0;
        if (db != null){
            try {
                ContentValues valores = new ContentValues();
                valores.put("nombre", user.getNombre());
                valores.put("password", user.getPass());
                valores.put("telefono", user.getTelefono());
                rowCount =db.update("usuarios", valores, "cedula=" + user.getCedula(), null);
            }catch (Exception e){
                Log.e("Error Actualizando",e.getMessage(),e);
                throw e;
            }finally {
                db.close();
            }
        }
        return rowCount;
    }

    public int borrarUsuario(String cedula) throws SecurityException{
        if (cedula.equals("25283")){
            throw new SecurityException("El usuario administrador no puede ser eliminado");
        }
        SQLiteDatabase db = getWritableDatabase();
        int rowCount = 0;
        if (db != null) {
            try {
                rowCount = db.delete("usuarios", "cedula="+cedula, null);
            }catch (Exception e){
                Log.e("Error Eliminando",e.getMessage(),e);
                throw e;
            }finally {
                db.close();
            }
        }
        return rowCount;
    }

    public Usuario buscarUsuario(String cedula,String operacion){
        SQLiteDatabase db = getReadableDatabase();
        Usuario user = null;
        if (db != null) {
            Cursor cursor = null;
            try {
                String[] datos;
                if (operacion.equals("BUSCAR"))
                    datos = new String[]{"nombre", "telefono"};
                else
                    datos = new String[]{"nombre","password", "telefono"};
                cursor = db.query("usuarios",datos,"cedula = "+ cedula,null,null,null,null);
                if (cursor.moveToNext()){
                    if (operacion.equals("BUSCAR"))
                        user = new Usuario(cedula,cursor.getString(0), cursor.getLong(1));
                    else
                        user = new Usuario(cedula,cursor.getString(0), cursor.getString(1),cursor.getLong(2));
                }
            }
            catch (Exception e){
                Log.e("Error Recuperando Datos",e.getMessage(),e);
                throw e;
            } finally {
                if (cursor != null)
                    cursor.close();
                db.close();
            }
        }
        return user;
    }

    public Usuario doLogin(String cedula, String pass){
        SQLiteDatabase db = getReadableDatabase();
        Usuario user = null;
        String where = "cedula = ? AND password = ?";
        if (db != null) {
            Cursor cursor = null;
            try {
                String[] datos = {"nombre","telefono"};
                String[] args = {cedula,pass};
                cursor = db.query("usuarios",datos,where,args,null,null,null);
                if (cursor.moveToFirst()){
                    user = new Usuario(cedula,cursor.getString(0), pass, cursor.getLong(1));
                }
            }catch (Exception e){
                Log.e("Error Recuperando Datos",e.getMessage(),e);
                throw e;
            } finally {
                if (cursor != null)
                    cursor.close();
                db.close();
            }
        }
        return user;
    }
}
