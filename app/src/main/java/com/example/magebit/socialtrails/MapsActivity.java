package com.example.magebit.socialtrails;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.magebit.socialtrails.R.id.map;
import static com.example.magebit.socialtrails.R.id.wide;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static GoogleMap mMap;
    LocationManager locationManager;
    ArrayList markerPoints = new ArrayList();
    TextView _outputRoute;
    EditText _inputRoute;
    static DBHandler dbHandler;
    static Double startMarkerLat;
    static Double startMarkerLng;
    static Double finishMarkerLat;
    static Double finishMarkerLng;
    static int currentD, currentM, currentY;
    static boolean isRoute;
    static String filterTagId = "";
    boolean isPlacing;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        final Calendar cal = Calendar.getInstance();
        currentY = cal.get(Calendar.YEAR);
        currentM = cal.get(Calendar.MONTH) + 1;
        currentD = cal.get(Calendar.DAY_OF_MONTH);
        dbHandler = new DBHandler(this, null, null, 1);


    }


    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        printDatabase();
        FiletMenuActivity.filterParameter = 0;

        //Camera move for demostraction , Note to self: remove when location request is fixed
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(56.9496, 24.1052)).zoom(10).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //Set current location marker
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Check if network is enabled
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        //get current latitude
                        double latitude = location.getLatitude();
                        //get current longitude
                        double longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            String str = addressList.get(0).getLocality() + ",";
                            str += addressList.get(0).getCountryName();
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(str)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.current))
                            );
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
            //Check if GPS is enabled
            else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        //get current latitude
                        double latitude = location.getLatitude();
                        //get current longitude
                        double longitude = location.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                            String str = addressList.get(0).getLocality() + ",";
                            str += addressList.get(0).getCountryName();
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(str)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.current))
                            );
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        }
        // Placing route marker functionality
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (isPlacing) {

                    //Clearing markers
                    if (markerPoints.size() > 1) {
                        markerPoints.clear();
                        // mMap.clear();
                    }

                    markerPoints.add(latLng);

                    MarkerOptions options = new MarkerOptions();

                    options.position(latLng);

                    if (markerPoints.size() == 1) {
                        startMarkerLat = latLng.latitude;
                        startMarkerLng = latLng.longitude;
                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
                    } else if (markerPoints.size() == 2) {
                        finishMarkerLat = latLng.latitude;
                        finishMarkerLng = latLng.longitude;
                        isRoute = true;
                        Button button = (Button) findViewById(R.id.addMenu);
                        button.setVisibility(View.VISIBLE);


                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
                    }
                    // Add the new marker
                    mMap.addMarker(options);

                    if (markerPoints.size() >= 2) {
                        //Get first click location, starting point
                        LatLng origin = (LatLng) markerPoints.get(0);
                        LatLng dest = (LatLng) markerPoints.get(1);

                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        downloadTask.execute(url);
                    }
                }
            }
        });

        //Adding zoom out/in functionality
        mMap.getUiSettings().setZoomControlsEnabled(true);


        //Adding show current location functionality
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }


    }

/*    //Add route to database
    public void _addRoute(View view) {
        if (isRoute) {
            Route route = new Route(0, _inputRoute.getText().toString(), startMarkerLat, startMarkerLng, finishMarkerLat, finishMarkerLng, null, 0, 0, 0, 0, 0);
            dbHandler.addRoute(route);
            _createTrail(view);
            printDatabase();

        }
    }*/

    //Enter/Cancel create a trail mode
    public void _createTrail(View view) {
        Button createTrailButton = (Button) findViewById(R.id.createTrailButton);
        if (isPlacing) {
            isPlacing = false;
            createTrailButton.setText("Create Trail");
            if (markerPoints.size() > 1) {
                markerPoints.clear();
             /*   mMap.clear();*/
            }

        } else {
            isPlacing = true;
            createTrailButton.setText("Cancel");
        }

    }


    //Outputs all routes on map
    public void printDatabase() {
        String dbString = dbHandler.databaseToString();
        int parameter = FiletMenuActivity.filterParameter;
        Object[] list;
        if(filterTagId == "") {
            list = dbHandler.getRoute(parameter);
        }
        else {
            list = dbHandler.getTagRoutes(filterTagId);
        }
        for (Object route : list) {
            LatLng origin = new LatLng(((Route) route).get_startLat(), ((Route) route).get_startLng());
            LatLng dest = new LatLng(((Route) route).get_finishLat(), ((Route) route).get_finishLng());
            String url = getDirectionsUrl(origin, dest);
            MarkerOptions start = new MarkerOptions();
            MarkerOptions finish = new MarkerOptions();

            start.position(origin);
            finish.position(dest);
            start.icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
            start.title(((Route) route).get_name()+ " start " +
                    ((Route) route).getDay_x()+ "/" +
                    ((Route) route).getMonth_x() + " " +
                    ((Route) route).getHour_x() + ":" +
                    ((Route) route).getMinute_x()
            );
            finish.title(((Route) route).get_name()+ " finish");
            finish.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
            mMap.addMarker(start);
            mMap.addMarker(finish);
            DownloadTask downloadTask = new DownloadTask();

            downloadTask.execute(url);
        }
        filterTagId = "";
    }
    //Velo automašīna
    //Datu modelis, d
    //Crud
    //Distance
    //Laiks ceļā
    // Move to add menu activity
    public void addMenu(View v) {
        startActivity(new Intent(MapsActivity.this, AddRouteActivity.class));
    }

    //Move to Route list activity
    public void RouteListView(View v) {
        startActivity(new Intent(MapsActivity.this, RouteListActivity.class));
    }
    public void TagListView(View v) {
        startActivity(new Intent(MapsActivity.this, TagListActivity.class));
    }

    //Move to filter menu activity
    public void filterMenu(View v) {
        startActivity(new Intent(MapsActivity.this, FiletMenuActivity.class));
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {


        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    //Adding custom markers from start to finish
                /*    if (j == 0 || j == (path.size() - 1)) {
                        MarkerOptions options = new MarkerOptions();
                        options.position(position);
                        if (j == 0) {
                            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
                        } else {
                            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
                        }
                        mMap.addMarker(options);
                    }*/
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);

                HashMap<String, String> point = path.get(i);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

            }

            // Drawing polyline(route)
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

