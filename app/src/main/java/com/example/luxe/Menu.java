package com.example.luxe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Menu extends AppCompatActivity {
    CardView cardRooms, cardFacilities, cardServices, cardGallery;
    ImageView btnLogout , btnUserInfo;
    TextView txtToolbarTitle;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menue);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnLogout = findViewById(R.id.logoutBTN);
        cardRooms = findViewById(R.id.card_rooms);
        cardFacilities = findViewById(R.id.card_facilities);
        cardServices = findViewById(R.id.card_services);
        cardGallery = findViewById(R.id.card_gallery);
        txtToolbarTitle = findViewById(R.id.txt_toolbarTitle);

        String username = getIntent().getStringExtra("username");

        if (username != null && !username.isEmpty()) {
            txtToolbarTitle.setText("Hi, " + username);
        } else {
            txtToolbarTitle.setText("Hi, User");
        }

        btnLogout.setOnClickListener(view -> {
            Intent movetoLogin = new Intent(getApplicationContext(), login.class);
            startActivity(movetoLogin);
            Toast.makeText(Menu.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        cardRooms.setOnClickListener(view -> {
            Intent movuetoRoom = new Intent(getApplicationContext(), Rooms.class);
            startActivity(movuetoRoom);
        });

        cardServices.setOnClickListener(view -> {
            Intent movuetoServices = new Intent(getApplicationContext(), srv.class);
            startActivity(movuetoServices);
        });

        cardFacilities.setOnClickListener(view -> {
            Intent moveToFacilities = new Intent(getApplicationContext(), Facilities.class);
            startActivity(moveToFacilities);
        });

        cardGallery.setOnClickListener(view -> {
            Intent movetoGallery = new Intent(getApplicationContext(), Gallery.class);
            startActivity(movetoGallery);
        });

        btnUserInfo = findViewById(R.id.userInfoBTN);
        btnUserInfo.setOnClickListener(view -> {
            Intent moveToUserInfo = new Intent(Menu.this, userinfor.class);
            startActivity(moveToUserInfo);
        });



    }
}
