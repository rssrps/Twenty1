package raman.raman.raman.twenty1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import raman.raman.raman.twenty1.R;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i1 = new Intent(splashscreen.this, newUser.class);
                Intent i2=new Intent(splashscreen.this,MainActivity.class);

                if(pref.getString("first","yes").equals("yes"))
                    startActivity(i1);
                else
                    startActivity(i2);

                // close this activity
                finish();
            }
        }, 2500);


    }

}
