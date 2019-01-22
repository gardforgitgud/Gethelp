package gr.mmv.gethelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class edit_user extends AppCompatActivity {

    TextView textViewEmail, textViewNatID, textViewName, textViewTel, textViewAddress;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, User_Login.class));
        }

        findViewById(R.id.BackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), User_Profile.class));
            }
        });

        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewNatID = (TextView) findViewById(R.id.textViewNatID);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewTel = (TextView) findViewById(R.id.textViewTel);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        editPassword = (EditText) findViewById(R.id.editPassword);

        User user = SharedPrefManager.getInstance(this).getUser();

        textViewEmail.setText(String.valueOf(user.getEmail()));
        textViewNatID.setText(user.getNational_id());
        textViewName.setText(user.getName());
        textViewTel.setText(user.getTel());
        textViewAddress.setText(user.getAddress());

        findViewById(R.id.editPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = textViewEmail.getText().toString();
                final String password = editPassword.getText().toString().trim();

                if (TextUtils.isEmpty(password)){
                    editPassword.setError("Please enter your new password");
                    editPassword.requestFocus();
                    return;
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_EDITUSE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject obj = new JSONObject(response);

                                    if (!obj.getBoolean("error")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                                        finish();
                                        startActivity(new Intent(getApplicationContext(), User_Profile.class));
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
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }
                };
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }

        });
    }


}
