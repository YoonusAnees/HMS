package com.example.luxe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    EditText etxt_email, etxt_password;
    Button btn_login;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView txt_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        etxt_email = findViewById(R.id.ETXT_mail);
        etxt_password = findViewById(R.id.ETXT_password);
        btn_login = findViewById(R.id.BTN_Login);
        txt_registration = findViewById(R.id.TXT_Registration);

        btn_login.setOnClickListener(view -> {
            String email = etxt_email.getText().toString().trim();
            String pass = etxt_password.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            fAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(login.this, "Login successfully", Toast.LENGTH_SHORT).show();

                        // Fetch user details
                        String userId = fAuth.getCurrentUser().getUid();
                        fStore.collection("Users").document(userId)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        String isAdmin = documentSnapshot.getString("isAdmin");
                                        if ("1".equals(isAdmin)) {
                                            // Navigate to admin dashboard
                                            Intent adminIntent = new Intent(login.this, adminDashboard.class);
                                            startActivity(adminIntent);

                                        } else {
                                            String username = documentSnapshot.getString("Full Name");
                                            Intent intent = new Intent(login.this, Menu.class);
                                            intent.putExtra("username", username);  // Pass username here
                                            startActivity(intent);
                                            finish();

                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(login.this, "Failed to fetch user info: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(login.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        txt_registration.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
