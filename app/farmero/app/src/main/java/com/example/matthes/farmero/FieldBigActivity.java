package com.example.matthes.farmero;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleStrokeWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


/**
 * The class:
 * - loads a mapbox map
 * - loads the risk areas from local storage and shows them on the map
 * - loads the te field polygon from the local storage and shows it on the map
 * - shows the current user location on the map
 */

public class FieldBigActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;


    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url = "http://10.0.2.2:3000/";
    private static String pointsFile = "/storage/emulated/0/Download/points.txt";


    private static final List<List<Point>> POINTS = new ArrayList<>();
    private static final List<Point> OUTER_POINTS = new ArrayList<>();

/*    static {
        OUTER_POINTS.add(Point.fromLngLat(12.941725850371768, 51.57971352400993));
        OUTER_POINTS.add(Point.fromLngLat(12.942047715453555, 51.57588640768779));
        OUTER_POINTS.add(Point.fromLngLat(12.940846085814883, 51.571032040481654));
        OUTER_POINTS.add(Point.fromLngLat(12.943442464141299, 51.57112539858611));
        POINTS.add(OUTER_POINTS);
    }*/

    //const coord = '[[12.941725850371768,51.57971352400993],[12.942047715453555,51.57588640768779],[12.940846085814883,51.571032040481654],[12.943442464141299,51.57112539858611],[12.944708466796328,51.570671943139885],[12.948763966826846,51.57088533450155],[12.949965596465518,51.57121875649885],[12.951789498595645,51.570325180040776],[12.954450249938418,51.57021848317042],[12.954471707610537,51.571432145293755],[12.946274876861025,51.580113552408065],[12.941725850371768,51.57971352400993]]'


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, "pk.eyJ1IjoiZmFybWVybyIsImEiOiJjanJ6Zmd5cGcxODFzNDNsdnB0dW16bXh6In0.F6w_diZUKuqmcHGc1QNOGw");

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_location_component);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private String readFromFile(String fileName) {
        InputStream in = null;
        try {
            in = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return String.valueOf(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        FieldBigActivity.this.mapboxMap = mapboxMap;


        String coordString = readSavedData();
        Log.d("coordString: ", ""+coordString);
        // Open File
        String coordStringList[] = coordString.split("#");

        // Create List with coordinates
        Log.d("coordString.length: ",""+coordString.length());
        Log.d("coordString.length: ",""+coordStringList.toString());
        int i;
        for(i=0; i< coordStringList.length; i++){
            double lon;
            double lat;
            try {
                lon = Double.parseDouble(coordStringList[i].split(",")[0]);
                lat = Double.parseDouble(coordStringList[i].split(",")[1]);
            }
            catch (NumberFormatException e)
            {
                lon = 0;
                lat = 0;
            }
            //mapboxMap.addMarker(new MarkerOptions()
            //        .position(new LatLng(lat, lon))
            //        .title("Eiffel Tower"));
            OUTER_POINTS.add(Point.fromLngLat(lon, lat));
        }
        POINTS.add(OUTER_POINTS);


        // Ass marker
        String points = readFromFile(pointsFile);
        String pointsList[] = points.split("#");
        int j;
        for(j=0; j< pointsList.length; j++){
            double lon;
            double lat;
            try {
                lon = Double.parseDouble(pointsList[j].split(",")[0]);
                lat = Double.parseDouble(pointsList[j].split(",")[1]);
            }
            catch (NumberFormatException e)
            {
                lon = 0;
                lat = 0;
            }
            //mapboxMap.addMarker(new MarkerOptions()
            //        .position(new LatLng(lat, lon))
            //        .title("Eiffel Tower"));

            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat,lon))
                    .icon(IconFactory.getInstance(this).fromResource(R.drawable.yellowpoint_marker))
            );
        }


        mapboxMap.setStyle(Style.SATELLITE_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        style.addSource(new GeoJsonSource("source-id", Polygon.fromLngLats(POINTS)));

                        Log.d("OUTER_POINTS: ",""+OUTER_POINTS);
                        style.addSource(new GeoJsonSource("line-source",
                                FeatureCollection.fromFeatures(new Feature[] {Feature.fromGeometry(
                                        LineString.fromLngLats(OUTER_POINTS)
                                )})));

                        style.addLayer(new FillLayer("layer-id", "source-id").withProperties(
                                fillColor(Color.parseColor("#22ffc20c")))
                        );

                        style.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                                PropertyFactory.lineDasharray(new Float[] {0.01f, 2f}),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                PropertyFactory.lineWidth(2.3f),
                                PropertyFactory.lineColor(Color.parseColor("#ffc20c"))
                        ));
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(this, loadedMapStyle);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);

                }

            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }



    @Override
    @SuppressWarnings( {"MissingPermission"})
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public String readSavedData ( ) {
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = openFileInput ( "fields_field_1" );
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ){
                datax.append(readString);
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
        } catch ( IOException ioe ) {
            ioe.printStackTrace ( ) ;
        }
        return datax.toString() ;

    }
}