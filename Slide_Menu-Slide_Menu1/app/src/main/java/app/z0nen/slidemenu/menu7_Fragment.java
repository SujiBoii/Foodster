package app.z0nen.slidemenu;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.Object;
import java.util.HashMap;
import java.util.Map;


import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Z0NEN on 10/22/2014.
 */
public class menu7_Fragment extends Fragment implements OnMapReadyCallback {
    View rootview;


    public GoogleMap mMap;
    MapView mMapView;
    View mView;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;

    private FirebaseAuth firebaseAuth;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.menu7_layout, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            getActivity().finish();
            // startActivity(new Intent(getActivity(), menu5_Fragment.class));
        }

        return mView;
    }

   @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

       // CameraPosition ort = CameraPosition.builder().target(new LatLng(51.4556432, 7.0115552)).zoom(16).bearing(0).tilt(45).build();

       // googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(ort));


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);



            Intent intent = new Intent(getActivity(), GPSTrackerActivity.class);

            startActivityForResult(intent,1);



        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        mMap.setMyLocationEnabled(true);

                    }


                    }else {
                    Toast.makeText(getActivity(), "Diese App funktioniert nicht ohne Gps", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    break;
                }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Bundle extras = data.getExtras();
            Double longitude = extras.getDouble("Longitude");
            Double latitude = extras.getDouble("Latitude");


            FirebaseUser userr = firebaseAuth.getCurrentUser();


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(userr.getUid());;


            GeoFire geoFire = new GeoFire(ref);

            geoFire.setLocation("location", new GeoLocation(longitude, latitude));

        }
    }










}