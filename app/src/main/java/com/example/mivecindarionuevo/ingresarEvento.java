package com.example.mivecindarionuevo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mivecindarionuevo.modelos.Evento;
import com.example.mivecindarionuevo.modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ingresarEvento extends AppCompatActivity {

    private Toolbar toolbar;

    private String tipoUsuario,nmUsuario,apUsuario;

    private EditText comentarioEvento,fechaEvento;

    private Button btnEvento,btnAsistir;

    private ListView lv_Eventos,lv_asistentes;

    Usuario usuarioActual;

    private List<Evento> listaEventos = new ArrayList<Evento>();
    private  List<Usuario> listaAsistentes = new ArrayList<Usuario>();
    private ArrayAdapter<Evento> arrayAdapteEvento;

    private ArrayAdapter<Usuario> arrayAdapteAsistente;

    private Spinner tipoEvento;

    private TextView txt_tipoEvento,txt_comentarioEvento,txt_usuarioEvento,txt_fechaEvento;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Evento eventoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_evento);
        comentarioEvento = findViewById(R.id.et_comentarioEvento);
        fechaEvento = findViewById(R.id.et_fechaEvento);
        tipoEvento = findViewById(R.id.spEvento);
        btnEvento = findViewById(R.id.btnEventos);
        btnAsistir = findViewById(R.id.btnAsistir);
        lv_Eventos = findViewById(R.id.lv_eventos);
        lv_asistentes = findViewById(R.id.lv_asistentes);
        txt_comentarioEvento = findViewById(R.id.txt_comentarioEventoSeleccionado);
        txt_tipoEvento = findViewById(R.id.txt_tipoEventoSeleccionado);
        txt_usuarioEvento = findViewById(R.id.txt_usuarioEventoSeleccionado);
        txt_fechaEvento = findViewById(R.id.txt_fechaEventoSeleccionado);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource
                (this,R.array.TipoEvento, android.R.layout.simple_spinner_dropdown_item);

        tipoEvento.setAdapter(adapterSpinner);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicializarFirebase();
        cargarPreferencias();
        listarDatos();
        tiposUsuarios();

        lv_Eventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                cargarPreferencias();
                eventoSeleccionado = (Evento) parent.getItemAtPosition(position);
                txt_comentarioEvento.setText(eventoSeleccionado.getComentario());
                txt_tipoEvento.setText(eventoSeleccionado.getTipo());
                txt_fechaEvento.setText(eventoSeleccionado.getFecha());
                txt_usuarioEvento.setText(eventoSeleccionado.getUsuario().getNombre()
                        +" "+eventoSeleccionado.getUsuario().getApellido());
                arrayAdapteAsistente = new ArrayAdapter<>(ingresarEvento.this,
                        android.R.layout.simple_list_item_1,eventoSeleccionado.getListaAsistentes());
                lv_asistentes.setAdapter(arrayAdapteAsistente);
                btnAsistir.setVisibility(View.VISIBLE);

                for (int i = 0 ; i < eventoSeleccionado.getListaAsistentes().size(); i++){
                    if (eventoSeleccionado.getListaAsistentes().get(i).getNombre()
                            .equals(nmUsuario) && eventoSeleccionado.getListaAsistentes()
                            .get(i).getApellido().equals(apUsuario)){
                        btnAsistir.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public void participar(View v){
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    if (nmUsuario.equals(u.getNombre()) && apUsuario.equals(u.getApellido())){
                        eventoSeleccionado.getListaAsistentes().add(u);
                    }
                    databaseReference.child("Evento").child(eventoSeleccionado.getUid())
                            .setValue(eventoSeleccionado);
                    arrayAdapteAsistente = new ArrayAdapter<>(ingresarEvento.this,
                            android.R.layout.simple_list_item_1,eventoSeleccionado.getListaAsistentes());
                    lv_asistentes.setAdapter(arrayAdapteAsistente);
                    btnAsistir.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Agrega el objeto Usuario en una lista que sera tratada como los Participantes del evento, Parametro entrada: Usuario, Parametro salida: List


    private void tiposUsuarios(){

        switch (tipoUsuario){

            case "Presidente junta vecinos":{
                btnEvento.setOnClickListener(new View.OnClickListener() {
                   @Override
                    public void onClick(View view) {
                        ingresarEvento();
                    }
                });
                break;
            }

            case "Jefe de hogar":{
               btnEvento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingresarEvento();
                    }
                });
                break;
           }

            case "Miembro de hogar":{
                btnEvento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingresarEvento();
                    }
                });
                break;


            }

        }
    }

    private void ingresarEvento() {
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String comentario = comentarioEvento.getText().toString();
                String tipo = tipoEvento.getSelectedItem().toString();
                String fecha = fechaEvento.getText().toString();

                if (comentario.equals("") || tipo.equals("Tipo")){
                    validacion();
                }
                else{
                    Evento e = new Evento();
                    e.setUid(UUID.randomUUID().toString());
                    e.setComentario(comentario);
                    e.setTipo(tipo);
                    e.setFecha(fecha);
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Usuario u = objSnapshot.getValue(Usuario.class);
                        if (nmUsuario.equals(u.getNombre()) && apUsuario.equals(u.getApellido())){
                            listaAsistentes.add(u);
                            e.setListaAsistentes(listaAsistentes);
                            e.setUsuario(u);
                        }
                    }
                    databaseReference.child("Evento").child(e.getUid()).setValue(e);
                    Toast.makeText(ingresarEvento.this, "Evento agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Ingresa un Evento a la base de datos, Parametro entrada: String, Parametro salida: Evento

    private void listarDatos() {

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaEventos.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    if (u.getNombre().equals(nmUsuario) && u.getApellido().equals(apUsuario)){
                        usuarioActual = u;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Evento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Evento e = objSnapshot.getValue(Evento.class);
                    if (usuarioActual.getHogar().getVecindario().getNombre().equals(e.getUsuario()
                            .getHogar().getVecindario().getNombre())){
                        listaEventos.add(e);
                        arrayAdapteEvento = new ArrayAdapter<Evento>(ingresarEvento.this,
                                android.R.layout.simple_list_item_1, listaEventos);
                        lv_Eventos.setAdapter(arrayAdapteEvento);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Lista los eventos que estan en la base de datos, Parametro salida: List

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
            } // Inicia la vista (Activity) MapsActivity que muestra el mapa del vecindario

            case R.id.ingresarEventos:{
                Intent intent = new Intent(this,ingresarEvento.class);
                startActivity(intent);

                break;
            } // Inicia la vista (Activity) Ingresar evento

            case R.id.editarHogar:{
                Intent intent = new Intent(this,editarHogar.class);
                startActivity(intent);

                break;
            } // Inicia la vista (Activity) Editar hogar

            case R.id.datosUsuario:{
                Intent intent = new Intent(this,datosUsuario.class);
                startActivity(intent);

                break;
            } // Inicia la vista (Activity) Datos usuario

            case R.id.cerrarSesion:{
                SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferecias.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this,com.example.mivecindarionuevo.iniciarSesion.class);
                startActivity(intent);
                finish();
            } // Cierra la sesion del usuario
            default:break;

        }
        return true;
    } // Metodos de las opcion del menu

    private void validacion() {

        String comentario = comentarioEvento.getText().toString();
        String fecha = fechaEvento.getText().toString();

        if (comentario.equals("")) {
            comentarioEvento.setError("Requerido");
        } else if (tipoEvento.getSelectedItem().toString().equals("Tipo")) {
            Toast.makeText(this,"Debe seleccionar el tipo", Toast.LENGTH_LONG).show();
        }else if (fecha.equals("")) {
            fechaEvento.setError("Requerido");
        }
    }

    // Valida que los campos de datos no esten vacios

    private void limpiarCajas() {

        comentarioEvento.setText("");
        fechaEvento.setText("");
        tipoEvento.setSelection(0);

    } // Limpia los campos de datos

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializa la conexion con la base de datos, en este caso Firebase

    private void cargarPreferencias() {
        SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        nmUsuario = preferecias.getString("nombreUsuario","NoSesion");
        apUsuario = preferecias.getString("apellidoUsuario","NoSesion");
        tipoUsuario = preferecias.getString("tipoUsuario","NoSesion");
        toolbar.setSubtitle(nmUsuario+" "+apUsuario);
    } // Carga la sesion del usuario.

}
