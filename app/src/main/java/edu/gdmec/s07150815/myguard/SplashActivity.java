package edu.gdmec.s07150815.myguard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by D on 2016/12/21.
 */
public class SplashActivity extends AppCompatActivity {
    private TextView mVersionTV;
    private String mVersion;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


    }
}
