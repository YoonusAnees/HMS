package com.example.luxe;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class showusers extends AppCompatActivity {

    FirebaseFirestore fStore;
    RecyclerView recyclerView;
    UsersAdapter usersAdapter;
    ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showusers);

        // Initialize Firebase Firestore
        fStore = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, userList);
        recyclerView.setAdapter(usersAdapter);

        // Load the list of users
        loadUserList();
    }

    private void loadUserList() {
        fStore.collection("Users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String fullName = document.getString("Full Name");
                        String phone = document.getString("Phone");
                        String email = document.getString("Email");
                        String password = document.getString("Password");
                        String isAdmin = document.getString("isAdmin");

                        // Add only non-admin users to the list
                        if ("0".equals(isAdmin) && fullName != null && phone != null && email != null && password != null) {
                            userList.add(new User(document.getId(), fullName, phone, email, password));
                        }
                    }
                    usersAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load user data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
