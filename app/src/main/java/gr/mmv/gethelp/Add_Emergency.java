package gr.mmv.gethelp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Add_Emergency extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener, OnMapReadyCallback {

    private ImageView imageView;
    private ProgressBar progressBar;
    private EditText userNote;
    private TextView userName, userTel, latTxt, lonTxt;
    private final int PERMISSION_REQ_CODE = 111;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    private GoogleApiClient google_client = null;
    private GoogleApiClient.Builder google_client_builder = null;
    private LocationRequest location_req = null;
    private GoogleMap mMap;

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Requesting permission Storage
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //Requesting permission location
    private void manageLocationPermissions(){
        int hw = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (hw != PackageManager.PERMISSION_GRANTED){
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQ_CODE);
            }
        } else {
            // Permission has already been granted
            location_req = LocationRequest.create();
            location_req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            location_req.setInterval(0);
            location_req.setFastestInterval(0);
            LocationServices.FusedLocationApi.requestLocationUpdates(google_client, location_req, this);
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getApplicationContext(), "Connected to Google Play",Toast.LENGTH_SHORT).show();
        manageLocationPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Error! Connecting to Google Play Services",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location == null) { return; }

        double latNew = location.getLatitude();
        double lonNew = location.getLongitude();
        latTxt.setText(String.valueOf(latNew));
        lonTxt.setText(String.valueOf(lonNew));

        mMap.clear();

        LatLng coordinate = new LatLng(latNew, lonNew);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate,30));

        MarkerOptions marker = new MarkerOptions().position(new LatLng(latNew, lonNew)).title("I'm here");
        mMap.addMarker(marker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == PERMISSION_REQ_CODE && grantResults.length > 0)) {
            for (int i=0; i<grantResults.length; i++)
            {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Location Permission Granted", Toast.LENGTH_SHORT).show();
                    manageLocationPermissions();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Permission Required for this Feature!",Toast.LENGTH_SHORT).show();
                }
            }
        }
        //Checking the request code of our request
        else if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
//ON CREATE******************************************************************************************************ON CREATE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__emergency);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Check User Login or not
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, User_Login.class));
        }

        //connect google play
        int hasgoogleplay = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (hasgoogleplay == ConnectionResult.SUCCESS){
            Toast.makeText(this, "Has Google Play Service", Toast.LENGTH_SHORT).show();
            google_client_builder = new GoogleApiClient.Builder(this);
            google_client_builder = google_client_builder.addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this);
            google_client = google_client_builder.build();
            google_client.connect();
        } else {
            Toast.makeText(this, "Google Play Service Required", Toast.LENGTH_SHORT).show();
        }

        //declare all layout ITEM!
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.imageView);
        userNote = (EditText) findViewById(R.id.userNote) ;
        userName = (TextView)findViewById(R.id.userName);
        userTel = (TextView)findViewById(R.id.userTel);
        latTxt = (TextView)findViewById(R.id.latTxt);
        lonTxt = (TextView)findViewById(R.id.lonTxt);

        //GET USER LOGGED IN FROM SHAREDFREFMANAGER
        User user = SharedPrefManager.getInstance(this).getUser();

        //SET WHICH USER ARE INFORMING
        userName.setText(user.getName());
        userTel.setText(user.getTel());

        //Declare all button on this activity
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Add_Emergency.this, User_firstPage.class));
            }
        });

        findViewById(R.id.informBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informEmer(bitmap);
            }
        }
        );
        findViewById(R.id.chooseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestReadStoragePermission
                requestStoragePermission();
                showFileChooser();
            }
        });
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void informEmer(final Bitmap bitmap) {

        final String note = userNote.getText().toString().trim();
        final String user_report_name = userName.getText().toString().trim();
        final String user_report_tel = userTel.getText().toString().trim();
        final String lat = latTxt.getText().toString().trim();
        final String lon = lonTxt.getText().toString().trim();

        if (TextUtils.isEmpty(note)) {
            userNote.setError("Please enter your note");
            userNote.requestFocus();
            return;
        }

        // custom volley
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs2.URL_UPPIC,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            //After added successful
                            finish();
                            startActivity(new Intent(getApplicationContext(), User_firstPage.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("note", note);
                params.put("user_report_name", user_report_name);
                params.put("user_report_tel", user_report_tel);
                params.put("lat",lat);
                params.put("lon",lon);
                return params;
        }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
    };
        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
//    private void informEmer() {
//        final String note = userNote.getText().toString().trim();
//        final String user_report_name = userName.getText().toString().trim();
//        final String user_report_tel = userTel.getText().toString().trim();
//        final String lat = latTxt.getText().toString().trim();
//        final String lon = lonTxt.getText().toString().trim();
//        String path = getPath(filePath);
//
//        try {
//            String uploadID = UUID.randomUUID().toString();
//            new MultipartUploadRequest(this, uploadID, URLs.URL_INFORMEMER)
//                    .addFileToUpload(path, "image")
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(2)
//                    .startUpload();
//
//        } catch (Exception exc) {
//            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_INFORMEMER,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//
//                            if (!obj.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                finish();
//                                startActivity(new Intent(getApplicationContext(), User_Profile.class));
//                            } else {
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//
//    }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("note", note);
//                params.put("user_report_name", user_report_name);
//                params.put("user_report_tel", user_report_tel);
//                params.put("lat",lat);
//                params.put("lon",lon);
//                return params;
//            }
//        };
//            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//    }

}
}
