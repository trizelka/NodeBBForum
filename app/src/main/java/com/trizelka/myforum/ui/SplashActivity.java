package com.trizelka.myforum.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.trizelka.myforum.Constants;
import com.trizelka.myforum.R;
import com.trizelka.myforum.ForumService;
import com.trizelka.myforum.utils.UserData;


public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        background();
    }

    private void openLogin() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    public void background() {
        new Handler().postDelayed(new Runnable() {
            /*
			 * Showing splash_activity screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                String getUser = UserData.getName(getApplicationContext());
                String getToken = UserData.getToken(getApplicationContext());
                if (getUser=="" && getToken=="") {
                    openLogin();
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), ForumService.class);
                    intent1.setAction(ForumService.ACTION_GET_PUBLIC);
                    getApplicationContext().startService(intent1);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
