package com.example.matthes.farmero;





import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The class:
 * - loads google maps
 * - user can select a polygon and save it locally
 * - polygon gets send to the backend
 * - backend responds with field analyses and app stores them
 */


public class polygonActivity extends AppCompatActivity implements OnMapReadyCallback{

    private FrameLayout frameLayout;

    private GoogleMap googleMap;
    private Polyline polyline = null;
    private Polygon polygon = null;


    private Button buttonPolygon;
    private Button buttonClear;
    private Button save;

    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    // File url to download (static dummy url)
    //private static String file_url = "http://10.0.2.2:3000/?coord=[[12.941725850371768,51.57971352400993],[12.942047715453555,51.57588640768779],[12.940846085814883,51.571032040481654],[12.943442464141299,51.57112539858611],[12.944708466796328,51.570671943139885],[12.948763966826846,51.57088533450155],[12.949965596465518,51.57121875649885],[12.951789498595645,51.570325180040776],[12.954450249938418,51.57021848317042],[12.954471707610537,51.571432145293755],[12.946274876861025,51.580113552408065],[12.941725850371768,51.57971352400993]]";

    ///data/user/0/com.example.matthes.farmero/files
    //File directory = getApplicationContext().getFilesDir();
    //Log.d("currentDir:", directory.toString());

    //private final LatLng beirutLatLng = new LatLng(33.893865, 35.501175);
    private final LatLng beirutLatLng = new LatLng(51.574723, 12.946393);
    //private final MarkerOptions beirutMarker = new MarkerOptions().position(beirutLatLng);

    private List<LatLng> listLatLngs = new ArrayList<>();
    private List<Marker> listMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon);
        // Construct a PlaceDetectionClient.

        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(polygonActivity.this);
        setupUI();
        setButtonPolylineListener();
        setButtonPolygonListener();
        setButtonClearListener();



    }

    private void setupUI(){
        frameLayout = findViewById(R.id.frameLayout);

        buttonPolygon = findViewById(R.id.buttonPolygon);
        buttonClear = findViewById(R.id.buttonReset);
        save = findViewById(R.id.buttonLocation);


    }

    private void setButtonPolylineListener() {

        buttonPolygon.setOnClickListener(e -> connectPolygon());
        buttonClear.setOnClickListener(e -> resetMap());
        save.setOnClickListener(e -> savePolygon());
    }

    private void savePolygon() {
        Iterator<LatLng> crunchifyIterator = listLatLngs.iterator();
        String filename = "fields_field_1";


        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            while (crunchifyIterator.hasNext()) {
                LatLng point = crunchifyIterator.next();
                outputStream.write((point.longitude + "," + point.latitude + "#" + "\n").getBytes());
            }
            outputStream.write((listLatLngs.get(0).longitude + "," + listLatLngs.get(0).latitude + "#" +"\n").getBytes());
            outputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        // create URL String out of the selected Polygone
        //Log.d("file_url: ",file_url);
        //"http://10.0.2.2:3000/?coord=[[12.941725850371768,51.57971352400993],[12.942047715453555,51.57588640768779],[12.940846085814883,51.571032040481654],[12.943442464141299,51.57112539858611],[12.944708466796328,51.570671943139885],[12.948763966826846,51.57088533450155],[12.949965596465518,51.57121875649885],[12.951789498595645,51.570325180040776],[12.954450249938418,51.57021848317042],[12.954471707610537,51.571432145293755],[12.946274876861025,51.580113552408065],[12.941725850371768,51.57971352400993]]";
        String baseUrl = "http://35.204.175.152:3000/?coord=";
        String coords = listLatLngs.toString();
        Log.d("listLatLngs:", coords);
        //[lat/lng: (51.57678616516126,12.94315919280052), lat/lng: (51.56971775253245,12.940498776733875), lat/lng: (51.57139842604919,12.94969640672207)]
        Iterator<LatLng> crunchifyIterator2 = listLatLngs.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (crunchifyIterator2.hasNext()){
            LatLng point = crunchifyIterator2.next();
            sb.append("["+point.longitude + "," + point.latitude + "],");
        }
        sb.replace(sb.length()-1,sb.length(),"]");

        // Communicate with the nodeJS Server and download the files
        try{
            new DownloadFileFromURL().execute(baseUrl + sb.toString()).execute().get();
////                        .setAction("Action", null).show();
            // this is not working
            Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content),
                    "Your Field is set up!", Snackbar.LENGTH_LONG);
            snackBar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        } catch (Exception e) {

        }




    }

    private void setButtonPolygonListener() {
        buttonPolygon.setOnClickListener(e -> connectPolygon());
    }

    private void setButtonClearListener() {
        buttonClear.setOnClickListener(e -> resetMap());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;



        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(beirutLatLng, 15.0f));
        //map view
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        setGoogleMapClickListener();
        setCircleClickListener();
        setPolylineClickListener();
        setPolygonClickListener();
    }



    private void setGoogleMapClickListener(){
        googleMap.setOnMapClickListener(e -> {
            MarkerOptions markerOptions = new MarkerOptions().position(e);
            //markerOptions.draggable(true);
            //markerOptions.alpha(50);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.polygone_marker));
            Marker marker = googleMap.addMarker(markerOptions);
            System.out.println("FA");
            System.out.println(marker.getPosition());
            listMarkers.add(marker);
            listLatLngs.add(e);

            //Log.d("This many points we have: ",""+listLatLngs.size());
        });
    }


    private void setCircleClickListener(){
        googleMap.setOnCircleClickListener(e ->{
            Snackbar.make(frameLayout, "Circle clicked!", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setPolylineClickListener(){
        googleMap.setOnPolylineClickListener(e -> {
            Snackbar.make(frameLayout, "Polyline clicked!", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setPolygonClickListener(){
        googleMap.setOnPolygonClickListener(e -> {
            Snackbar.make(frameLayout, "Polygon clicked!", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void connectPolyline(){
        if (polyline != null)
            polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions().addAll(listLatLngs).clickable(true).color(R.color.colorPrimary);

        polyline = googleMap.addPolyline(polylineOptions);

    }

    private void connectPolygon(){
        if (polygon != null)
            polygon.remove();
        PolygonOptions polygonOptions = new PolygonOptions().addAll(listLatLngs).clickable(true);
        polygon = googleMap.addPolygon(polygonOptions);
        polygon.setStrokeColor(R.color.colorWhite);
    }

    private void resetMap(){
        if (polyline != null) polyline.remove();
        if (polygon != null) polygon.remove();

        for (Marker marker : listMarkers) marker.remove();
        listMarkers.clear();
        listLatLngs.clear();
    }



    ///////////// START / FILE DOWNLOAD ///////////////////


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

    /**
     * Showing Dialog
     * */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background threaddevice
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/Download/points.txt");

                String test = Environment
                        .getExternalStorageDirectory().toString();
                Log.d("CREATION",test);



                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
        }


        ///////////// END / FILE DOWNLOAD ///////////////////



    }
}