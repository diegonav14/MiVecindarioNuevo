package com.example.mivecindarionuevo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.mivecindarionuevo.modelos.Alarma;
import com.example.mivecindarionuevo.modelos.Hogar;
import com.example.mivecindarionuevo.modelos.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements  OnMapReadyCallback  {

    GoogleMap mMap;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Button btnAlarma;

    ImageView iv_seguridad, iv_bomberos, iv_mascotas, iv_ambulancia, iv_auto;

    String nmUsuario, apUsuario;


    Usuario usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnAlarma = findViewById(R.id.btnAlarma);

        btnAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                LayoutInflater alarma_alerta = LayoutInflater.from(MapsActivity.this);
                final View alerta = alarma_alerta.inflate(R.layout.alerta_alarmas,null);
                builder.setView(alerta);
                builder.setTitle("Seleccione alarma");

                iv_ambulancia = alerta.findViewById(R.id.iv_ambulancia);
                iv_auto = alerta.findViewById(R.id.iv_auto);
                iv_bomberos = alerta.findViewById(R.id.iv_bomberos);
                iv_mascotas = alerta.findViewById(R.id.iv_mascotas);
                iv_seguridad = alerta.findViewById(R.id.iv_seguridad);

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();

                iv_ambulancia.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences tipo_alarma = getSharedPreferences("alarma", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = tipo_alarma.edit();
                        editor.putString("tipoAlarma", "Ambulancia");
                        editor.apply();
                        Intent intent = new Intent(MapsActivity.this,ingresarAlarma.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

                iv_auto.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences tipo_alarma = getSharedPreferences("alarma", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = tipo_alarma.edit();
                        editor.putString("tipoAlarma", "Vehiculo");
                        editor.apply();
                        Intent intent = new Intent(MapsActivity.this,ingresarAlarma.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

                iv_bomberos.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences tipo_alarma = getSharedPreferences("alarma", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = tipo_alarma.edit();
                        editor.putString("tipoAlarma", "Bomberos");
                        editor.apply();
                        Intent intent = new Intent(MapsActivity.this,ingresarAlarma.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

                iv_mascotas.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences tipo_alarma = getSharedPreferences("alarma", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = tipo_alarma.edit();
                        editor.putString("tipoAlarma", "Mascota");
                        editor.apply();
                        Intent intent = new Intent(MapsActivity.this,ingresarAlarma.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

                iv_seguridad.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences tipo_alarma = getSharedPreferences("alarma", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = tipo_alarma.edit();
                        editor.putString("tipoAlarma", "Seguridad");
                        editor.apply();
                        Intent intent = new Intent(MapsActivity.this,ingresarAlarma.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

            }
        });

        inicializarFirebase();
        cargarPreferencias();
        estadoInternet();

    }


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

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializa la conexion con la base de datos, en este caso Firebase

    private void cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        nmUsuario = preferencias.getString("nombreUsuario","NoSesion");
        apUsuario = preferencias.getString("apellidoUsuario","NoSesion");
    } // Carga la sesion del usuario

    public void ingresarEvento (View v){
        Intent intent = new Intent(this,ingresarEvento.class);
        startActivity(intent);

    } // Inicia la vista (Activity) Ingresar evento

    public void misDatos (View v){
        Intent intent = new Intent(this,datosUsuario.class);
        startActivity(intent);

    } // Inicia la vista (Activity) Datos usuario

    public void miHogar (View v){
        Intent intent = new Intent(this,editarHogar.class);
        startActivity(intent);

    } // Inicia la vista (Activity) Editar hogar

    public void cerrarSesion (View v){
        SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferecias.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this,iniciarSesion.class);
        startActivity(intent);
        finish();
    } // Cerrar sesion del usuario


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMinZoomPreference(17.0f);
        agregarMarcadores(googleMap);
        agregarAlarmas(googleMap);
        // Add a marker in Sydney and move the camera
    } // Metodo propio de la clase GoogleMap que se encarga de los metodos del mapa


    public void agregarMarcadores(GoogleMap googleMap){

        mMap = googleMap;

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
                        double latitud = Double.parseDouble(h.getLatitud());
                        double longitud = Double.parseDouble(h.getLongitud());
                        LatLng padreHurtado = new LatLng(latitud,longitud);
                        if (usuarioActual.getHogar().getAlarma()){
                            mMap.addMarker(new MarkerOptions().position(padreHurtado).title
                                    (h.getComentario()+" "+h.getDireccion()+" "+h.getNombre())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_alarma_round)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));
                        }else{
                            mMap.addMarker(new MarkerOptions().position(padreHurtado).title
                                    (h.getComentario()+" "+h.getDireccion()+" "+h.getNombre())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_casmarket_round)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    } // Metodo que crea los marcadores de los hogares del vecindario

    public void agregarAlarmas(GoogleMap googleMap){

        mMap = googleMap;

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

        databaseReference.child("Alarma").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String fecha = df.format(c);

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Alarma a = objSnapshot.getValue(Alarma.class);
                    if (a.getFecha().equals(fecha)){

                        String tipoAlarma = a.getTipo();

                        double latitud = Double.parseDouble(a.getHogar().getLatitud());
                        double longitud = Double.parseDouble(a.getHogar().getLongitud());

                        LatLng padreHurtado = new LatLng(latitud,longitud);

                        switch (tipoAlarma){
                            case "Vehiculo":

                                mMap.addMarker(new MarkerOptions().position(padreHurtado).title
                                        (a.getTipo())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.auto)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));

                                break;

                            case "Ambulancia":

                                mMap.addMarker(new MarkerOptions().position(padreHurtado).title
                                        (a.getTipo())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancia)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));

                                break;

                            case "Bomberos":

                                mMap.addMarker(new MarkerOptions().position(padreHurtado).title
                                        (a.getTipo())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bomberos)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));

                                break;

                            case "Mascota":

                                mMap.addMarker(new MarkerOptions().position(padreHurtado).title
                                        (a.getTipo())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mascota)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));

                                break;

                            case "Seguridad":

                                mMap.addMarker(new MarkerOptions().position(padreHurtado).title
                                        (a.getTipo())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_alarmarobo_fg)).rotation(5));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));

                                break;

                        }
                    }else{
                        databaseReference.child("Alarma").child(a.getUid()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    } // Metodo que crea los marcadores de los hogares del vecindario
}