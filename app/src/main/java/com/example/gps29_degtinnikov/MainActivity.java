package com.example.gps29_degtinnikov;

import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity {

    LocationManager _LocationManager;
    TextView textAddress;
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("6140d8d1-12ca-426b-b76e-65913c54854c");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapview);
        textAddress = findViewById(R.id.edittext);
        _LocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }
    LocationListener _locationListnaet = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                mapView.getMap().move(
                        new CameraPosition(
                                new Point(location.getLatitude(), location.getLongitude()), 15,  0,  0));

                mapView.getMap().getMapObjects().clear();
                mapView.getMap().getMapObjects().addPlacemark(
                        new Point(location.getLatitude(), location.getLongitude()),
                        ImageProvider.fromResource(MainActivity.this, R.drawable.location)
            );
                GetAddressByGPS getAddressByGPS = new GetAddressByGPS(
                        String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getLatitude()),
                        textAddress
            );
                getAddressByGPS.execute();
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    1);
        }
        _LocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                 1000,
               10,
                _locationListnaet);
        _LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                 10,
                _locationListnaet);
        mapView.onStart();
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
}