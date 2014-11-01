package com.example.navegacionvista;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;


public class MainActivity extends Activity {

	private manejador_sql db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		manejador_sql manejador = new manejador_sql(this);
		db = manejador;
	}
	
	//FUNCIONES : ON_CLICK 
	//-INSERTAR USUARIO
	public void insertar(View view){
		EditText editNombre = (EditText)findViewById(R.id.editTextNombre);
		EditText editPassword = (EditText)findViewById(R.id.editTextPassword);
		EditText editEmail= (EditText)findViewById(R.id.editTextEmail);
        String valor1 =editNombre.getText().toString();
        String valor2 =editPassword.getText().toString();
        String valor3 =editEmail.getText().toString();
        db.abrir();
        try {
			db.insertarUsuario(valor1, valor2, valor3);
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Error al insertar usuario!!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
        db.cerrar();
		editPassword.setText("");
		editNombre.setText("");
        Toast.makeText(getBaseContext(), "Usuario dado de Alta!!", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        consultar();
	}

	//-MODIFICAR USUARIO
	public void modificar(View view){
		EditText editId = (EditText)findViewById(R.id.editTextId);
		EditText editNombre = (EditText)findViewById(R.id.editTextNombre);
		EditText editPassword = (EditText)findViewById(R.id.editTextPassword);
		EditText editEmail= (EditText)findViewById(R.id.editTextEmail);
        String s_id	  =editId.getText().toString();
        int    valor0 = Integer.parseInt(s_id);
		String valor1 =editNombre.getText().toString();
        String valor2 =editPassword.getText().toString();
        String valor3 =editEmail.getText().toString();
        db.abrir();
        try {
			db.modificarUsuario( valor0, valor1, valor2, valor3);
		} catch (Exception e) {
	        Toast.makeText(getBaseContext(), "Error al actualizar Usuario!!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		db.cerrar();
		editPassword.setText("");
		editNombre.setText("");
        Toast.makeText(getBaseContext(), "Usuario Actualizado!!", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        consultar();
	}

	//-ELIMINAR USUARIO
	public void eliminar(View view){
		EditText editNombre = (EditText)findViewById(R.id.editTextEliminar);
		String valorEliminar = editNombre.getText().toString();
		int value = Integer.parseInt(valorEliminar);
		db.abrir();
	    db.borrarUsuario(value);
		db.cerrar();
        Toast.makeText(getBaseContext(), "Usuario _id="+ valorEliminar +" Eliminado!!", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        consultar();
	}
	
	public void consultar(){
		TextView consulta = (TextView)findViewById(R.id.mensaje);
		db.abrir();
		String arrayResult = "";
		try {
			arrayResult = db.mostrarListado();
		} catch (Exception e) {
	        Toast.makeText(getBaseContext(), "Error al consultar Usuarios!!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		db.cerrar();
		if(arrayResult != "" )consulta.setText(arrayResult);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	


	//-OPCIONES DE MENÚ
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.altas:	Toast.makeText(this, "PANTALLA INSERCIONES", Toast.LENGTH_LONG).show();
								setContentView(R.layout.alta);
								return true;
			case R.id.consultas:Toast.makeText(this, "PANTALLA CONSULTAS", Toast.LENGTH_LONG).show();
								setContentView(R.layout.activity_main);
								consultar();
								return true;
			case R.id.bajas :	Toast.makeText(this, "PANTALLA ELIMINACIONES", Toast.LENGTH_LONG).show();
								setContentView(R.layout.bajas);
								return true;
			case R.id.modificar:Toast.makeText(this, "PANTALLA MODIFICACIONES", Toast.LENGTH_LONG).show();
								setContentView(R.layout.modificaciones);
								return true;
			case R.id.salir:	finish();
								return true;
			default:
				return super.onOptionsItemSelected(item);
							
		}
	}
	
	
}
