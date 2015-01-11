package capstone.learn;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;



public class MapsActivity extends FragmentActivity {

   // TextView testViewStatus, textViewLatitude, textViewLongitude;
    private GoogleMap mMap;
    String value;


    LocationManager myLocationManager;
    String PROVIDER = LocationManager.GPS_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        value = intent.getStringExtra("key");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        Toast.makeText(this, value, Toast.LENGTH_LONG).show();

       // testViewStatus = (TextView)findViewById(R.id.status);
        //textViewLatitude = (TextView)findViewById(R.id.latitude);
        //textViewLongitude = (TextView)findViewById(R.id.longitude);
        try {
            geoLocate(value);
        } catch (IOException e) {
            e.printStackTrace();
        }

        myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //get last known location, if available
        showMyLocation();

        myLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 3000, 10, myLocationListener);
    }


    public void geoLocate (String value) throws IOException {
        Geocoder gc = new Geocoder(this);

        if(gc.isPresent()) {
            List<Address> list = null;
            try {
                list = gc.getFromLocationName(value, 10);

            } catch (IOException e) {
                e.printStackTrace();
            }


            if(list!=null){

            Address address = list.get(0);


            String localit = address.getLocality();
            double lat = address.getLatitude();
            double lng = address.getLongitude();
            Toast.makeText(this, localit, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, lat, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, lng, Toast.LENGTH_SHORT).show();

            //goTo(lat, lng, 15);
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Destination"));}

        }
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

    private void setUpMap() {

         /* try {
              geoLocate(value);
          } catch (IOException e) {
              e.printStackTrace();
    }      */
    }
        @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        myLocationManager.removeUpdates(myLocationListener);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        myLocationManager.requestLocationUpdates(
                PROVIDER,     //provider
                0,       //minTime
                0,       //minDistance
                myLocationListener); //LocationListener
    }

    private void showMyLocation(){

        myLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        Location location = myLocationManager.getLastKnownLocation(PROVIDER);

        if(location == null){

            myLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            Toast.makeText(this, "My location is null ", Toast.LENGTH_SHORT).show()  ;

        }else{
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Destination"));
        }

    }

    private LocationListener myLocationListener
            = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            //   showMyLocation();

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
            //   showMyLocation();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            //myLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);

        }
    };

        protected void createLocationRequest() {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }


}