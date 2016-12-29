package kh.edu.rupp.ckcc.eclass.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
 * Created by leapkh on 29/12/16.
 */

public class CkccMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, View.OnClickListener, LocationListener {

    private final double CKCC_LATITUDE = 11.568930;
    private final double CKCC_LONGTITUDE = 104.888426;

    private GoogleMap map;
    private MapView mapView;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ckcc_map);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addConnectionCallbacks(this);
        builder.addApi(LocationServices.API);
        googleApiClient = builder.build();
        googleApiClient.connect();

        findViewById(R.id.btn_my_location).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
        googleApiClient.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        addCkccMarker();
    }

    private void addCkccMarker() {
        LatLng ckccPosition = new LatLng(CKCC_LATITUDE, CKCC_LONGTITUDE);
        addMarker("CKCC", "Mobile App Development Class", ckccPosition);
    }

    private void addMyLocationMarker(Location currentLocation){
        addMarker("My Location", "", new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        float[] results = new float[5];
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), CKCC_LATITUDE, CKCC_LONGTITUDE, results);
        for(float result:results){
            Log.d("ckcc", "Result: " + result);
        }
    }

    private void addMarker(String title, String text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(title);
        markerOptions.snippet(text);
        markerOptions.position(position);
        map.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_my_location) {
            if (googleApiClient.isConnected()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //request permission
                    return;
                }
                Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (currentLocation == null) {
                    Log.d("ckcc", "Current location not found");
                    requestLocationUpdate();
                    return;
                }
                addMyLocationMarker(currentLocation);
                requestLocationUpdate();
            } else {
                Toast.makeText(this, "The map is still in progress. Please try again later.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestLocationUpdate() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //// TODO: 29/12/16 request for permission
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("ckcc", "onLocationChanged");
        addMyLocationMarker(location);
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
}
