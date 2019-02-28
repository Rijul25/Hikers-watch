package com.example.dell.hikerswatch2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivityy extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            StartListening();

        }
    }

    public void StartListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }
    }

    public void UpdateLocation(Location location) {

        Log.e("MAIN","inside update location");
        TextView LatTextView = findViewById(R.id.LatTextView);
        TextView LonTextView = findViewById(R.id.LongTextView);
        TextView AltTextView = findViewById(R.id.AltitudeTextView);
        TextView AccTextView = findViewById(R.id.AccuracyTextView);
        if (location != null) {
            LatTextView.setText("Latitude : " + location.getLatitude());
            LonTextView.setText("Longitude : " + location.getLongitude());
            AltTextView.setText("Altitude : " + location.getAltitude());
            AccTextView.setText("Accuracy: " + location.getAccuracy());
        }

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String Adress = "Could not find Adress";
            if (location != null) {


                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                Log.e("MAIN","here2");

                if (addressList != null && addressList.size() > 0) {
                    Log.e("MAIN","address is not null"+addressList.get(0).toString());
                    Adress = " Adress :";

                    if (addressList.get(0).getSubThoroughfare() != null) {
                        Adress += addressList.get(0).getSubThoroughfare() + " ";
                    }
                    if (addressList.get(0).getThoroughfare() != null) {
                        Adress += addressList.get(0).getThoroughfare() + "\n";
                    }
                    if (addressList.get(0).getLocality() != null) {
                        Adress += addressList.get(0).getLocality() + "\n";
                    }
                    if (addressList.get(0).getPostalCode() != null) {
                        Adress += addressList.get(0).getPostalCode() + "\n";
                    }
                    if (addressList.get(0).getCountryName() != null) {
                        Adress += addressList.get(0).getCountryName() + "\n";
                    }


                } else {
                    Log.e("MAIN","empty address");
                }
                TextView AdRessTextView = findViewById(R.id.AddressTextView);
                AdRessTextView.setText(Adress);
            } else {
                Log.e("MAIN","location is null");
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MAIN",e.toString());
        }
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityy);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                UpdateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            StartListening();

        } else {
            Log.e("MAIN","check self permission");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                Log.e("MAIN","granted");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10000, locationListener);
                Log.e("MAIN",String.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
                boolean okay=false;
                Location location;
                for(String p:locationManager.getProviders(true)) {
                    location = locationManager.getLastKnownLocation(p);
                    if(location!=null) {
                        okay=true;
                        UpdateLocation(location);
                        break;
                    }
                }
                if(!okay) {
                    Log.e("MAIN","All providers are disabled or not working!");
                }


            }
        }
    }
}

