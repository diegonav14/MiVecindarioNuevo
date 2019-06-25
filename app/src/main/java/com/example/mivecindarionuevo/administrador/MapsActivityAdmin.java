package com.example.mivecindarionuevo.administrador;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.mivecindarionuevo.R;
import com.example.mivecindarionuevo.modelos.Hogar;
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

public class MapsActivityAdmin extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    String uidMapa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_admin);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapadmin);
        mapFragment.getMapAsync(this);
        inicializarFirebase();
        cargarPreferencias();

    }


    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializa la conexion con la base de datos, en este caso Firebase

    private void cargarPreferencias() {
        SharedPreferences mapauid = getSharedPreferences("mapaUid", Context.MODE_PRIVATE);
        uidMapa = mapauid.getString("mapaUid","NoUid");
    } // Carga la sesion del usuario


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
        // Add a marker in Sydney and move the camera
    } // Metodo propio de la clase GoogleMap que se encarga de los metodos del mapa


    public void agregarMarcadores(GoogleMap googleMap){

        mMap = googleMap;


        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Hogar h = objSnapshot.getValue(Hogar.class);
                    if (h.getVecindario().getUid().equals(uidMapa)){
                        double latitud = Double.parseDouble(h.getLatitud());
                        double longitud = Double.parseDouble(h.getLongitud());
                        LatLng padreHurtado = new LatLng(latitud,longitud);
                        mMap.addMarker(new MarkerOptions().position(padreHurtado).title(h.getComentario()+" "+h.getDireccion()+" "+h.getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_casmarket_round)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(padreHurtado));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    } // Metodo que crea los marcadores de los hogares del vecindario

}