package com.example.matthes.farmero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

/*************** CLASS NOT IN USE *****************/

/**
 * The class:
 * - shows the satellite image slider in a new activity
 */

public class UNUSEDsliderActivity extends AppCompatActivity {

    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);




        seekBar = (SeekBar) findViewById(R.id.seekBar1);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                FrameLayout target = (FrameLayout) findViewById(R.id.target);

                progress = progresValue;

                ViewGroup.LayoutParams lp = target.getLayoutParams();
                lp.height = progress;
                target.setLayoutParams(lp);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

    }
}
