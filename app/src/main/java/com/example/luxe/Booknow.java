package com.example.luxe;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Booknow extends AppCompatActivity {
    private EditText checkInDate, checkOutDate;
    private TextView roomTypeSelected;
    private Spinner adultsSpinner, childrenSpinner;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booknow);

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Handle edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        roomTypeSelected = findViewById(R.id.roomTypeSelected);
        adultsSpinner = findViewById(R.id.adultsSpinner);
        childrenSpinner = findViewById(R.id.childrenSpinner);
        Button confirmBookingButton = findViewById(R.id.confirmBookingButton);

        // Get the room name from the intent and set it
        String roomName = getIntent().getStringExtra("ROOM_NAME");
        roomTypeSelected.setText(roomName != null ? roomName : "Select a room");

        // Initialize Spinners
        ArrayAdapter<CharSequence> adultsAdapter = ArrayAdapter.createFromResource(this,
                R.array.adults_numbers, android.R.layout.simple_spinner_item);
        adultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adultsSpinner.setAdapter(adultsAdapter);

        ArrayAdapter<CharSequence> childrenAdapter = ArrayAdapter.createFromResource(this,
                R.array.children_numbers, android.R.layout.simple_spinner_item);
        childrenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childrenSpinner.setAdapter(childrenAdapter);

        checkInDate.setOnClickListener(v -> showDatePicker(checkInDate));
        checkOutDate.setOnClickListener(v -> showDatePicker(checkOutDate));

        confirmBookingButton.setOnClickListener(v -> {
            String roomType = roomTypeSelected.getText().toString();
            String checkIn = checkInDate.getText().toString();
            String checkOut = checkOutDate.getText().toString();
            String adults = adultsSpinner.getSelectedItem().toString();
            String children = childrenSpinner.getSelectedItem().toString();

            if (roomType.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
                Toast.makeText(Booknow.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
            if (userId == null) {
                Toast.makeText(Booknow.this, "User not logged in. Please log in to book.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("Room Type", roomType);
            bookingData.put("Check-In Date", checkIn);
            bookingData.put("Check-Out Date", checkOut);
            bookingData.put("Adults", adults);
            bookingData.put("Children", children);
            bookingData.put("User ID", userId); // Save the user ID

            firestore.collection("hotelroom")
                    .add(bookingData)
                    .addOnSuccessListener(documentReference ->
                            Toast.makeText(Booknow.this, "Booking confirmed! ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(Booknow.this, "Failed to save booking: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    private void showDatePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) ->
                        editText.setText((selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear),
                year, month, day);
        datePickerDialog.show();
    }
}
