package gr.mmv.gethelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Officer_firstpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_firstpage);

        if (!SharedPrefManager.getInstance(this).isOffLoggedIn()) {
            finish();
            startActivity(new Intent(this, Officer_Login.class));
        }

        findViewById(R.id.toProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Officer_firstpage.this, Officer_Profile.class));
            }
        });

        findViewById(R.id.LogoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        findViewById(R.id.toEmerList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Officer_firstpage.this, emer_listview.class));
            }
        });
    }
}
