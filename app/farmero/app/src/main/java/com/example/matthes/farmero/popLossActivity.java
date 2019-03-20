package com.example.matthes.farmero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * The class:
 * - shows the popup "what is loss" to the user
 */

public class popLossActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // constructor
        super.onCreate(savedInstanceState);

        // set xml
        setContentView(R.layout.activity_pop_loss);

        // params
        ImageView buttonLoss;

        // initialize
        buttonLoss = this.findViewById(R.id.pop_loss);

        // listener
        buttonLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // settings for the popup effect
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width-100),(int) (height-300));
    }
}
