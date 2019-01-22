package gr.mmv.gethelp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class emer_listview extends AppCompatActivity {

//    private static final String JSON_URL = "http://192.168.1.44/gethelp/API3.php";
    private static final String JSON_URL = "https://pattanaponggard.000webhostapp.com/API3.php";
    private SwipeRefreshLayout mSwipeRefresh;
    ListView emerListView;

    List<Emergency> emergencyLists;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emer_listview);
        if (!SharedPrefManager.getInstance(this).isOffLoggedIn()) {
            finish();
            startActivity(new Intent(this, Officer_Login.class));
        }

        mSwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buildList();
                mSwipeRefresh.setRefreshing(false);
            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Officer_firstpage.class));
            }
        });


        emerListView = (ListView) findViewById(R.id.emerListView);
        emergencyLists = new ArrayList<>();
        buildList();
//        loadEmerList();


    }

    ArrayList<HashMap<String, String>> emer_list;
    private void parseJson (String json_str) {
        emer_list = new ArrayList<HashMap<String, String>>();
        emer_list.clear();
        try {
            JSONArray emerArray = new JSONArray(json_str);
            for (int i=0; i<emerArray.length();i++){
                JSONObject emerObj = emerArray.getJSONObject(i);
                String emergency_id = emerObj.getString("emergency_id");
                String status = emerObj.getString("status");
                String date_time = emerObj.getString("date_time");
                String note = emerObj.getString("note");
                String lat = emerObj.getString("lat");
                String lon = emerObj.getString("lon");
                String emer_img = emerObj.getString("emer_img");
                String user_report_name = emerObj.getString("user_report_name");
                String user_report_tel = emerObj.getString("user_report_tel");

                HashMap<String, String> record_map = new HashMap<String, String>();
                record_map.put("emergency_id", emergency_id);
                record_map.put("status", status);
                record_map.put("date_time", date_time);
                record_map.put("note", note);
                record_map.put("lat", lat);
                record_map.put("lon", lon);
                record_map.put("emer_img", emer_img);
                record_map.put("user_report_name", user_report_name);
                record_map.put("user_report_tel", user_report_tel);

                emer_list.add(record_map);
            }
            String[] from_data_map = new String[] {"status","date_time","note","user_report_name","user_report_tel"};
            int[] to_view_id = new int[]{R.id.statusTextView, R.id.datetimeTextView, R.id.noteTextView, R.id.userNameTextView, R.id.userTelTextView};
            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), emer_list, R.layout.emer_custom_list, from_data_map, to_view_id);
            emerListView.setAdapter(adapter);
            emerListView.setOnItemClickListener(emerClickLnsr);

        } catch (Exception e) {}
    }
    private AdapterView.OnItemClickListener emerClickLnsr = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int emer_idx = (int) id;
            HashMap<String, String> record_map = emer_list.get(emer_idx);
            String emergency_id = record_map.get("emergency_id");
            String status = record_map.get("status");
            String date_time = record_map.get("date_time");
            String note = record_map.get("note");
            String lat = record_map.get("lat");
            String lon = record_map.get("lon");
            String emer_img = record_map.get("emer_img");
            String user_report_name = record_map.get("user_report_name");
            String user_report_tel = record_map.get("user_report_tel");

            Intent i = new Intent(getApplicationContext(), clicked_listview.class);
            i.putExtra("emergency_id", emergency_id);
            i.putExtra("status", status);
            i.putExtra("date_time", date_time);
            i.putExtra("note", note);
            i.putExtra("lat", lat);
            i.putExtra("lon", lon);
            i.putExtra("emer_img", emer_img);
            i.putExtra("user_report_name", user_report_name);
            i.putExtra("user_report_tel", user_report_tel);
            startActivity(i);
            return;
        }
    };

    private void buildList() {
        getAllEmerTask task = new getAllEmerTask();
        task.execute();
    }

    private class getAllEmerTask extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){
                parseJson(s);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL linkUrl = new URL(JSON_URL);
                HttpURLConnection urlConn = (HttpURLConnection) linkUrl.openConnection();
                urlConn.setRequestMethod("GET");
                InputStream is = urlConn.getInputStream();
                InputStreamReader is_reader = new InputStreamReader(is, "UTF-8");
                BufferedReader buf_reader = new BufferedReader(is_reader);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line=buf_reader.readLine())!=null){
                    sb.append(line);
                }
                is.close();
                return  sb.toString();
            } catch (Exception e) {}
            return null;
        }
    }

//    private void loadEmerList() {
//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//        progressBar.setVisibility(View.VISIBLE);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.INVISIBLE);
//
//                        try {
//
//                            JSONArray emerArray = new JSONArray(response);
//
//                            for (int i = 0; i < emerArray.length(); i++) {
//                                JSONObject emerObj = emerArray.getJSONObject(i);
//
//                                Emergency emergency = new Emergency
//                                        (emerObj.getInt("emergency_id"),
//                                                emerObj.getString("status"),
//                                                emerObj.getString("date_time"),
//                                                emerObj.getString("note"),
//                                                emerObj.getString("lat"),
//                                                emerObj.getString("lon"),
//                                                emerObj.getString("emer_img"),
//                                                emerObj.getString("user_report_name"),
//                                                emerObj.getString("user_report_tel"));
//
//                                emergencyLists.add(emergency);
//                            }
//
//                            EmerListViewAdapter adapter = new EmerListViewAdapter(emergencyLists, getApplicationContext());
//
//                            emerListView.setAdapter(adapter);
//                            emerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    Intent i = new Intent(this, XXX.class);
//                                    i
//                                }
//                            });
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//        );
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        requestQueue.add(stringRequest);
//    }
}
