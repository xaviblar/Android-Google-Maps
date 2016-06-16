package com.example.xavier.googlemaps_example;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Button limpiar_btn;
    Button dibujar_btn;
    Spinner tipo_mapa_spn;
    ArrayAdapter adap;
    CheckBox unq_marker_chk;
    ArrayList<LatLng> markers_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        //Agregar Click Listener
        addClickListener();
        //Agregar Long Click Listener
        addClickLongListener();
        markers_array = new ArrayList<>();
        limpiar_btn = (Button) findViewById(R.id.btn_limpiar);
        limpiar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                markers_array.clear();
            }
        });
        tipo_mapa_spn = (Spinner) findViewById(R.id.spn_mapa);
        String[] arr = getResources().getStringArray(R.array.tipos_mapa);
        adap = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        tipo_mapa_spn.setAdapter(adap);
        tipo_mapa_spn.setSelection(1);
        tipo_mapa_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (tipo_mapa_spn.getSelectedItem().toString()) {
                    case "Normal":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case "Hibrido":
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case "Satelital":
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case "Terreno":
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case "Sin Capas":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        unq_marker_chk = (CheckBox) findViewById(R.id.unq_mrkr_chk);
        dibujar_btn = (Button) findViewById(R.id.btn_dibujar);
        dibujar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawPoligono();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // El objeto GoogleMap ha sido referenciado correctamente ahora podemos manipular sus propiedades
        // Seteamos el tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //Activamos la capa o layer MyLocation
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),marker.getTitle(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        //Este evento se ejecuta cuando le dan click a la ventana de información del Marker
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getSnippet(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addClickListener(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (unq_marker_chk.isChecked()) {
                    mMap.clear();
                    markers_array.clear();
                }
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Coordenadas:")
                        .snippet("Latitud: " + String.valueOf(latLng.latitude)
                                + " Longitud: " + String.valueOf(latLng.longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                markers_array.add(latLng);
            }
        });
    }
    public void addClickLongListener(){
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (unq_marker_chk.isChecked()) {
                    mMap.clear();
                    markers_array.clear();
                }
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Coordenadas:")
                        .snippet("Latitud: " + String.valueOf(latLng.latitude)
                                + " Longitud: " + String.valueOf(latLng.longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                markers_array.add(latLng);
            }
        });
    }

    private void drawPoligono(){
        PolygonOptions POLIGONO = new PolygonOptions();
        for(LatLng mark : markers_array)
        {
            POLIGONO.add(mark);
        }
        POLIGONO.strokeColor(Color.RED);
        POLIGONO.fillColor(Color.BLUE);
        mMap.addPolygon(POLIGONO);
    }
}

