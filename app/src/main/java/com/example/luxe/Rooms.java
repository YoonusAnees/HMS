package com.example.luxe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Rooms extends AppCompatActivity {
    Button btn_Oceanroom, btnDeluxroom;
    ImageView toggleIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rooms);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_Oceanroom = findViewById(R.id.BTNOcenaRoom);
        btnDeluxroom = findViewById(R.id.BTNdulexRoom);
        toggleIcon = findViewById(R.id.toggle_icon);

       toggleIcon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent movetomenu = new Intent(Rooms.this, Menu.class);
               startActivity(movetomenu);
           }
       });


        btn_Oceanroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToOceanRoom = new Intent(getApplicationContext(), oceanroom.class); // Updated to OceanRoom
                startActivity(moveToOceanRoom);
            }
        });


        btnDeluxroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToDeluxRoom = new Intent(getApplicationContext(), deluxroom.class);
                startActivity(moveToDeluxRoom);
            }
        });
    }
}