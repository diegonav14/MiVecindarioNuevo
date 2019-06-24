package com.example.mivecindarionuevo.administrador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mivecindarionuevo.R;
import com.example.mivecindarionuevo.modelos.Vecindario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ingresarVecindario extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText nombreVec, direccionVec,latitudVec, longitudVec;

    Vecindario vecindarioSeleccionado;

    ListView listV_vecindario;

    private List<Vecindario> listaVecindario = new ArrayList<Vecindario>();
    ArrayAdapter<Vecindario> arrayAdapterVecindario;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_vecindario);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombreVec = findViewById(R.id.et_nombreVecindario);
        direccionVec = findViewById(R.id.et_direccionVecindario);
        latitudVec= findViewById(R.id.et_latitudVecindario);
        longitudVec= findViewById(R.id.et_longitudVecindario);

        cargarPreferencias();
        inicializarFirebase();
        listarDatos();

        listV_vecindario=findViewById(R.id.lv_datosVecindario);

        listV_vecindario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) { // Metodo OnClick para seleccionar un Objeto de una lista vecindarios
                vecindarioSeleccionado = (Vecindario) parent.getItemAtPosition(position);
                nombreVec.setText(vecindarioSeleccionado.getNombre());
                direccionVec.setText(vecindarioSeleccionado.getDireccion());
                latitudVec.setText(vecindarioSeleccionado.getLatitud());
                longitudVec.setText(vecindarioSeleccionado.getLongitud());
            }
        });


    }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializa la conexion con la base de datos, en este caso Firebase

    private void listarDatos() {
        databaseReference.child("Vecindario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaVecindario.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Vecindario v = objSnapshot.getValue(Vecindario.class);
                    listaVecindario.add(v);
                    arrayAdapterVecindario = new ArrayAdapter<Vecindario>(ingresarVecindario.this, android.R.layout.simple_list_item_1, listaVecindario);
                    listV_vecindario.setAdapter(arrayAdapterVecindario);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Listar los vecindarios que estan en la base de datos, Parametro entrada: Vecidario, Parametro salida: List

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    } // Setea el menu en el objeto Toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nombre= nombreVec.getText().toString();
        String direccion= direccionVec.getText().toString();
        String latitud = latitudVec.getText().toString();
        String longitud = longitudVec.getText().toString();


        switch (item.getItemId()){

            case R.id.icon_add:{

                if (nombre.equals("") || direccion.equals("") || latitud.equals("") || longitud.equals("")){
                    validacion();
                }
                else {
                    Vecindario v = new Vecindario();
                    v.setUid(UUID.randomUUID().toString());
                    v.setNombre(nombre);
                    v.setDireccion(direccion);
                    v.setLatitud(latitud);
                    v.setLongitud(longitud);
                    databaseReference.child("Vecindario").child(v.getUid()).setValue(v);
                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            } // Ingresa un objeto Vecindario en la base de datos, Parametro entrada: String, Parametro salida: Vecindario

            case R.id.icon_save:{
                Vecindario v = new Vecindario ();
                v.setUid(vecindarioSeleccionado.getUid());
                v.setNombre(nombreVec.getText().toString().trim());
                v.setDireccion(direccionVec.getText().toString().trim());
                v.setLatitud(direccionVec.getText().toString().trim());
                v.setLongitud(direccionVec.getText().toString().trim());
                databaseReference.child("Vecindario").child(v.getUid()).setValue(v);
                Toast.makeText(this,"Guardar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            } // Modifica el objeto Vecindario seleccionado, Parametro entrada: String, Parametro salida: Vecindario

            case R.id.icon_delete:{
                Vecindario v = new Vecindario ();
                v.setUid(vecindarioSeleccionado.getUid());
                databaseReference.child("Vecindario").child(v.getUid()).removeValue();
                Toast.makeText(this,"Eliminar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            } // Elimina el objeto Vecindario seleccionado de la base de datos.

            case R.id.icon_login:{
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


    private void limpiarCajas() {

        nombreVec.setText("");
        direccionVec.setText("");
        latitudVec.setText("");
        longitudVec.setText("");
    } // Limpia los campos de datos

    private void validacion() {

        String nombre = nombreVec.getText().toString();
        String direccion = direccionVec.getText().toString();
        String latitud = latitudVec.getText().toString();
        String longitud = longitudVec.getText().toString();


        if (nombre.equals("")) {
            nombreVec.setError("Requerido");
        } else if (direccion.equals("")) {
            direccionVec.setError("Requerido");
        }else if (latitud.equals("")){
            latitudVec.setError("Requerido");
        }else if (longitud.equals("")){
            longitudVec.setError("Requerido");
        }
    } // Valida que los campos de datos no esten vacios

    private void cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String nmAdmin = preferencias.getString("nombreAdmin","NoSesion");
        String apAdmin = preferencias.getString("apellidoAdmin","NoSesion");
        toolbar.setSubtitle(nmAdmin+" "+apAdmin);
    } // Carga la sesion del usuario

}
