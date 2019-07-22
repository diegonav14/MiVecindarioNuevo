package com.example.mivecindarionuevo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mivecindarionuevo.modelos.Alarma;
import com.example.mivecindarionuevo.modelos.Hogar;
import com.example.mivecindarionuevo.modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ingresarAlarma extends AppCompatActivity {

    private List<String> listaHogar = new ArrayList<String>();

    String tipoAlarma,nmUsuario,apUsuario;

    EditText et_suceso;

    TextView txt_tipoAlarma;

    Button btn_ingresar;

    Spinner spHogar;

    Toolbar toolbar;

    Usuario usuarioActual;

    ImageView iconoInicio;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_alarma);

        et_suceso = findViewById(R.id.et_suceso);
        spHogar = findViewById(R.id.sp_hogarSuceso);
        iconoInicio = findViewById(R.id.iv_alarmaInicio);
        txt_tipoAlarma = findViewById(R.id.txt_tipoAlarma);
        btn_ingresar = findViewById(R.id.btn_ingresarAlarma);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaHogar.add("Seleccione el hogar de la emergencia");

        ArrayAdapter<String> adapterSpinnerHogar = new ArrayAdapter<>
                (this,android.R.layout.simple_spinner_dropdown_item, listaHogar);

        spHogar.setAdapter(adapterSpinnerHogar);

        tipoAlarma();
        estadoInternet();
        inicializarFirebase();
        cargarPreferencias();
        iconoTipoAlarma();
        spinnerHogar ();

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                switch (tipoAlarma){

                    case "Vehiculo":

                        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String suceso = et_suceso.getText().toString();
                                String hogar = spHogar.getSelectedItem().toString();


                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String fecha = df.format(c);

                                if (suceso.equals("") || hogar.equals("Seleccione el hogar de la emergencia")){
                                    validacion();
                                }
                                else{
                                    Alarma a = new Alarma();
                                    a.setUid(UUID.randomUUID().toString());
                                    a.setSuceso(suceso);
                                    a.setFecha(fecha);
                                    a.setTipo(tipoAlarma);
                                    a.setUsuario(usuarioActual);
                                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                                        Hogar h = objSnapshot.getValue(Hogar.class);
                                        if (h.getNombre().equals(hogar)){
                                            a.setHogar(h);
                                        }
                                    }

                                    databaseReference.child("Alarma").child(a.getUid()).setValue(a);
                                    Toast.makeText(ingresarAlarma.this, "Alarma agregada", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }

                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;

                    case "Ambulancia":

                        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String suceso = et_suceso.getText().toString();
                                String hogar = spHogar.getSelectedItem().toString();

                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String fecha = df.format(c);

                                Alarma a = new Alarma();
                                a.setUid(UUID.randomUUID().toString());
                                a.setSuceso(suceso);
                                a.setFecha(fecha);
                                a.setTipo(tipoAlarma);
                                a.setUsuario(usuarioActual);

                                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                                    Hogar h = objSnapshot.getValue(Hogar.class);

                                    if (h.getNombre().equals(hogar)){
                                        a.setHogar(h);
                                    }
                                }

                                databaseReference.child("Alarma").child(a.getUid()).setValue(a);
                                Toast.makeText(ingresarAlarma.this, "Alarma agregada", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;

                    case "Bomberos":

                        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String suceso = et_suceso.getText().toString();
                                String hogar = spHogar.getSelectedItem().toString();

                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String fecha = df.format(c);

                                Alarma a = new Alarma();
                                a.setUid(UUID.randomUUID().toString());
                                a.setSuceso(suceso);
                                a.setFecha(fecha);
                                a.setTipo(tipoAlarma);
                                a.setUsuario(usuarioActual);

                                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                                    Hogar h = objSnapshot.getValue(Hogar.class);

                                    if (h.getNombre().equals(hogar)){
                                        a.setHogar(h);
                                    }
                                }

                                databaseReference.child("Alarma").child(a.getUid()).setValue(a);
                                Toast.makeText(ingresarAlarma.this, "Alarma agregada", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;

                    case "Mascota":

                        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String suceso = et_suceso.getText().toString();
                                String hogar = spHogar.getSelectedItem().toString();

                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String fecha = df.format(c);

                                Alarma a = new Alarma();
                                a.setUid(UUID.randomUUID().toString());
                                a.setSuceso(suceso);
                                a.setFecha(fecha);
                                a.setTipo(tipoAlarma);
                                a.setUsuario(usuarioActual);

                                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                                    Hogar h = objSnapshot.getValue(Hogar.class);

                                    if (h.getNombre().equals(hogar)){
                                        a.setHogar(h);
                                    }
                                }

                                databaseReference.child("Alarma").child(a.getUid()).setValue(a);
                                Toast.makeText(ingresarAlarma.this, "Alarma agregada", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;

                    case "Seguridad":

                        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String suceso = et_suceso.getText().toString();
                                String hogar = spHogar.getSelectedItem().toString();

                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String fecha = df.format(c);

                                Alarma a = new Alarma();
                                a.setUid(UUID.randomUUID().toString());
                                a.setSuceso(suceso);
                                a.setFecha(fecha);
                                a.setTipo(tipoAlarma);
                                a.setUsuario(usuarioActual);

                                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                                    Hogar h = objSnapshot.getValue(Hogar.class);

                                    if (h.getNombre().equals(hogar)){
                                        a.setHogar(h);
                                    }
                                }

                                databaseReference.child("Alarma").child(a.getUid()).setValue(a);
                                Toast.makeText(ingresarAlarma.this, "Alarma agregada", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;

                }

            }
        });

    }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializa la conexion con la base de datos, en este caso Firebase

    private void estadoInternet (){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Intent intent = new Intent(this, estadoInternet.class);
            startActivity(intent);
            finish();
        }

    }

    private void cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        nmUsuario = preferencias.getString("nombreUsuario","NoSesion");
        apUsuario = preferencias.getString("apellidoUsuario","NoSesion");
    } // Carga la sesion del usuario

    private void tipoAlarma() {
        SharedPreferences tipo_alarma = getSharedPreferences("alarma", Context.MODE_PRIVATE);
        tipoAlarma = tipo_alarma.getString("tipoAlarma","NoSesion");
    }

    private void iconoTipoAlarma(){

        switch (tipoAlarma){
            case "Vehiculo":
                txt_tipoAlarma.setText("Robo de Vehiculo");
                iconoInicio.setBackground(getApplication().getResources().getDrawable(R.drawable.auto));
                break;

            case "Ambulancia":
                txt_tipoAlarma.setText("Emergencia medica");
                iconoInicio.setBackground(getApplication().getResources().getDrawable(R.drawable.ambulancia));
                break;

            case "Bomberos":
                txt_tipoAlarma.setText("Emergencia bomberos");
                iconoInicio.setBackground(getApplication().getResources().getDrawable(R.drawable.bomberos));
                break;

            case "Mascota":
                txt_tipoAlarma.setText("Perdida/robo de mascota");
                iconoInicio.setBackground(getApplication().getResources().getDrawable(R.drawable.mascota));
                break;

            case "Seguridad":
                txt_tipoAlarma.setText("Emergencia seguridad");
                iconoInicio.setBackground(getApplication().getResources().getDrawable(R.drawable.alarma_robo));
                break;

        }

    }

    private void spinnerHogar (){

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Hogar h = objSnapshot.getValue(Hogar.class);
                    if (usuarioActual.getHogar().getVecindario().getNombre().equals(h.getVecindario().getNombre())){
                        listaHogar.add(h.getNombre());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuario,menu);
        return super.onCreateOptionsMenu(menu);
    } // Setea el menu en el objeto Toolbar

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

    private void validacion() {

        String suceso = et_suceso.getText().toString();
        String hogar = spHogar.getSelectedItem().toString();

        if (suceso.equals("")) {
            et_suceso.setError("Se requiere un suceso");
        } else if (hogar.equals("Seleccione el hogar de la emergencia")) {
            Toast.makeText(this,"Debe seleccionar el hogar", Toast.LENGTH_LONG).show();
        }

    }

}
