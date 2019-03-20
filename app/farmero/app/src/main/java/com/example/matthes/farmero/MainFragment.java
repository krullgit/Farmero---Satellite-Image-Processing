package com.example.matthes.farmero;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;


import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
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




import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;


/**
 * The class:
 * - is the main tab of the app containing the overview of the fields like yield, loss, map with risk areas, satellite image exploring, disease gallery ...
 */

public class MainFragment extends Fragment {

    // properties
    FloatingActionButton settings;
    ImageView field;
    LinearLayout buttonYield;
    LinearLayout buttonLoss;
    LinearLayout buttonMildrew;
    ImageView buttonnvdi;
    ImageView buttonrgb;
    private static final List<List<Point>> POINTS = new ArrayList<>();
    private static final List<Point> OUTER_POINTS = new ArrayList<>();
    FloatingActionButton polygone;
    private static String pointsFile = "/storage/emulated/0/Download/points.txt";
    private SeekBar seekBar;
    ImageView imagesliderfirst;
    ImageView imageslidersecond;

    // polygone for map

    // required constructor
    public MainFragment() {
        // Required empty public constructor
    }

    // every fragment has this
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // initialize parameters
        settings = view.findViewById(R.id.nav_settings2);
        field = view.findViewById(R.id.field);
        polygone = view.findViewById(R.id.floatingActionButton);
        buttonYield = view.findViewById(R.id.buttonYield);
        buttonLoss = view.findViewById(R.id.buttonLoss);
        buttonMildrew = view.findViewById(R.id.buttonMildrew);
        buttonnvdi = view.findViewById(R.id.btn_nvdi);
        buttonrgb = view.findViewById(R.id.btn_rgb);
        imagesliderfirst = view.findViewById(R.id.image_slider_first);
        imageslidersecond = view.findViewById(R.id.image_slider_last);

        // Listener
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        polygone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), polygonActivity.class);
                startActivity(intent);
            }
        });

        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FieldBigActivity.class);
                startActivity(intent);
            }
        });
        buttonYield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), popYieldActivity.class);
                startActivity(intent);
            }
        });
        buttonLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), popLossActivity.class);
                startActivity(intent);
            }
        });
        buttonrgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesliderfirst.setImageResource(R.drawable.rgb_day1);
                imageslidersecond.setImageResource(R.drawable.rgb_day2);
            }
        });
        buttonnvdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesliderfirst.setImageResource(R.drawable.nvdiday1_color);
                imageslidersecond.setImageResource(R.drawable.nvdiday2_color);
            }
        });
        buttonMildrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), popMildewActivity.class);
                startActivity(intent);
            }
        });





        // showing the slider for satellite images
        seekBar = (SeekBar) view.findViewById(R.id.seekBar1);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                FrameLayout target = (FrameLayout) view.findViewById(R.id.target);

                progress = progresValue;

                ViewGroup.LayoutParams lp = target.getLayoutParams();
                lp.height = progress;
                target.setLayoutParams(lp);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });



        // Create Mapbox
        SupportMapFragment mapFragment;
        if (savedInstanceState == null) {

            // Create fragment
            final FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Build mapboxMap
            MapboxMapOptions options = new MapboxMapOptions();
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(51.575037, 12.946546
                    ))
                    .zoom(13)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

            // Add map fragment to parent container
            transaction.add(R.id.sub_frame, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getFragmentManager().findFragmentByTag("com.mapbox.map");
        }



        // Create Mapbox Callback
        mapFragment.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {



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


                // Add marker
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
                            .icon(IconFactory.getInstance(getActivity()).fromResource(R.drawable.yellowpoint_marker))
                    );
                }





                mapboxMap.setStyle(Style.SATELLITE, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
//                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
//
//                        // ADD the Polygon
//                        style.addSource(new GeoJsonSource("source-id", Polygon.fromLngLats(POINTS)));
//                        style.addLayer(new FillLayer("layer-id", "source-id").withProperties(
//                                fillColor(Color.parseColor("#3CFFEA00")))
//                        );

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
        });





        return view;
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

    public String readSavedData ( ) {
        StringBuffer datax = new StringBuffer("");
        try {
            FileInputStream fIn = getActivity().openFileInput ( "fields_field_1" );
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

