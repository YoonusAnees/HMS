package com.example.luxe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class deluxroom extends AppCompatActivity {
    Button dbookButton1, dbookButton2, dbookButton3, dbookButton4;
    ImageView toggleIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deluxroom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbookButton1 = findViewById(R.id.bookDeluxeRoomButton1);
        dbookButton2 = findViewById(R.id.bookDeluxeRoomButton2);
        dbookButton3 = findViewById(R.id.bookDeluxeRoomButton3);
        dbookButton4 = findViewById(R.id.bookDeluxeRoomButton4);

        dbookButton1.setOnClickListener(view -> openBookingScreen("Duxel Room 1"));
        dbookButton2.setOnClickListener(view -> openBookingScreen("Duxel Room 2"));
        dbookButton3.setOnClickListener(view -> openBookingScreen("Duxel Room3"));
        dbookButton4.setOnClickListener(view -> openBookingScreen("Duxel Room 4"));


        toggleIcon = findViewById(R.id.toggle_icon);
        toggleIcon.setOnClickListener(view -> {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
        });






    }

    private void openBookingScreen(String roomName) {
        Intent intent = new Intent(this, Booknow.class);
        intent.putExtra("ROOM_NAME", roomName); // Pass the room name to the next activity
        startActivity(intent);
    }
}