package com.example.navegacionvista;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper; //Clase para la manipulación facil de SQLite
import android.util.Base64;
import static android.provider.BaseColumns._ID; //Provee el campo ID para la  base de datos

public class manejador_sql extends SQLiteOpenHelper {
	

    private static final String ALGORITMO = "AES";
    private static final byte[] valorClave = "0000000000000000".getBytes(); 

	public static final String DATABASE_NAME = "basedatos.db";
	public manejador_sql(Context ctx) {
		super (ctx,DATABASE_NAME,null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		String query = "CREATE TABLE usuarios("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , nombre text,  contrasena text, correo text)"; //_ID IMportado de BaseColumn 
		db.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int anterior, int nueva){
		db.execSQL("DROP TABLE IF EXISTS usuarios");
		onCreate(db);
	}
	
	public void abrir(){
		this.getWritableDatabase();
	}
	
	public void cerrar(){
		this.close();
	}
	
	public void insertarUsuario(String nuevo_nombre,  String nueva_contrasena, String nueva_correo) throws Exception {
		ContentValues estructura = new ContentValues();
		estructura.put("nombre", nuevo_nombre);
		String contrasena_encriptada = encriptar(nueva_contrasena);
		estructura.put("contrasena",contrasena_encriptada);
		estructura.put("correo", nueva_correo);
		this.getWritableDatabase().insert("usuarios", null , estructura);
		this.close();
	}
	
	public void borrarUsuario(int id){
	    this.getWritableDatabase().delete("usuarios", "_id="+id, null);
	    this.close();  
	}
	
	public void modificarUsuario(int id, String nuevo_nombre,  String nuevo_password, String nuevo_correo) throws Exception{
	    ContentValues valores = new ContentValues();
	    valores.put(_ID, id);
	    valores.put("nombre", nuevo_nombre);
		String contrasena_encriptada = encriptar(nuevo_password);
	    valores.put("contrasena", contrasena_encriptada);
	    valores.put("correo", nuevo_correo);
	    this.getWritableDatabase().update("usuarios", valores, "_id=" + id, null);
	    this.close();   
	}

	public String mostrarListado() throws Exception{
		
		String resultado="";
		String columnas[] = {_ID,"nombre", "contrasena","correo"};
		Cursor c = this.getReadableDatabase().query("usuarios",columnas,null,null,null,null,null);
		int i_I = c.getColumnIndex(_ID);
		int i_N = c.getColumnIndex("nombre");
		int i_C= c.getColumnIndex("contrasena");
		c.moveToFirst(); 
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			String IDLeido =  c.getString(i_I);
			String NombreLeido = c.getString(i_N);
			String ContraLeida = c.getString(i_C);
			String ContraTraducida = desencriptar(ContraLeida);
			resultado += "ID :  " + IDLeido + "; Nombre : " + NombreLeido +  " \n\nPass almacenado : " + ContraLeida + " \n" + "Pass Desencriptado :  \n" + ContraTraducida + "\n";
		}
		return resultado;	
		
	}
	public static String encriptar (String texto_a_encriptar) throws Exception 
	{
		Key keyNueva = new SecretKeySpec(valorClave, ALGORITMO);
		Cipher cipher = Cipher.getInstance("AES"); 
		cipher.init(Cipher.ENCRYPT_MODE, keyNueva );
		byte[] encrypted = cipher.doFinal(texto_a_encriptar.getBytes("UTF-8"));
		String texto_encriptado = Base64.encodeToString(encrypted, Base64.DEFAULT);//new String(encrypted, "UTF-8");
		return texto_encriptado;
	   
	}
	 public static String desencriptar(String texto_encriptado) throws Exception
	 {
		Key key = new SecretKeySpec(valorClave, ALGORITMO);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
	    byte[] decodificar_texto = Base64.decode(texto_encriptado.getBytes("UTF-8"), Base64.DEFAULT);
	    byte[] desencriptado = cipher.doFinal(decodificar_texto);
	    return new String(desencriptado, "UTF-8");
	 }
}
