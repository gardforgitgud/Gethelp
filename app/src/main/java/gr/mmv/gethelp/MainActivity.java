package gr.mmv.gethelp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.signupBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Signup_Activity.class));
            }
        });

        findViewById(R.id.use_logBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, User_Login.class));
            }
        });
        findViewById(R.id.off_logBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Officer_Login.class));
            }
        });
//
//        if (SharedPrefManager.getInstance(this).isOffLoggedIn()) {
//            finish();
//            startActivity(new Intent(this, Officer_firstpage.class));
//            return;
//        }
//        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
//           finish();
//           startActivity(new Intent(this, User_firstPage.class));
//           return;
//         }


    }
}
