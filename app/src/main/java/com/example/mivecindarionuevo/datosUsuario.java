package com.example.mivecindarionuevo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mivecindarionuevo.modelos.Hogar;
import com.example.mivecindarionuevo.modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class datosUsuario extends AppCompatActivity {

    EditText nomUsuario, apeUsuario, corUsuario, passUsuario, telUsuario, dirUsuario;

    String nmUsuario,apUsuario;

    Toolbar toolbar;

    TextView txt_miNombre,txt_miApellido,txt_miDireccion,txt_miTelefono, txt_miCorreo;

    Button btn_alarma, btn_desactivarAlarma;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        incializarFirebase();

        nomUsuario=findViewById(R.id.et_nombreUsuarioM);
        apeUsuario=findViewById(R.id.et_apellidoUsuarioM);
        corUsuario=findViewById(R.id.et_correoUsuarioM);
        passUsuario=findViewById(R.id.et_passUsuarioM);
        telUsuario=findViewById(R.id.et_telefonoUsuarioM);
        dirUsuario=findViewById(R.id.et_direccionUsuarioM);
        btn_alarma=findViewById(R.id.btn_agregarAlarma);
        btn_desactivarAlarma=findViewById(R.id.btn_desactivarAlarma);
        txt_miNombre=findViewById(R.id.txt_miNombre);
        txt_miApellido=findViewById(R.id.txt_miApellido);
        txt_miDireccion=findViewById(R.id.txt_miDireccion);
        txt_miTelefono=findViewById(R.id.txt_miTelefono);
        txt_miCorreo=findViewById(R.id.txt_miCorreo);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cargarPreferencias();
        mostrarMisDatos();

        btn_desactivarAlarma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { // Este metodo se encarga de cambiar la alarma a apagada, Parametro entrada; Boolean Parametro salida: Hogar
                databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {

                    String hogarUsuario;
                    String idusuario;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Hogar h = new Hogar();
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            Usuario u = objSnapshot.getValue(Usuario.class);
                            if (u.getNombre().equals(nmUsuario) && u.getApellido().equals(apUsuario)) {
                                hogarUsuario = u.getHogar().getUid();
                                idusuario = u.getRut();
                                h.setNombre(u.getHogar().getNombre());
                                h.setDireccion(u.getHogar().getDireccion());
                                h.setComentario(u.getHogar().getComentario());
                                h.setUid(hogarUsuario);
                                h.setAlarma(false);
                                h.setLatitud(u.getHogar().getLatitud());
                                h.setLongitud(u.getHogar().getLongitud());
                                h.setVecindario(u.getHogar().getVecindario());
                            }
                        }
                        databaseReference.child("Hogar").child(hogarUsuario).setValue(h);
                        databaseReference.child("Usuario").child(idusuario).child("hogar").setValue(h);
                        Intent intent = new Intent(datosUsuario.this, MapsActivity.class);
                        startActivity(intent);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_alarma.setOnClickListener(new View.OnClickListener() {// Este metodo se encarga de cambiar la alarma a encendida, Parametro entrada; Boolean Parametro salida: Hogar

            @Override
            public void onClick(View view) {

                databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {

                    String hogarUsuario;
                    String idusuario;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Hogar h = new Hogar();
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            Usuario u = objSnapshot.getValue(Usuario.class);
                            if (u.getNombre().equals(nmUsuario) && u.getApellido().equals(apUsuario)) {
                                hogarUsuario = u.getHogar().getUid();
                                idusuario = u.getRut();
                                h.setNombre(u.getHogar().getNombre());
                                h.setDireccion(u.getHogar().getDireccion());
                                h.setComentario(u.getHogar().getComentario());
                                h.setUid(hogarUsuario);
                                h.setAlarma(true);
                                h.setLatitud(u.getHogar().getLatitud());
                                h.setLongitud(u.getHogar().getLongitud());
                                h.setVecindario(u.getHogar().getVecindario());
                            }
                        }
                        databaseReference.child("Hogar").child(hogarUsuario).setValue(h);
                        databaseReference.child("Usuario").child(idusuario).child("hogar").setValue(h);
                        Intent intent = new Intent(datosUsuario.this, MapsActivity.class);
                        startActivity(intent);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    public void mostrarMisDatos(){
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = objSnapshot.getValue(Usuario.class);
                    if (nmUsuario.equals(usuario.getNombre()) && apUsuario.equals(usuario.getApellido())) {
                        txt_miNombre.setText(usuario.getNombre());
                        txt_miApellido.setText(usuario.getApellido());
                        txt_miDireccion.setText(usuario.getDireccion());
                        txt_miTelefono.setText(usuario.getTelefono());
                        txt_miCorreo.setText(usuario.getCorreo());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Muestra los datos del usuario que inicio sesion, Parametro salida: String

    public void modificarDatosUsuario(View v) {

        String nombre = nomUsuario.getText().toString();
        String apellido = apeUsuario.getText().toString();
        String correo = corUsuario.getText().toString();
        String password = passUsuario.getText().toString();
        String telefono = telUsuario.getText().toString();
        String direccion = dirUsuario.getText().toString();

        if (nombre.equals("") || apellido.equals("") || correo.equals("") || password.equals("") || telefono.equals("") ||
                direccion.equals("")) {
            validacion();
        }
        else{
            databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
                Usuario u = new Usuario();
                String uidUsuario;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = objSnapshot.getValue(Usuario.class);
                        if (nmUsuario.equals(usuario.getNombre()) && apUsuario.equals(usuario.getApellido())) {
                            uidUsuario = usuario.getRut();
                            u.setRut(uidUsuario);
                            u.setNombre(nomUsuario.getText().toString());
                            u.setApellido(apeUsuario.getText().toString());
                            u.setCorreo(corUsuario.getText().toString());
                            u.setPassword(passUsuario.getText().toString());
                            u.setTelefono(telUsuario.getText().toString());
                            u.setDireccion(dirUsuario.getText().toString());
                            u.setTipo(usuario.getTipo());
                            u.setHogar(usuario.getHogar());
                        }
                    }
                    databaseReference.child("Usuario").child(uidUsuario).setValue(u);
                    limpiarCajas();
                    SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferecias.edit();
                    editor.putString("nombreUsuario", u.getNombre());
                    editor.putString("apellidoUsuario", u.getApellido());
                    editor.apply();
                    Intent intent = new Intent(datosUsuario.this,datosUsuario.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    } // Modifica los datos del usuario que inicio sesion

    private void incializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    // Inicializa la base de datos, en este caso Firebase

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuario,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Setea el menu en el objeto Toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.verVecindario:{ // Inicia la vista (Activity) MapsActivity que mustra el mapa del vecindario
                Intent intent = new Intent(this,MapsActivity.class);
                startActivity(intent);

                break;
            }

            case R.id.editarHogar:{ // Inicia la vista (Activity) editarHogar
                Intent intent = new Intent(this,editarHogar.class);
                startActivity(intent);

                break;
            }

            case R.id.datosUsuario:{ // Inicia la vista (Activity) datosUsuario
                Intent intent = new Intent(this,datosUsuario.class);
                startActivity(intent);

                break;
            }

            case R.id.ingresarEventos:{ // Inicia la vista (Activity) ingresarEvento
                Intent intent = new Intent(this,ingresarEvento.class);
                startActivity(intent);
                break;
            }

            case R.id.cerrarSesion:{ // Cierra la sesion del usuario
                SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferecias.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this,com.example.mivecindarionuevo.iniciarSesion.class);
                startActivity(intent);
                finish();
            }
            default:break;

        }
        return true;
    } // Metodos de las opciones del menu

    private void limpiarCajas() {

        nomUsuario.setText("");
        apeUsuario.setText("");
        corUsuario.setText("");
        passUsuario.setText("");
        telUsuario.setText("");
        dirUsuario.setText("");
    }

    // Limpia los campos de datos

    private void validacion() {

        String nombre = nomUsuario.getText().toString();
        String apellido = apeUsuario.getText().toString();
        String correo = corUsuario.getText().toString();
        String password = passUsuario.getText().toString();
        String telefono = telUsuario.getText().toString();
        String direccion = dirUsuario.getText().toString();


        if (nombre.equals("")){
            nomUsuario.setError("Requerido");
        }
        else if (apellido.equals("")){
            apeUsuario.setError("Requerido");
        }
        else if (correo.equals("")){
            corUsuario.setError("Requerido");
        }
        else if (password.equals("")){
            passUsuario.setError("Requerido");
        }
        else if (telefono.equals("")){
            telUsuario.setError("Requerido");
        }
        else if (direccion.equals("")){
            dirUsuario.setError("Requerido");
        }


    }

    // Valida que los campos de datos esten vacios

    private void cargarPreferencias() {
        SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        nmUsuario = preferecias.getString("nombreUsuario","NoSesion");
        apUsuario = preferecias.getString("apellidoUsuario","NoSesion");
        toolbar.setSubtitle(nmUsuario+" "+apUsuario);
    }

    // Carga la sesion del usuario.

}