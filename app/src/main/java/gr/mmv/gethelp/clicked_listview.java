package gr.mmv.gethelp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class clicked_listview extends AppCompatActivity implements OnMapReadyCallback {

    private TextView latTxt, lonTxt, userName, userTel, userNote, idTxt, statusTxt, datetimeTxt;
    private String emergency_id, status, date_time, note, lat, lon ,emer_img, user_report_name, user_report_tel;
    private GoogleMap mMap;
    private Double latNew, lonNew;
    private ImageView imageView;


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();

        LatLng coordinate = new LatLng(latNew, lonNew);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate,30));

        MarkerOptions marker = new MarkerOptions().position(new LatLng(latNew, lonNew)).title("Need helps!");
        mMap.addMarker(marker);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_listview);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!SharedPrefManager.getInstance(this).isOffLoggedIn()) {
            finish();
            startActivity(new Intent(this, Officer_Login.class));
        }

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(clicked_listview.this, emer_listview.class));
            }
        });

        findViewById(R.id.toAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emergency_id = idTxt.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ACCEPT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);

                                    if (!obj.getBoolean("error")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("emergency_id", emergency_id);;
                        return params;
                    }
                };
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });

        //declare view
        latTxt = (TextView) findViewById(R.id.latTxt);
        lonTxt = (TextView) findViewById(R.id.lonTxt);
        userName = (TextView) findViewById(R.id.userName);
        userTel = (TextView) findViewById(R.id.userTel);
        userNote = (TextView) findViewById(R.id.textView4);
        imageView = (ImageView) findViewById(R.id.imageView);
        idTxt = (TextView) findViewById(R.id.idTxt);
        statusTxt = (TextView) findViewById(R.id.statusTxt);
        datetimeTxt = (TextView) findViewById(R.id.datetimeTxt);


        //get intent
        Intent i = getIntent();
        emergency_id = i.getStringExtra("emergency_id");
        status = i.getStringExtra("status");
        date_time = i.getStringExtra("date_time");
        note = i.getStringExtra("note");
        lat = i.getStringExtra("lat");
        lon = i.getStringExtra("lon");
        emer_img = i.getStringExtra("emer_img");
        user_report_name = i.getStringExtra("user_report_name");
        user_report_tel = i.getStringExtra("user_report_tel");

        //set text on Declared view
        latTxt.setText(lat);
        lonTxt.setText(lon);
        userName.setText(user_report_name);
        userTel.setText(user_report_tel);
        userNote.setText(note);
        idTxt.setText(emergency_id);
        statusTxt.setText(status);
        datetimeTxt.setText(date_time);


        //parse string to double for lat lon
        latNew = Double.parseDouble(lat);
        lonNew = Double.parseDouble(lon);

        Glide.with(this).load(emer_img).into(imageView);

        findViewById(R.id.toGgsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriBegin = "geo:" + latNew + "," + lonNew;
                String query = latNew + "," + lonNew ;
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=30";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

    }
}
