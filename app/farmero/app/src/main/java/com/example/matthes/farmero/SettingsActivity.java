package com.example.matthes.farmero;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/**
 * The class:
 * - shows the view "settings" to the farmer
 * - gives logout functionality
 */

public class SettingsActivity extends AppCompatActivity {

    //parameter
    Button btnLogOut;
    private EditText v;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    // constructor
    public SettingsActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //constructor
        super.onCreate(savedInstanceState);

        // set xml
        setContentView(R.layout.activity_settings);

        // set variables
        btnLogOut = findViewById(R.id.btnLogOut);

        // Listener
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(I);
            }

        });

        // get user email and place it inside the xml
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userid=user.getEmail();
        v = (EditText) findViewById(R.id.email);
        SharedPreferences settings = this.getSharedPreferences("PREFS", 0);
        v.setText(settings.getString("value", userid));
    }
}















///**
// * A simple {@link Fragment} subclass.
// */
//public class SettingsActivity extends Fragment {
//
//    Button btnLogOut;
//    private EditText v;
//    FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//
//    public SettingsActivity() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.activity_settings, container, false);
//
//        // set variables
//        btnLogOut = view.findViewById(R.id.btnLogOut);
//
//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent I = new Intent(getActivity(), LoginActivity.class);
//                startActivity(I);
//            }
//        });
//
//        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
//        String userid=user.getEmail();
//
//        v = (EditText) view.findViewById(R.id.email);
//        SharedPreferences settings = this.getActivity().getSharedPreferences("PREFS", 0);
//        v.setText(settings.getString("value", userid));
//
//
//        return view;
//
//    }
//
//}
