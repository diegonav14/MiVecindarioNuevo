package com.example.mivecindarionuevo.administrador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mivecindarionuevo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class inicioAdmin extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        cargarPreferencias();
        incializarFirebase();

    }


    private void cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String nmAdmin = preferencias.getString("nombreAdmin","NoSesion");
        String apAdmin = preferencias.getString("apellidoAdmin","NoSesion");
        toolbar.setSubtitle(nmAdmin+" "+apAdmin);
    }

    // Carga la sesion del usuario

    public void irUsuario (View v){
        Intent intent = new Intent(inicioAdmin.this, ingresarUsuarios.class);
        startActivity(intent);
    }

    // Inicia la vista(Activity) de Ingresar Usuarios

    public void irVecindario (View v){
        Intent intent = new Intent(inicioAdmin.this, ingresarVecindario.class);
        startActivity(intent);
    }

    // Inicia la vista(Activity) de Ingresar Vecindario

    public void irHogares (View v){
        Intent intent = new Intent(inicioAdmin.this, agregarHogares.class);
        startActivity(intent);
    }

    // Inicia la vista (Activity) Ingresar Hogares

    public void irMapa (View v){
        Intent intent = new Intent (inicioAdmin.this, MapsActivityAdmin.class);
        startActivity(intent);
    }

    // Inicia la vista (Activity) MapsActivityAdmin para ver los mapas de los vecindarios

    private void incializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    // Inicia la conexion con la base de datos

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Setea el menu en el objeto Toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.cerrarSesion:{
                SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this, com.example.mivecindarionuevo.iniciarSesion.class);
                startActivity(intent);
                finish();
            } // Cierra la sesion del usuario
            default:break;

        }
        return true;
    } // Metodos de las opciones del menu

}
