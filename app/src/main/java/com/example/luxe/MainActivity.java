package com.example.luxe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView txt_tvreg;
    EditText etxt_username, etxt_phone, etxt_email, etxt_password;
    Button btn_Register;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Link UI components
        btn_Register = findViewById(R.id.BTN_Register);
        etxt_username = findViewById(R.id.ETXT_Username);
        etxt_phone = findViewById(R.id.ETXT_phone);
        etxt_email = findViewById(R.id.ETXT_mail);
        etxt_password = findViewById(R.id.ETXT_password);
        txt_tvreg = findViewById(R.id.TXT_TVLogin);

        // Handle "Login" text click
        txt_tvreg.setOnClickListener(view -> {
            Intent moveReg = new Intent(MainActivity.this, login.class);
            startActivity(moveReg);
        });

        btn_Register.setOnClickListener(view -> {
            if (areFieldsValid()) {
                String email = etxt_email.getText().toString().trim();
                String username = etxt_username.getText().toString().trim();
                String phone = etxt_phone.getText().toString().trim();
                String pass = etxt_password.getText().toString().trim();

                fAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(authResult -> {
                            Toast.makeText(MainActivity.this, "Registration Successful ", Toast.LENGTH_SHORT).show();

                            DocumentReference df = fStore.collection("Users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Full Name", username);
                            userInfo.put("Phone", phone);
                            userInfo.put("Email", email);
                            userInfo.put("Password", pass);
                            userInfo.put("isAdmin", "0");
                            df.set(userInfo).addOnSuccessListener(unused -> {
                                Toast.makeText(MainActivity.this, "User info saved", Toast.LENGTH_SHORT).show();

                                // Pass the username to the Login activity
                                Intent intent = new Intent(MainActivity.this, login.class);
                                intent.putExtra("username", username);  // Pass the username here
                                startActivity(intent);
                                finish();

                            });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MainActivity.this, "Failed to Create Account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private boolean areFieldsValid() {
        boolean isValid = true;

        if (etxt_username.getText().toString().isEmpty()) {
            etxt_username.setError("Required");
            isValid = false;
        }
        if (etxt_phone.getText().toString().isEmpty()) {
            etxt_phone.setError("Required");
            isValid = false;
        }
        if (etxt_email.getText().toString().isEmpty()) {
            etxt_email.setError("Required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etxt_email.getText().toString()).matches()) {
            etxt_email.setError("Invalid email format");
            isValid = false;
        }
        if (etxt_password.getText().toString().isEmpty()) {
            etxt_password.setError("Required");
            isValid = false;
        } else if (etxt_password.getText().toString().length() < 6) {
            etxt_password.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }
}
