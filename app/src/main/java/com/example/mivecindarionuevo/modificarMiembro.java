package com.example.mivecindarionuevo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mivecindarionuevo.modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class modificarMiembro extends AppCompatActivity {

    EditText et_nombreMiembro, et_apellidoMiembro, et_telefonoMiembro, et_correoMiembro, et_passwordMiembro;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Toolbar toolbar;

    Spinner spinnerTipo;

    String nombreMiembro, apellidoMiembro, nmUsuario, apUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_modificar_miembro);

        et_nombreMiembro = findViewById(R.id.et_nombreMiembro);
        et_apellidoMiembro = findViewById(R.id.et_apellidoMiembro);
        et_telefonoMiembro = findViewById(R.id.et_telefonoMiembro);
        et_correoMiembro = findViewById(R.id.et_correoMiembro);
        et_passwordMiembro = findViewById(R.id.et_passwordMiembro);
        spinnerTipo = findViewById(R.id.spTipoMiembro);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(
                this,R.array.Tipo, android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterSpinner);

        inicializarFirebase();
        cargarPreferencias();
        traerMiembro();

    }

    private void traerMiembro() {

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    if (nombreMiembro.equals(u.getNombre()) && apellidoMiembro.equals(u.getApellido())){
                        et_nombreMiembro.setText(u.getNombre());
                        et_apellidoMiembro.setText(u.getApellido());
                        et_telefonoMiembro.setText(u.getTelefono());
                        et_correoMiembro.setText(u.getCorreo());
                        et_passwordMiembro.setText(u.getPassword());
                        for (int i = 0; i < spinnerTipo.getCount(); i++){
                            if (spinnerTipo.getItemAtPosition(i).toString().equals(u.getTipo())){
                                spinnerTipo.setSelection(i);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Trae los datos de los miembros que conforman el hogar y los muestra en los campos de datos, Parametro salida; String

    public void modificarDatosMiembro(View view){

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nombre= et_nombreMiembro.getText().toString();
                String apellido= et_apellidoMiembro.getText().toString();
                String correo= et_correoMiembro.getText().toString();
                String password= et_passwordMiembro.getText().toString();
                String telefono = et_telefonoMiembro.getText().toString();
                String tipo = spinnerTipo.getSelectedItem().toString();

                if (nombre.equals("") || apellido.equals("") ||
                        correo.equals("") || password.equals("") ||
                        telefono.equals("")  || tipo.equals("Tipo")){
                    validacion();
                }
                else{
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Usuario u = objSnapshot.getValue(Usuario.class);
                        if (nombreMiembro.equals(u.getNombre())&& apellidoMiembro.equals(u.getApellido())){
                            u.setRut(u.getRut());
                            u.setNombre(nombre);
                            u.setApellido(apellido);
                            u.setCorreo(correo);
                            u.setPassword(password);
                            u.setTelefono(telefono);
                            u.setTipo(tipo);
                            u.setHogar(u.getHogar());
                        }
                        databaseReference.child("Usuario").child(u.getRut()).setValue(u);
                        limpiarCajas();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Modifica los datos del miembro seleccionado anteriormente, Parametro entrada: String, Parametro salida: Usuario

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuario,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Setea el menu en el objeto Toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.verVecindario:{
                Intent intent = new Intent(this,MapsActivity.class);
                startActivity(intent);

                break;
            } // Inicia la vista (Activity) MapsActivity que muestra el mapa

            case R.id.ingresarEventos:{
                Intent intent = new Intent(this,ingresarEvento.class);
                startActivity(intent);

                break;
            } // Inicia la vista (Activity) Ingresar evento

            case R.id.editarHogar:{
                Intent intent = new Intent(this,editarHogar.class);
                startActivity(intent);

                break;
            } // Inicia la vista (Activity) Editar Hogar

            case R.id.datosUsuario:{
                Intent intent = new Intent(this,datosUsuario.class);
                startActivity(intent);

                break;
            } // Inicia la vista (Activity) Datos Usuario

            case R.id.cerrarSesion:{
                SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferecias.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this,com.example.mivecindarionuevo.iniciarSesion.class);
                startActivity(intent);
                finish();
            } // Cierra la sesion del usuario actual
            default:break;
        }
        return true;
    } // Metodos de las opciones del menu

    private void cargarPreferencias() {
        SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        nmUsuario = preferecias.getString("nombreUsuario","NoSesion");
        apUsuario = preferecias.getString("apellidoUsuario","NoSesion");
        toolbar.setSubtitle(nmUsuario+" "+apUsuario);

        SharedPreferences usuarioModificar = getSharedPreferences("usuarioModificar", Context.MODE_PRIVATE);
        nombreMiembro = usuarioModificar.getString("nombreUsuario","NoMiembro");
        apellidoMiembro  = usuarioModificar.getString("apellidoUsuario", "NoMiembro");
    } // Carga la sesion del usuario.

    private void validacion() {

        String password = et_passwordMiembro.getText().toString();
        String nombre = et_nombreMiembro.getText().toString();
        String apellido = et_apellidoMiembro.getText().toString();
        String telefono = et_telefonoMiembro.getText().toString();
        String correo = et_correoMiembro.getText().toString();

        if (nombre.equals("")) {
            et_nombreMiembro.setError("Requerido");
        }
        else if (apellido.equals("")){
            et_apellidoMiembro.setError("Requerido");
        }

        else if (telefono.equals("")){
            et_telefonoMiembro.setError("Requerido");
        }
        else if (correo.equals("")){
            et_correoMiembro.setError("Requerido");
        }
        else if (password.equals("")){
            et_passwordMiembro.setError("Requerido");
        }
        else if (spinnerTipo.getSelectedItem().equals("Tipo")){
            Toast.makeText(this,"Debe seleccionar el tipo de usuario", Toast.LENGTH_LONG).show();
        }

    } // Valida que los campos de datos no esten vacios

    private void limpiarCajas() {

        et_nombreMiembro.setText("");
        et_apellidoMiembro.setText("");
        et_telefonoMiembro.setText("");
        et_correoMiembro.setText("");
        et_passwordMiembro.setText("");

    } // Limpia los campos de datos

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializa la base de datos, en este caso Firebase
}
