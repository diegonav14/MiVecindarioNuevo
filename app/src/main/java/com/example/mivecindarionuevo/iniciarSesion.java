package com.example.mivecindarionuevo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mivecindarionuevo.administrador.inicioAdmin;
import com.example.mivecindarionuevo.modelos.Administrador;
import com.example.mivecindarionuevo.modelos.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class iniciarSesion extends AppCompatActivity {

    EditText editTextCorreo, editTextPass;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        editTextCorreo = findViewById(R.id.et_correo);
        editTextPass = findViewById(R.id.et_pass);


        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        cargarPreferencias();
        incializarFirebase();

    }

    private void cargarPreferencias() {

        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        if (preferencias.contains("nombreAdmin") && preferencias.contains("apellidoAdmin")){
            Intent intent = new Intent(this, inicioAdmin.class);
            startActivity(intent);
            finish();
        }else if(preferencias.contains("nombreUsuario") && preferencias.contains("apellidoUsuario")){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
        }
    } // Carga la sesion del usuario

    private void incializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializa la conexion con la base de datos, en este caso Firebase

    public void IniciarSesion (View v) {

        databaseReference.child("Administrador").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String correoAdmin = editTextCorreo.getText().toString();
                String passAdmin = editTextPass.getText().toString();

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Administrador admin = objSnapshot.getValue(Administrador.class);
                    editTextPass.setError(null);
                    editTextCorreo.setError(null);
                    if (correoAdmin.equals(admin.getCorreo()) && passAdmin.equals(admin.getPassword())){
                        Intent intentAdmin = new Intent(iniciarSesion.this, com.example.mivecindarionuevo.administrador.inicioAdmin.class);
                        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencias.edit();
                        editor.putString("nombreAdmin", admin.getNombre());
                        editor.putString("apellidoAdmin",admin.getApellido());
                        editor.commit();
                        startActivity(intentAdmin);
                        finish();
                    }else{
                        editTextCorreo.setError("Correo incorrecto");
                        editTextPass.setError("Contraseña incorrecta");
                    }
                }
                editTextPass.setError(null);
                editTextCorreo.setError(null);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String correo = editTextCorreo.getText().toString();
                String pass = editTextPass.getText().toString();
                Usuario user;

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        user = objSnapshot.getValue(Usuario.class);
                        editTextCorreo.setError(null);
                        editTextPass.setError(null);
                    if (correo.equals(user.getCorreo()) && pass.equals(user.getPassword())) {
                        Intent intent = new Intent(iniciarSesion.this, MapsActivity.class);
                        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencias.edit();
                        editor.putString("nombreUsuario", user.getNombre());
                        editor.putString("apellidoUsuario", user.getApellido());
                        editor.putString("tipoUsuario", user.getTipo());
                        editor.commit();
                        startActivity(intent);
                        finish();
                    }else{
                        editTextCorreo.setError("Correo incorrecto");
                        editTextPass.setError("Contraseña incorrecta");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Valida la sesion del usuario y la redirige a su debida pagina

    public void irContacto(View v){
        Intent intent = new Intent(iniciarSesion.this, contacto.class);
        startActivity(intent);
    } // Inicia la vista (Activity) Contacto

}
