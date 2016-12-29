package kh.edu.rupp.ckcc.eclass.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import kh.edu.rupp.ckcc.eclass.R;

/**
 * eClass
 * Created by leapkh on 27/12/16.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private final int PERMISSION_REQUEST_CODE = 1;

    private GoogleApiClient googleApiClient;
    private GoogleMap map;
    private MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ckcc", "MapActivity onCreate");
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        findViewById(R.id.btn_ckcc).setOnClickListener(this);
        findViewById(R.id.btn_current_location).setOnClickListener(this);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addConnectionCallbacks(this);
        builder.addApi(LocationServices.API);
        googleApiClient = builder.build();

        Log.d("ckcc", "Distanct");
        float[] result = new float[5];
        Location.distanceBetween(11.568846, 104.888415, 11.5696, 104.921, result);
        Log.d("ckcc", "D: " + result[0]);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mapView.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mapView.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("ckcc", "onMapReady");
        map = googleMap;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ckcc) {
            addCkccMarker();
        } else if (v.getId() == R.id.btn_current_location) {
            addCurrentLocationMarker();
        }
    }

    private void addCkccMarker() {
        if (map != null) {
            LatLng ckccLatLng = new LatLng(11.569151, 104.888453);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("CKCC");
            markerOptions.snippet("Mobile App Development");
            markerOptions.position(ckccLatLng);
            map.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ckccLatLng, 15);
            map.animateCamera(cameraUpdate);
        }
    }

    private void addCurrentLocationMarker() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (map != null && location != null) {
            LatLng ckccLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("CKCC");
            markerOptions.snippet("Mobile App Development");
            markerOptions.position(ckccLatLng);
            map.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ckccLatLng, 15);
            map.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                addCurrentLocationMarker();
            }else {
                Toast.makeText(this, "Cannot get current location since you denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
