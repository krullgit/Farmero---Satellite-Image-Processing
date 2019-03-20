package com.example.matthes.farmero;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

/**
 * The class:
 * - shows the popup "what is yield" to the user
 */

public class popYieldActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // constructor
        super.onCreate(savedInstanceState);

        // set xml
        setContentView(R.layout.activity_pop_yield);

        // params
        ImageView buttonYield;

        // initialize
        buttonYield = this.findViewById(R.id.pop_yield);

        // listener
        buttonYield.setOnClickListener(new View.OnClickListener() {
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
