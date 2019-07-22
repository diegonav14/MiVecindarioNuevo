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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MapsActivity extends FragmentActivity implements  OnMapReadyCallback {

    GoogleMap mMap;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Usuario> listaUsuario = new ArrayList<Usuario>();
    ArrayAdapter<Usuario> arrayAdapterUsuario;

    Button btnAlarma;

    ListView lv_Miembros;

    ImageView iv_seguridad, iv_bomberos, iv_mascotas, iv_ambulancia, iv_auto, iv_imagenMarcador;

    String nmUsuario, apUsuario;

    TextView txt_nombreMarcador, txt_direccionMarcador, txt_comentarioMarcador, txt_marcadorAlarma, txt_marcadorSuceso, txt_marcadorUsuario;


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
                View alerta = alarma_alerta.inflate(R.layout.alerta_alarmas, null);
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
                        Intent intent = new Intent(MapsActivity.this, ingresarAlarma.class);
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
                        Intent intent = new Intent(MapsActivity.this, ingresarAlarma.class);
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
                        Intent intent = new Intent(MapsActivity.this, ingresarAlarma.class);
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
                        Intent intent = new Intent(MapsActivity.this, ingresarAlarma.class);
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
                        Intent intent = new Intent(MapsActivity.this, ingresarAlarma.class);
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

    private void estadoInternet() {
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
        nmUsuario = preferencias.getString("nombreUsuario", "NoSesion");
        apUsuario = preferencias.getString("apellidoUsuario", "NoSesion");
    } // Carga la sesion del usuario

    public void ingresarEvento(View v) {
        Intent intent = new Intent(this, ingresarEvento.class);
        startActivity(intent);

    } // Inicia la vista (Activity) Ingresar evento

    public void misDatos(View v) {
        Intent intent = new Intent(this, datosUsuario.class);
        startActivity(intent);

    } // Inicia la vista (Activity) Datos usuario

    public void miHogar(View v) {
        Intent intent = new Intent(this, editarHogar.class);
        startActivity(intent);

    } // Inicia la vista (Activity) Editar hogar

    public void cerrarSesion(View v) {
        SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferecias.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, iniciarSesion.class);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMinZoomPreference(17.0f);
        agregarMarcadores(googleMap);
        agregarAlarmas(googleMap);

        // Add a marker in Sydney and move the camera
    } // Metodo propio de la clase GoogleMap que se encarga de los metodos del mapa


    public void agregarMarcadores(GoogleMap googleMap) {

        mMap = googleMap;

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    if (u.getNombre().equals(nmUsuario) && u.getApellido().equals(apUsuario)) {
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
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for (final DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Hogar h = objSnapshot.getValue(Hogar.class);

                    if (usuarioActual.getHogar().getVecindario().getNombre().equals(h.getVecindario().getNombre())) {

                        double latitud = Double.parseDouble(h.getLatitud());
                        double longitud = Double.parseDouble(h.getLongitud());

                        LatLng padreHurtado = new LatLng(latitud, longitud);

                        mMap.addMarker(new MarkerOptions().position(padreHurtado)
                                .title(h.getNombre())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_casa_round)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));

                        marcadoresCasa();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    } // Metodo que crea los marcadores de los hogares del vecindario

    public void agregarAlarmas(GoogleMap googleMap) {

        mMap = googleMap;

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    if (u.getNombre().equals(nmUsuario) && u.getApellido().equals(apUsuario)) {
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
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String fecha = df.format(c);

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Alarma a = objSnapshot.getValue(Alarma.class);

                    if (a.getFecha().equals(fecha)) {

                        String tipoAlarma = a.getTipo();

                        double latitud = Double.parseDouble(a.getHogar().getLatitud());
                        double longitud = Double.parseDouble(a.getHogar().getLongitud());

                        LatLng padreHurtado = new LatLng(latitud, longitud);

                         Marker marcador = mMap.addMarker(new MarkerOptions().position(padreHurtado).title(a.getUid()));
                         mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));

                        switch (tipoAlarma) {
                            case "Vehiculo":

                                marcador.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_auto_round));
                                marcador.setAnchor(1,2);

                                break;

                            case "Ambulancia":

                                marcador.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ambulancia_round));
                                marcador.setAnchor(1,2);

                            break;

                            case "Bomberos":

                                marcador.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bomberos_round));
                                marcador.setAnchor(1,2);

                                break;

                            case "Mascota":

                                marcador.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mascota_round));
                                marcador.setAnchor(1,2);

                                break;

                            case "Seguridad":

                                marcador.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_seguridad_round));
                                marcador.setAnchor(1,2);

                                break;

                        }

                        marcadoresAlarma();

                    } else if (a.getFecha() != fecha) {
                        databaseReference.child("Alarma").child(a.getUid()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Metodo que crea los marcadores de los hogares del vecindario

    public void marcadoresAlarma() {

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker markerAlarma) {

                databaseReference.child("Alarma").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        AlertDialog.Builder builderAlarma = new AlertDialog.Builder(MapsActivity.this);
                        LayoutInflater alertaMarcadorAlarma = LayoutInflater.from(MapsActivity.this);
                        View alertaAlarma = alertaMarcadorAlarma.inflate(R.layout.alerta_marcador_alarma, null);
                        builderAlarma.setView(alertaAlarma);

                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            Alarma a = objSnapshot.getValue(Alarma.class);

                            if (markerAlarma.getTitle().equals(a.getUid())) {

                                txt_marcadorAlarma = alertaAlarma.findViewById(R.id.txt_marcadorAlarma);
                                txt_marcadorSuceso = alertaAlarma.findViewById(R.id.txt_marcadorSuceso);
                                txt_marcadorUsuario = alertaAlarma.findViewById(R.id.txt_marcadorUsuario);
                                iv_imagenMarcador = alertaAlarma.findViewById(R.id.iv_marcadorImagen);

                                txt_marcadorAlarma.setText(a.getTipo());
                                txt_marcadorSuceso.setText(a.getSuceso());
                                txt_marcadorUsuario.setText(a.getUsuario().getNombre() + " " + a.getUsuario().getApellido());

                                switch (a.getTipo()) {
                                    case "Vehiculo":
                                        iv_imagenMarcador.setBackground(getApplication().getResources().getDrawable(R.drawable.auto));
                                        break;

                                    case "Ambulancia":
                                        iv_imagenMarcador.setBackground(getApplication().getResources().getDrawable(R.drawable.ambulancia));
                                        break;

                                    case "Bomberos":
                                        iv_imagenMarcador.setBackground(getApplication().getResources().getDrawable(R.drawable.bomberos));
                                        break;

                                    case "Mascota":
                                        iv_imagenMarcador.setBackground(getApplication().getResources().getDrawable(R.drawable.mascota));
                                        break;

                                    case "Seguridad":
                                        iv_imagenMarcador.setBackground(getApplication().getResources().getDrawable(R.drawable.alarma_robo));
                                        break;

                                }

                                builderAlarma.setTitle(a.getTipo());
                                builderAlarma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                                markerAlarma.remove();
                                builderAlarma.show();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return false;
            }

        });

    }

    public void marcadoresCasa() {

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker markerCasa) {

                databaseReference.child("Alarma").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        AlertDialog.Builder builderCasa = new AlertDialog.Builder(MapsActivity.this);
                        LayoutInflater alertaMarcador = LayoutInflater.from(MapsActivity.this);
                        View alertaCasa = alertaMarcador.inflate(R.layout.alerta_marcador, null);
                        builderCasa.setView(alertaCasa);

                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            final Hogar h = objSnapshot.getValue(Hogar.class);

                            if (markerCasa.getTitle().equals(h.getNombre())) {

                                lv_Miembros = alertaCasa.findViewById(R.id.lv_marcadorMiembros);
                                txt_nombreMarcador = alertaCasa.findViewById(R.id.txt_marcadorNombre);
                                txt_comentarioMarcador = alertaCasa.findViewById(R.id.txt_marcadorComentario);
                                txt_direccionMarcador = alertaCasa.findViewById(R.id.txt_marcadorDireccion);

                                txt_direccionMarcador.setText(h.getDireccion());
                                txt_comentarioMarcador.setText(h.getComentario());
                                txt_nombreMarcador.setText(h.getNombre());

                                databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        listaUsuario.clear();
                                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                                            Usuario u = objSnapshot.getValue(Usuario.class);
                                            if (u.getHogar().getNombre().equals(h.getNombre()))
                                                listaUsuario.add(u);
                                            arrayAdapterUsuario = new ArrayAdapter<Usuario>
                                                    (MapsActivity.this,
                                                            android.R.layout.simple_list_item_1, listaUsuario);
                                            lv_Miembros.setAdapter(arrayAdapterUsuario);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }



                                });

                                builderCasa.setTitle(h.getNombre());
                                builderCasa.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                            }
                            builderCasa.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return false;
            }

        });
    }
}