package com.example.mivecindarionuevo;

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

import java.util.ArrayList;
import java.util.List;

public class editarHogar extends AppCompatActivity {

    Toolbar toolbar;

    EditText comentarioHogar, direccionHogar, nombreHogar;

    String nmUsuario,apUsuario,tipoUsuario;

    ListView lv_miembrosHogar;

    TextView txt_nombreHogar, txt_direccionHogar, txt_comentarioHogar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Usuario> listaMiembros = new ArrayList<Usuario>();

    private ArrayAdapter<Usuario> arrayAdapteMiembro;

    Usuario usuarioActual;

    Usuario usuarioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_hogar);
        inicializarFirebase();

        lv_miembrosHogar=findViewById(R.id.lv_miembrosHogar);
        txt_comentarioHogar=findViewById(R.id.txt_nombreHogar);
        txt_direccionHogar=findViewById(R.id.txt_direccionHogar);
        txt_nombreHogar=findViewById(R.id.txt_comentarioHogar);
        comentarioHogar = findViewById(R.id.et_comentarioUsuarioHogar);
        direccionHogar = findViewById(R.id.et_direccionHogarUsuario);
        nombreHogar = findViewById(R.id.et_nombreUsuarioHogar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cargarPreferencias();
        listarDatos();
        tipoUsuarioModificar();
    }

    private void tipoUsuarioModificar(){

        if (tipoUsuario.equals("Jefe de hogar") || tipoUsuario.equals("Presidente junta vecinos")){
            lv_miembrosHogar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    usuarioSeleccionado = (Usuario) parent.getItemAtPosition(position);
                    SharedPreferences usuarioModificar = getSharedPreferences("usuarioModificar", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorUsuario = usuarioModificar.edit();
                    editorUsuario.putString("nombreUsuario", usuarioSeleccionado.getNombre());
                    editorUsuario.putString("apellidoUsuario",usuarioSeleccionado.getApellido());
                    editorUsuario.commit();
                    Intent intent = new Intent(editarHogar.this,modificarMiembro.class);
                    startActivity(intent);
                }
            });
        }

    }

    public void modificarHogar( View v){
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {

            String comentario = comentarioHogar.getText().toString();
            String direccion = direccionHogar.getText().toString();
            String nombre = nombreHogar.getText().toString();
            String idusuario;
            String hogarUsuario;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Hogar h = new Hogar();
                if (comentario.equals("") || direccion.equals("") || nombre.equals("")){
                    validacion();
                }
                else{
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Usuario u = objSnapshot.getValue(Usuario.class);
                        if (nmUsuario.equals(u.getNombre()) && apUsuario.equals(u.getApellido())){
                            hogarUsuario = u.getHogar().getUid();
                            idusuario = u.getRut();
                            h.setNombre(nombre);
                            h.setDireccion(direccion);
                            h.setComentario(comentario);
                            h.setUid(hogarUsuario);
                            h.setAlarma(null);
                            h.setLatitud(u.getHogar().getLatitud());
                            h.setLongitud(u.getHogar().getLongitud());
                            h.setVecindario(u.getHogar().getVecindario());
                        }
                    }
                    databaseReference.child("Hogar").child(hogarUsuario).setValue(h);
                    databaseReference.child("Usuario").child(idusuario).child("hogar").setValue(h);
                    limpiarCajas();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Modifica el hogar del usuario que inicio sesion, Parametro entrada: String, Parametro salida: Hogar

    private void listarDatos() {

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    if (u.getNombre().equals(nmUsuario) && u.getApellido().equals(apUsuario)){
                        usuarioActual=u;
                        txt_nombreHogar.setText(usuarioActual.getHogar().getNombre());
                        txt_comentarioHogar.setText(usuarioActual.getHogar().getComentario());
                        txt_direccionHogar.setText(usuarioActual.getHogar().getDireccion());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaMiembros.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    if (usuarioActual.getHogar().getUid().equals(u.getHogar().getUid())){
                        listaMiembros.add(u);
                        arrayAdapteMiembro = new ArrayAdapter<Usuario>(editarHogar.this,
                                android.R.layout.simple_list_item_1, listaMiembros);
                        lv_miembrosHogar.setAdapter(arrayAdapteMiembro);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Lista los datos del hogar de usuario que inicio seison, Parametro salida: List

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
        tipoUsuario = preferecias.getString("tipoUsuario","NoSesion");
        toolbar.setSubtitle(nmUsuario+" "+apUsuario);
    }

    // Carga la sesion del usuario.

    private void validacion() {

        String comentario = comentarioHogar.getText().toString();
        String nombre = nombreHogar.getText().toString();
        String direccion = direccionHogar.getText().toString();

        if (comentario.equals("")) {
            comentarioHogar.setError("Requerido");
        } else if (nombre.equals("")){
            nombreHogar.setError("Requerido");
        }else if (direccion.equals("")){
            direccionHogar.setError("Requerido");
        }

    }

    // Valida que los campos de datos no esten vacios

    private void limpiarCajas() {

        comentarioHogar.setText("");
        nombreHogar.setText("");
        direccionHogar.setText("");

    }

    // Limpia los campos de datos

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializa la base de datos, en este caso Firebase


}
