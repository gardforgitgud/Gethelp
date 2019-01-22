package gr.mmv.gethelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Officer_Profile extends AppCompatActivity {

    TextView textViewEmail, textViewNatID, textViewName, textViewRole, textViewTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer__profile);

        if (!SharedPrefManager.getInstance(this).isOffLoggedIn()) {
            finish();
            startActivity(new Intent(this, Officer_Login.class));
        }

        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewNatID = (TextView) findViewById(R.id.textViewNatID);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewRole = (TextView) findViewById(R.id.textViewRole);
        textViewTel = (TextView) findViewById(R.id.textViewTel);

        Officer officer = SharedPrefManager.getInstance(this).getOfficer();

        textViewEmail.setText(String.valueOf(officer.getEmail()));
        textViewNatID.setText(officer.getNational_id());
        textViewName.setText(officer.getName());
        textViewRole.setText(officer.getRole());
        textViewTel.setText(officer.getTel());

        findViewById(R.id.LogoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        findViewById(R.id.BackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Officer_Profile.this, Officer_firstpage.class));
            }
        });

        findViewById(R.id.editpassBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Officer_Profile.this, edit_officer.class));
            }
        });

    }
}
