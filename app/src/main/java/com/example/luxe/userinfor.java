package com.example.luxe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class userinfor extends AppCompatActivity {

    private TextView txtToolbarTitle;
    private LinearLayout bookingsLayout;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    ImageView toggleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfor);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        txtToolbarTitle = findViewById(R.id.txtToolbarTitle);
        bookingsLayout = findViewById(R.id.bookingsLayout);
        toggleIcon = findViewById(R.id.toggle_icon);
        toggleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movetomenu = new Intent(userinfor.this, Menu.class);
                startActivity(movetomenu);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (fAuth.getCurrentUser() != null) {
            String userId = fAuth.getCurrentUser().getUid();
            fetchUserDetails(userId);
            fetchRoomBookings(userId);
            fetchServiceBookings(userId);
        } else {
            txtToolbarTitle.setText("Hi, User");
            showNoRoomBookingsMessage();
            showNoServiceBookingsMessage();
        }
    }

    private void fetchUserDetails(String userId) {
        fStore.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("Full Name");
                        txtToolbarTitle.setText(username != null ? "Reservation Information of, " + username : "Reservation Information");
                    }
                })
                .addOnFailureListener(e -> txtToolbarTitle.setText("Hi, User"));
    }

    private void fetchRoomBookings(String userId) {
        fStore.collection("hotelroom")
                .whereEqualTo("User ID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            displayRoomBookingInfo(document);
                        }
                    } else {
                        showNoRoomBookingsMessage();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load room bookings.", Toast.LENGTH_SHORT).show());
    }

    private void fetchServiceBookings(String userId) {
        fStore.collection("services").document(userId)
                .collection("reservations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            displayServiceBookingInfo(document);
                        }
                    } else {
                        showNoServiceBookingsMessage();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load service bookings.", Toast.LENGTH_SHORT).show());
    }

    private void displayRoomBookingInfo(QueryDocumentSnapshot document) {
        String bookingId = document.getId();
        String roomType = document.getString("Room Type");
        String checkInDate = document.getString("Check-In Date");
        String checkOutDate = document.getString("Check-Out Date");

        View bookingView = LayoutInflater.from(this).inflate(R.layout.item_booking_info, bookingsLayout, false);

        TextView roomDetails = bookingView.findViewById(R.id.txtDetails);
        Button btnDelete = bookingView.findViewById(R.id.btnDelete);

        roomDetails.setText("Room Type: " + roomType + "\nCheck-In: " + checkInDate + "\nCheck-Out: " + checkOutDate);

        btnDelete.setOnClickListener(v -> deleteRoomBooking(bookingId));

        bookingsLayout.addView(bookingView);
    }

    private void displayServiceBookingInfo(QueryDocumentSnapshot document) {
        String serviceId = document.getId();
        String serviceName = document.getString("serviceName");
        String reservationDate = document.getString("reservationDate"); // Fetch the reservation date

        View serviceView = LayoutInflater.from(this).inflate(R.layout.item_booking_info, bookingsLayout, false);

        TextView serviceDetails = serviceView.findViewById(R.id.txtDetails);
        Button btnDelete = serviceView.findViewById(R.id.btnDelete);

        // Update the service details text to include the reservation date
        serviceDetails.setText("Service: " + serviceName + "\nDate: " + reservationDate);

        btnDelete.setOnClickListener(v -> deleteServiceBooking(serviceId));

        bookingsLayout.addView(serviceView);
    }
    private void deleteRoomBooking(String bookingId) {
        fStore.collection("hotelroom").document(bookingId)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Room booking deleted successfully.",
                        Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete room booking.", Toast.LENGTH_SHORT).show());
    }
    private void deleteServiceBooking(String serviceId) {
        fStore.collection("services").document(fAuth.getCurrentUser().getUid())
                .collection("reservations").document(serviceId)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Service booking deleted successfully.",
                        Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete service booking.", Toast.LENGTH_SHORT).show());
    }


    private void showNoRoomBookingsMessage() {
        TextView noBookingsMessage = new TextView(this);
        noBookingsMessage.setText("No room bookings found.");
        bookingsLayout.addView(noBookingsMessage);
    }


    private void showNoServiceBookingsMessage() {
        TextView noBookingsMessage = new TextView(this);
        noBookingsMessage.setText("No service bookings found.");
        bookingsLayout.addView(noBookingsMessage);
    }
}
