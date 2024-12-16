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

public class  oceanroom extends AppCompatActivity{
    Button bookButton1, bookButton2, bookButton3, bookButton4;
   ImageView toggleIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oceanroom);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toggleIcon = findViewById(R.id.toggle_icon);
        toggleIcon.setOnClickListener(view -> {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
        });

        bookButton1 = findViewById(R.id.BTNbookOceanRoomButton1);
        bookButton2 = findViewById(R.id.bookOceanRoomButton2);
        bookButton3 = findViewById(R.id.bookOceanRoomButton3);
        bookButton4 = findViewById(R.id.bookOceanRoomButton4);

        bookButton1.setOnClickListener(view -> openBookingScreen("Ocean View Suite 1"));
        bookButton2.setOnClickListener(view -> openBookingScreen("Ocean View Suite 2"));
        bookButton3.setOnClickListener(view -> openBookingScreen("Ocean View Suite 3"));
        bookButton4.setOnClickListener(view -> openBookingScreen("Ocean View Suite 4"));
    }

    private void openBookingScreen(String roomName) {
        Intent intent = new Intent(this, Booknow.class);
        intent.putExtra("ROOM_NAME", roomName); // Pass the room name to the next activity
        startActivity(intent);
    }

}
