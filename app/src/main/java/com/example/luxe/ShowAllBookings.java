package com.example.luxe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ShowAllBookings extends AppCompatActivity {

    private TextView txtToolbarTitle;
    private LinearLayout bookingsLayout;
    private FirebaseFirestore fStore;
    private Map<String, String> userCache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_bookings);

        fStore = FirebaseFirestore.getInstance();
        txtToolbarTitle = findViewById(R.id.txtToolbarTitle);
        bookingsLayout = findViewById(R.id.bookingsLayout);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtToolbarTitle.setText("All Bookings Information");

        fetchAllBookings();
    }

    private void fetchAllBookings() {
        fStore.collection("hotelroom")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String userId = document.getString("User ID");
                            if (userId != null) {
                                fetchUserNameAndDisplayBooking(userId, document);
                            }
                        }
                    } else {
                        showNoRoomBookingsMessage();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load room bookings.", Toast.LENGTH_SHORT).show());
    }

    private void fetchUserNameAndDisplayBooking(String userId, QueryDocumentSnapshot bookingDocument) {
        if (userCache.containsKey(userId)) {
            displayRoomBookingInfo(userCache.get(userId), bookingDocument);
        } else {
            fStore.collection("Users").document(userId)
                    .get()
                    .addOnSuccessListener(userDocument -> {
                        if (userDocument.exists()) {
                            String userName = userDocument.getString("Full Name");
                            if (userName != null) {
                                userCache.put(userId, userName);
                                displayRoomBookingInfo(userName, bookingDocument);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to load user information.", Toast.LENGTH_SHORT).show());
        }
    }

    private void displayRoomBookingInfo(String userName, QueryDocumentSnapshot document) {
        String roomType = document.getString("Room Type");
        String checkInDate = document.getString("Check-In Date");
        String checkOutDate = document.getString("Check-Out Date");
        String adults = document.getString("Adults");
        String children = document.getString("Children");
        final String bookingId = document.getId();

        View bookingItemView = LayoutInflater.from(this).inflate(R.layout.booking_item_admin, bookingsLayout, false);
        TextView bookingInfoText = bookingItemView.findViewById(R.id.bookingInfoText);
        Button updateButton = bookingItemView.findViewById(R.id.updateButton);
        Button deleteButton = bookingItemView.findViewById(R.id.deleteButton);

        bookingInfoText.setText(
                "User: " + userName + "\n" +
                        "Room Booking:\n" +
                        "Room Type: " + roomType + "\n" +
                        "Check-In: " + checkInDate + "\n" +
                        "Check-Out: " + checkOutDate + "\n" +
                        "Adults: " + adults + "\n" +
                        "Children: " + children
        );

        updateButton.setOnClickListener(v -> showUpdateDialog(bookingId, roomType, checkInDate, checkOutDate, adults, children));
        deleteButton.setOnClickListener(v -> deleteBooking(bookingId));

        bookingsLayout.addView(bookingItemView);
    }

    private void showUpdateDialog(final String bookingId, String roomType, String checkInDate, String checkOutDate, String adults, String children) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_booking_for_user, null);
        final EditText editRoomType = dialogView.findViewById(R.id.editRoomType);
        final EditText editCheckInDate = dialogView.findViewById(R.id.editCheckInDate);
        final EditText editCheckOutDate = dialogView.findViewById(R.id.editCheckOutDate);
        final EditText editAdults = dialogView.findViewById(R.id.editAdults);
        final EditText editChildren = dialogView.findViewById(R.id.editChildren);
        editRoomType.setText(roomType);
        editCheckInDate.setText(checkInDate);
        editCheckOutDate.setText(checkOutDate);
        editAdults.setText(adults);
        editChildren.setText(children);

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Update Booking")
                .setPositiveButton("Update", (dialog, which) -> {
                    String updatedRoomType = editRoomType.getText().toString();
                    String updatedCheckInDate = editCheckInDate.getText().toString();
                    String updatedCheckOutDate = editCheckOutDate.getText().toString();
                    String updatedAdults = editAdults.getText().toString();
                    String updatedChildren = editChildren.getText().toString();

                    Map<String, Object> updatedBookingData = new HashMap<>();
                    updatedBookingData.put("Room Type", updatedRoomType);
                    updatedBookingData.put("Check-In Date", updatedCheckInDate);
                    updatedBookingData.put("Check-Out Date", updatedCheckOutDate);
                    updatedBookingData.put("Adults", updatedAdults);
                    updatedBookingData.put("Children", updatedChildren);

                    fStore.collection("hotelroom").document(bookingId)
                            .update(updatedBookingData)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Booking updated.", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to update booking.", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void deleteBooking(final String bookingId) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    fStore.collection("hotelroom").document(bookingId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Booking deleted.", Toast.LENGTH_SHORT).show();
                                bookingsLayout.removeAllViews();
                                fetchAllBookings();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete booking.", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showNoRoomBookingsMessage() {
        TextView noBookingsView = new TextView(this);
        noBookingsView.setText("No room bookings found.");
        noBookingsView.setPadding(16, 16, 16, 16);
        bookingsLayout.addView(noBookingsView);
    }
}
