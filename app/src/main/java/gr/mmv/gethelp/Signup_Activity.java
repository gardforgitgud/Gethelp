package gr.mmv.gethelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class Signup_Activity extends AppCompatActivity {

    EditText editEmail, editPassword, editName, editTel, editAddress, editNatID;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, User_firstPage.class));
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editName = (EditText) findViewById(R.id.editName);
        editTel = (EditText) findViewById(R.id.editTel);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editNatID = (EditText) findViewById(R.id.editNatID);

        findViewById(R.id.signupBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        findViewById(R.id.backBTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup_Activity.this, MainActivity.class));
            }
        });
    }

    private void registerUser() {
        final String email = editEmail.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        final String name = editName.getText().toString().trim();
        final String tel = editTel.getText().toString().trim();
        final String address = editAddress.getText().toString().trim();
        final String national_id = editNatID.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Please enter email");
            editEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter a valid email");
            editEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Please enter password");
            editPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            editName.setError("Please enter your name");
            editName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tel)) {
            editTel.setError("Please enter your tel. no");
            editTel.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            editAddress.setError("Please enter your address");
            editAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(national_id)) {
            editNatID.setError("Please enter your national ID");
            editNatID.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                JSONObject userJson = obj.getJSONObject("user");

                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("email"),
                                        userJson.getString("name"),
                                        userJson.getString("tel"),
                                        userJson.getString("address"),
                                        userJson.getString("national_id")
                                );

                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                finish();
                                startActivity(new Intent(getApplicationContext(), User_firstPage.class));
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("name", name);
                params.put("tel", tel);
                params.put("address", address);
                params.put("national_id", national_id);
                return params;
            }
        };

           VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
