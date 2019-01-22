package gr.mmv.gethelp;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class User_Profile extends AppCompatActivity{


    TextView textViewEmail, textViewNatID, textViewName, textViewTel, textViewAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);


        //is user logged in
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, User_Login.class));
        }

        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewNatID = (TextView) findViewById(R.id.textViewNatID);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewTel = (TextView) findViewById(R.id.textViewTel);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        User user = SharedPrefManager.getInstance(this).getUser();

        textViewEmail.setText(String.valueOf(user.getEmail()));
        textViewNatID.setText(user.getNational_id());
        textViewName.setText(user.getName());
        textViewTel.setText(user.getTel());
        textViewAddress.setText(user.getAddress());

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
                startActivity(new Intent(User_Profile.this, User_firstPage.class));
            }
        });

        findViewById(R.id.editPasswordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Profile.this, edit_user.class));
            }
        });
    }
}
