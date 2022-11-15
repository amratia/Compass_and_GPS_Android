package com.example.gps_and_compass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    double longitude = 0;
    double latitude = 0;
    LocationManager mgr;
    MyLocationListener listener;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final int _requestCode = 0;
    // device sensor manager
    private SensorManager SensorManage;
    private LocationManager LocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize your android device sensor capabilities
        SensorManage = (SensorManager) getSystemService(SENSOR_SERVICE);
        LocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            ActivityCompat.requestPermissions(MainActivity.this,permissions,_requestCode);
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }else{
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }

        TextView tv1 = (TextView)findViewById(R.id.GPStextView);
        tv1.setText("Longitude: "+longitude+"\n"+"Latitude: "+latitude);
    }
//-----------------------------------------------------------------------------------------------------
    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        SensorManage.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // code for system's orientation sensor registered listeners
        SensorManage.registerListener(this, SensorManage.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // get angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        TextView tv1 = (TextView)findViewById(R.id.CompasstextView);
        tv1.setText("Degree "+degree);

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
    //----------------------------------------------------------------------------------------------
    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location){
            //mgr.removeUpdates(listener);
            TextView tv1 = (TextView)findViewById(R.id.GPStextView);
            tv1.setText("Longitude:"+location.getLongitude()+"\n"+"Latitude:"+location.getLatitude());
            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}
        @Override
        public void onProviderEnabled(String provider){}
        @Override
        public void onProviderDisabled(String provider){}
        public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
            switch(requestCode){
                case _requestCode:
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,permissions,_requestCode);

                    }else{}break;
            }
        }
    }
}
// checking second commit