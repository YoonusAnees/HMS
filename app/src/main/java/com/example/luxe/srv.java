package com.example.luxe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class srv extends AppCompatActivity {
    ImageView toggle_icon;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srv);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Button reserveButton1 = findViewById(R.id.reserveButton1);
        Button reserveButton2 = findViewById(R.id.reserveButton2);
        Button reserveButton3 = findViewById(R.id.reserveButton3);

        reserveButton1.setOnClickListener(view -> reserveService("Spa Treatment"));
        reserveButton2.setOnClickListener(view -> reserveService("Fine Dining"));
        reserveButton3.setOnClickListener(view -> reserveService("Poolside Cabana"));

        toggle_icon = findViewById(R.id.toggle_icon);
        toggle_icon.setOnClickListener(view -> {
            Intent intent = new Intent(srv.this, Menu.class);
            startActivity(intent);
        });
    }

    private void reserveService(String serviceName) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            makeReservation(serviceName, selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void makeReservation(String serviceName, String reservationDate) {
        String userId = fAuth.getCurrentUser().getUid();

        if (userId != null) {
            fStore.collection("services")
                    .document(userId)
                    .collection("reservations")
                    .add(new ServiceReservation(serviceName, reservationDate))
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(srv.this, "Service Reserved: " + serviceName + " on " + reservationDate, Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(srv.this, "Failed to reserve service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(srv.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public static class ServiceReservation {
        String serviceName;
        String reservationDate;

        public ServiceReservation(String serviceName, String reservationDate) {
            this.serviceName = serviceName;
            this.reservationDate = reservationDate;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getReservationDate() {
            return reservationDate;
        }
    }
}
