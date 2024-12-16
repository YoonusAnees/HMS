package com.example.luxe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class adminDashboard extends AppCompatActivity {

    CardView btn_showuser,  btn_showbooking ,btn_showservices;
    Button logout_button  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btn_showuser = findViewById(R.id.BTN_showUsers);
        btn_showbooking = findViewById(R.id.BTN_showBookings);
        logout_button = findViewById(R.id.BTN_Logout);


        btn_showuser.setOnClickListener(view -> {
            Intent moveShowUser = new Intent(adminDashboard.this, showusers.class);
            startActivity(moveShowUser);
        });

        btn_showbooking.setOnClickListener(view -> {
            Intent moveShowBookings = new Intent(adminDashboard.this, ShowAllBookings.class);
            startActivity(moveShowBookings);
        });



        logout_button.setOnClickListener(view -> {

            FirebaseAuth.getInstance().signOut();
            Toast.makeText(adminDashboard.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(adminDashboard.this, login.class));
            finish();
        });
    }
}
